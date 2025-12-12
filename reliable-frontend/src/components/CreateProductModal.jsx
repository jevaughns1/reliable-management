import { useState, useEffect } from "react";
import { Modal, Button, Form, FormGroup } from "react-bootstrap";
import toast from "react-hot-toast";
import { createProduct, getAllCategories, addProductToWarehouse, getAllWarehouses } from "../api/warehouseApi";

/**
 * @typedef {object} ProductData
 * @property {string} name - Product name.
 * @property {string} sku - Stock Keeping Unit.
 * @property {string} description - Product description.
 * @property {string} categoryId - The ID of the product's category.
 * @property {string} unit - The unit of measure (e.g., 'EA', 'KG').
 * @property {string} price - The unit price.
 * @property {boolean} isHazardous - Flag if the product is hazardous.
 * @property {boolean} expirationRequired - Flag if an expiration date must be provided.
 * @property {string} storageLocation - The specific storage location in the warehouse.
 */

/**
 * Modal component for creating a new product and immediately adding its initial stock
 * to a selected warehouse.
 *
 * @param {object} props
 * @param {boolean} props.show - Controls the visibility of the modal.
 * @param {function} props.onClose - Function to be called when the modal is closed.
 * @param {function} props.onCreated - Function to be called after a product is successfully created and stocked (e.g., to refresh lists).
 * @author Jevaughn Stewart
 * @returns {JSX.Element}
 */
export default function CreateProductModal({ show, onClose, onCreated }) {

  /**
   * State for the core product attributes.
   * @type {[ProductData, function]}
   */
  const [productData, setProductData] = useState({
    name: "",
    sku: "",
    description: "",
    categoryId: "",
    unit: "",
    price: "",
    isHazardous: false,
    expirationRequired: false,
    storageLocation: ""
  });
  
  /**
   * State for categories loaded from the API to populate the select dropdown.
   * @type {[Array<object>, function]}
   */
  const [categories, setCategories] = useState([]);
  
  /**
   * State for warehouses loaded from the API to populate the destination dropdown.
   * @type {[Array<object>, function]}
   */
  const [warehouses, setWarehouses] = useState([]);
  
  /**
   * State for the ID of the warehouse where initial stock will be placed.
   * @type {[number, function]}
   */
  const [destinationId, setDestinationId] = useState(0);
  
  /**
   * State for the initial quantity of stock to be added.
   * @type {[number, function]}
   */
  const [quantity, setQuantity] = useState(1);

  /**
   * State for the batch expiration date (string 'YYYY-MM-DD').
   * @type {[string, function]}
   */
  const [initialExpirationDate, setInitialExpirationDate] = useState(""); 

  /**
   * Generic handler for updating fields in the productData state object.
   * @param {string} field - The key of the field to update.
   * @param {*} value - The new value for the field.
   */
  const handleProductChange = (field, value) => {
    setProductData(prev => ({ ...prev, [field]: value }));
  };
  

  /**
   * useEffect hook to load all available warehouses once on component mount.
   * Warehouses are used for selecting the initial stock destination.
   */
  useEffect(() => {
    const loadWarehouses = async () => {
      try {
        const allWarehouses = await getAllWarehouses();
        
        const filteredWarehouses = allWarehouses.map(warehouse => ({
          warehouseId: warehouse.warehouseId,
          name: warehouse.name,
          location: warehouse.location
        }));

        setWarehouses(filteredWarehouses);
      } catch (err) {
        console.error(err);
        toast.error("Failed to load warehouses");
      }
    };
    loadWarehouses();
  }, []);

  /**
   * useEffect hook to load all available categories when the modal is shown.
   * Categories are used for product classification.
   */
  useEffect(() => {
    const loadCats = async () => {
      try {
        const data = await getAllCategories();
        setCategories(data);
      } catch (err) {
        console.error(err);
        toast.error("Failed to load categories");
      }
    }

    if (show) loadCats();
  }, [show]);


  /**
   * Resets all form states and calls the parent's onClose handler to dismiss the modal.
   */
  const handleClose = () => {
    
      setProductData({
        name: "", sku: "", description: "", categoryId: "", unit: "", price: "",
        isHazardous: false, expirationRequired: false,  storageLocation: ""
      });
     
      setDestinationId(0);
      setQuantity(1);
      setInitialExpirationDate("");
      onClose();
  }

  /**
   * Handles the creation process:
   * 1. Performs client-side validation.
   * 2. Calls {@link createProduct} to register the new product.
   * 3. Calls {@link addProductToWarehouse} to place the initial stock.
   * 4. Shows success toast and calls {@link handleClose} and {@link onCreated}.
   * * @async
   */
  const handleCreate = async () => {
   // --- Validation ---
   if (!productData.name || !productData.sku) {
      toast.error("Name and SKU are required.");
      return;
    }
    if (Number(quantity) <= 0) {
      toast.error("Quantity must be greater than zero.");
      return;
    }
    if (!productData.categoryId) {
      toast.error("Please select a category");
      return;
    }
    if (!destinationId || destinationId === 0) {
      toast.error("Please select a destination warehouse.");
      return;
    }
    // Validation for expiration date if required by the product
    if (productData.expirationRequired && !initialExpirationDate) {
        toast.error("This product requires an expiration date.");
        return;
    }

    try {
      // 1. Create the Product
      const createdProduct = await createProduct({
        name: productData.name,
        sku: productData.sku,
        description: productData.description,
        categoryId: Number(productData.categoryId),
        unit: productData.unit,
        price: Number(productData.price),
        isHazardous: productData.isHazardous,
        expirationRequired: productData.expirationRequired,
      });

      // 2. Prepare Stock DTO
      const stockDto = {
        productPublicId: createdProduct.publicId, // Use the publicId returned from creation
        quantity: Number(quantity),
        storageLocation: productData.storageLocation,
        // Send null if not required/provided, otherwise send the date string
        expirationDate: initialExpirationDate || null 
      };
   
      // 3. Add Stock to Warehouse
      await addProductToWarehouse(Number(destinationId), stockDto); 

    
      toast.success("Product created and initial stock added!");
      handleClose(); 
      onCreated();
      
    } catch (error) {
      console.error(error);
      // More descriptive error message
      toast.error("Failed to complete product setup (Creation or Stocking failed). Check console for details.");
    }
  };


  return (
    <Modal show={show} onHide={handleClose} centered>
      <Modal.Header closeButton>
        <Modal.Title>Create New Product</Modal.Title>
      </Modal.Header>

      <Modal.Body>
        <Form>
          {/* --- Product Metadata Fields --- */}
          <Form.Group className="mb-2">
            <Form.Label>Name</Form.Label>
            <Form.Control 
              value={productData.name} 
              onChange={(e) => handleProductChange("name", e.target.value)} 
            />
          </Form.Group>

          <Form.Group className="mb-2">
            <Form.Label>SKU</Form.Label>
            <Form.Control 
              value={productData.sku} 
              onChange={(e) => handleProductChange("sku", e.target.value)} 
            />
          </Form.Group>

          <Form.Group className="mb-2">
            <Form.Label>Description</Form.Label>
            <Form.Control 
              as="textarea" 
              value={productData.description} 
              onChange={(e) => handleProductChange("description", e.target.value)} 
            />
          </Form.Group>

          <Form.Group className="mb-2">
            <Form.Label>Category</Form.Label>
            <Form.Select 
              value={productData.categoryId} 
              onChange={(e) => handleProductChange("categoryId", e.target.value)}
            >
              <option value="">Select category…</option>
              {categories.map((c) => (
                <option key={c.id} value={c.id}>
                  {c.name}
                </option>
              ))}
            </Form.Select>
          </Form.Group>

          <Form.Group className="mb-2">
            <Form.Label>Unit</Form.Label>
            <Form.Control 
              value={productData.unit} 
              onChange={(e) => handleProductChange("unit", e.target.value)} 
            />
          </Form.Group>

          <Form.Group className="mb-2">
            <Form.Label>Price</Form.Label>
            <Form.Control 
              type="number" 
              step="0.01"
              value={productData.price} 
              onChange={(e) => handleProductChange("price", e.target.value)} 
            />
          </Form.Group>
          
          <hr className="my-3"/>
          <h5>Initial Stock Placement</h5>

          {/* --- Initial Stock Fields --- */}
          <Form.Group className="mb-2">
            <Form.Label>Destination Warehouse</Form.Label>
            <Form.Select 
                aria-label="Warehouse Selection"
                value={destinationId}
                onChange={(e) => setDestinationId(Number(e.target.value))}
            >
              <option value={0}>Select a Warehouse</option>
              {warehouses.map((w) => (
                <option key={w.warehouseId} value={w.warehouseId}>
                  {w.name} — {w.location}
                </option>
              ))}
            </Form.Select>
          </Form.Group>
          
         <Form.Group className="mb-2">
            <Form.Label>Storage Location</Form.Label>
            <Form.Control 
              value={productData.storageLocation} 
              onChange={(e) => handleProductChange("storageLocation", e.target.value)} 
            />
          </Form.Group>
          
          <Form.Group className="mb-3">
            <Form.Label>Initial Quantity</Form.Label>
            <Form.Control 
                type="number" 
                min="1" 
                value={quantity} 
                onChange={(e) => setQuantity(e.target.value)} 
            />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Batch Expiration Date</Form.Label>
            <Form.Control
                type="date"
                value={initialExpirationDate}
                onChange={(e) => setInitialExpirationDate(e.target.value)}
                required={productData.expirationRequired}
            />
            {productData.expirationRequired && (
                <Form.Text className="text-danger">
                    This product requires an expiration date.
                </Form.Text>
            )}
          </Form.Group>
          
          <hr className="my-3"/>


          {/* --- Flags --- */}
          <Form.Check
            className="mt-2"
            label="Hazardous Material"
            checked={productData.isHazardous}
            onChange={(e) => handleProductChange("isHazardous", e.target.checked)}
          />
          <Form.Check
            className="mt-2"
            label="Requires Expiration Date"
            checked={productData.expirationRequired}
            onChange={(e) => handleProductChange("expirationRequired", e.target.checked)}
          />

          <Button variant="primary" className="w-100 mt-3" onClick={handleCreate}>
            Create Product & Add Stock
          </Button>
        </Form>
      </Modal.Body>

      <Modal.Footer>
        <Button variant="secondary" onClick={handleClose}>
          Close
        </Button>
      </Modal.Footer>
    </Modal>
  );
}