import { useEffect, useState } from "react";
import { Modal, Button, Form, Tabs, Tab } from "react-bootstrap";
import toast from "react-hot-toast";

import {
  updateProduct,
  deleteProduct,
  transferInventory,
  getAllWarehouses
} from "../api/warehouseApi";


export default function ProductModal({ item, warehouseId, show, onClose, categories, onTransferSuccess }) {


  const [product, setProduct] = useState(item.product); 


  const [destinationId, setDestinationId] = useState(0);
  const [warehouses, setWarehouses] = useState([]);

 
  useEffect(() => {
    const loadWarehouses = async () => {
      const allWarehouses = await getAllWarehouses();
      const availableWarehouses = allWarehouses.filter(w => w.warehouseId !== warehouseId);
      setWarehouses(availableWarehouses);
    };
    loadWarehouses();
  }, [warehouseId]);

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

  const handleDelete = async () => {
    try {
      await deleteProduct(item.product.publicId);

      toast.success("Product deleted!");
        onTransferSuccess(item.product.publicId);
      onClose();
    } catch (err) {
      console.error(err);
      toast.error("Delete failed");
    }
  };

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
        quantity: item.quantity,
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