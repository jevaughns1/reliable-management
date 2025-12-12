import { useState, useEffect } from "react";
import { Modal, Button, Form, FormGroup } from "react-bootstrap";
import toast from "react-hot-toast";
import { createProduct, getAllCategories, addProductToWarehouse, getAllWarehouses } from "../api/warehouseApi";

export default function CreateProductModal({ show, onClose, onCreated }) {

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
  
  const [categories, setCategories] = useState([]);
  const [warehouses, setWarehouses] = useState([]);
  const [destinationId, setDestinationId] = useState(0);
  const [quantity, setQuantity] = useState(1);

  const [initialExpirationDate, setInitialExpirationDate] = useState(""); 

  const handleProductChange = (field, value) => {
    setProductData(prev => ({ ...prev, [field]: value }));
  };
  

  useEffect(() => {
    const loadWarehouses = async () => {
      const allWarehouses = await getAllWarehouses();
      
      const filteredWarehouses = allWarehouses.map(warehouse => ({
        warehouseId: warehouse.warehouseId,
        name: warehouse.name,
        location: warehouse.location
      }));

      setWarehouses(filteredWarehouses);
    };
    loadWarehouses();
  }, []);

  // Existing effect to load categories
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

  const handleCreate = async () => {
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

      const stockDto = {
        productPublicId: createdProduct.publicId,
        quantity: Number(quantity),
        storageLocation: productData.storageLocation,
        expirationDate: initialExpirationDate || null 
      };
   

      await addProductToWarehouse(Number(destinationId), stockDto); 

    
      toast.success("Product created and initial stock added!");
      handleClose(); 
      onCreated();
      
    } catch (error) {
      console.error(error);
      toast.error("Failed to complete product setup (Creation or Stocking failed).");
    }
  };


  return (
    <Modal show={show} onHide={handleClose} centered>
      <Modal.Header closeButton>
        <Modal.Title>Create New Product</Modal.Title>
      </Modal.Header>

      <Modal.Body>
        <Form>
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