import { useEffect, useState } from "react";
import { Modal, Button, Form, Tabs, Tab } from "react-bootstrap";
import toast from "react-hot-toast";

import {
  updateProduct,
  deleteProduct,
  transferInventory,
  getAllWarehouses
} from "../api/warehouseApi";

/**
 * @file ProductModal.jsx
 * @author Jevaughn Stewart
 * @version 1.0
 */

/**
 * Modal component for viewing, editing, deleting, or transferring a specific product's inventory
 * from its current warehouse location.
 * Uses tabs to separate product metadata editing and inventory transfer actions.
 *
 * @param {object} props
 * @param {object} props.item - The {@code WarehouseInventoryDTO} item containing product and inventory details.
 * @param {number} props.warehouseId - The ID of the source warehouse where the inventory is currently located.
 * @param {boolean} props.show - Controls the visibility of the modal.
 * @param {function} props.onClose - Function to be called when the modal is closed.
 * @param {Array<object>} props.categories - List of all categories for the dropdown selection.
 * @param {function} props.onTransferSuccess - Callback function invoked after a successful transfer or deletion (to trigger inventory list refresh).
 * @returns {JSX.Element|null}
 */
export default function ProductModal({ item, warehouseId, show, onClose, categories, onTransferSuccess }) {

  /**
   * State for the product data, initialized from the item prop. Used for the Edit tab.
   * @type {[object, function]}
   */
  const [product, setProduct] = useState(item.product); 


  /**
   * State for the selected destination warehouse ID for transfers.
   * @type {[number, function]}
   */
  const [destinationId, setDestinationId] = useState(0);
  
  /**
   * State for the list of available warehouses (excluding the current one) for transfers.
   * @type {[Array<object>, function]}
   */
  const [warehouses, setWarehouses] = useState([]);

 
  /**
   * useEffect hook to load all available warehouses, filtering out the current warehouse
   * to prevent self-transfers. Runs when {@code warehouseId} changes.
   */
  useEffect(() => {
    const loadWarehouses = async () => {
      try {
        const allWarehouses = await getAllWarehouses();
        // Filter out the source warehouse
        const availableWarehouses = allWarehouses.filter(w => w.warehouseId !== warehouseId);
        setWarehouses(availableWarehouses);
      } catch (err) {
        console.error("Failed to load warehouses for transfer:", err);
        // Do not block the modal, but alert the user if transfers might be unavailable
        toast.error("Failed to load warehouses for transfer list.");
      }
    };
    loadWarehouses();
  }, [warehouseId]);

  /**
   * Handles saving the edited product metadata using the {@link updateProduct} API call (PUT semantics).
   * @async
   */
  const handleUpdate = async () => {
    try {
      await updateProduct(item.product.publicId, { ...product });
      toast.success("Product updated!");
      onClose();
    } catch (err) {
      console.error(err);
      toast.error("Update failed");
    }
  };

  /**
   * Handles the logical deletion of the product using the {@link deleteProduct} API call.
   * Calls the success handler to refresh the main inventory list.
   * @async
   */
  const handleDelete = async () => {
    try {
      await deleteProduct(item.product.publicId);

      toast.success("Product deleted!");
      // Signal to the parent component that this product is gone
      if (onTransferSuccess) {
          onTransferSuccess(item.product.publicId);
      }
      onClose();
    } catch (err) {
      console.error(err);
      toast.error("Delete failed");
    }
  };

  /**
   * Handles the full inventory transfer of the product from the source to the destination warehouse.
   * Calls the success handler to refresh the main inventory list.
   * @async
   */
  const handleTransfer = async () => {
    try {
      if (!destinationId || destinationId === 0) {
        toast.error("Please select a destination warehouse.");
        return;
      }

      await transferInventory({
        productPublicId: item.product.publicId,
        sourceWarehouseId: warehouseId,
        destinationWarehouseId: parseInt(destinationId),
        quantity: item.quantity, // Transfers the entire quantity
      });

      toast.success("Product transferred!");
      setDestinationId(0);
    
      if (onTransferSuccess) {
          onTransferSuccess(item.product.publicId);
      }
      
      onClose();
    } catch (err) {
      console.error(err);
      toast.error("Transfer failed");
    }
  
  };

  // Guard clause if item data is missing
  if (!item || !item.product) {
    return null;
  }
  
  return (
    <Modal show={show} onHide={onClose} centered size="lg">
      <Modal.Header closeButton>
        <Modal.Title>Edit Product — {product.name}</Modal.Title>
      </Modal.Header>

      <Modal.Body>
        <Tabs defaultActiveKey="edit" className="mb-3">
          {/* --- EDIT PRODUCT METADATA TAB --- */}
          <Tab eventKey="edit" title="Edit Product">
            <Form>

              <Form.Group className="mb-3">
                <Form.Label>Name</Form.Label>
                <Form.Control
                  value={product.name}
                  onChange={(e) =>
                    setProduct({ ...product, name: e.target.value })
                  }
                />
              </Form.Group>

              <Form.Group className="mb-3">
                <Form.Label>SKU</Form.Label>
                <Form.Control
                  value={product.sku}
                  onChange={(e) =>
                    setProduct({ ...product, sku: e.target.value })
                  }
                />
              </Form.Group>

              <Form.Group className="mb-3">
                <Form.Label>Description</Form.Label>
                <Form.Control
                  as="textarea"
                 
                  value={product.description}
                  onChange={(e) =>
                    setProduct({ ...product, description: e.target.value })
                  }
                />
              </Form.Group>

              <Form.Group className="mb-3">
                  <Form.Label>Categories</Form.Label>
         <Form.Select 
    aria-label="Category Selection"
    value={product.categoryId || ""}
    onChange={(e) => {
      const newCategoryId = e.target.value ? Number(e.target.value) : 0;
      setProduct({
        ...product,
        categoryId: newCategoryId
      });
    }}
  >
        <option value="">Select a Category</option>
        
        {categories.map((cat) => (
           
          <option key={cat.id} value={cat.id}>
            {cat.name}
          </option>
        ))}
      </Form.Select>
              </Form.Group>

              <Form.Group className="mb-3">
                <Form.Label>Unit</Form.Label>
                <Form.Control
                  value={product.unit}
                  onChange={(e) =>
                    setProduct({ ...product, unit: e.target.value })
                  }
                />
              </Form.Group>

              <Form.Group className="mb-3">
                <Form.Label>Price</Form.Label>
                <Form.Control
                  type="number"
                  step="0.01"
                  value={product.price}
                  onChange={(e) =>
                    setProduct({ ...product, price: Number(e.target.value) })
                  }
                />
              </Form.Group>

              <Form.Group className="mb-3">
                <Form.Check
                  label="Hazardous Material?"
                  checked={product.isHazardous}
                  onChange={(e) =>
                    setProduct({ ...product, isHazardous: e.target.checked })
                  }
                />
              </Form.Group>

              <Form.Group className="mb-3">
                <Form.Check
                  label="Expiration Required?"
                  checked={product.expirationRequired}
                  onChange={(e) =>
                    setProduct({
                      ...product,
                      expirationRequired: e.target.checked,
                    })
                  }
                />
              </Form.Group>

              <Button variant="primary" className="w-100" onClick={handleUpdate}>
                Save Changes
              </Button>

              <Button
                variant="danger"
                className="w-100 mt-2"
                onClick={handleDelete}
              >
                Delete Product
              </Button>
            </Form>
          </Tab>
          
          {/* --- TRANSFER INVENTORY TAB --- */}
          <Tab eventKey="transfer" title="Transfer Inventory">
            <Form>
              <Form.Group className="mb-3">
                <Form.Label>Destination Warehouse</Form.Label>
                <Form.Select 
    aria-label="Warehouse Selection"
    value={destinationId}
    onChange={(e) => {
      setDestinationId(Number(e.target.value));
    }}
  >
        <option value={0}>Select a Warehouse</option>
        
        {warehouses.map((w) => (
           
          <option key={w.warehouseId} value={w.warehouseId}>
            {w.name} — {w.location}
          </option>
        ))}
      </Form.Select>
              </Form.Group>

              <Button
                variant="warning"
                className="w-100"
                onClick={handleTransfer}
                disabled={destinationId === 0}
              >
                Transfer Product
              </Button>
            </Form>
          </Tab>
        </Tabs>
      </Modal.Body>

      <Modal.Footer>
        <Button variant="secondary" onClick={onClose}>
          Close
        </Button>
      </Modal.Footer>
    </Modal>
  );
}