import { useState, useEffect } from "react";
import { Modal, Button, Form } from "react-bootstrap";
import toast from "react-hot-toast";

import { createProduct, getAllCategories } from "../api/warehouseApi";

export default function CreateProductModal({ show, onClose, onCreated }) {
  const [categories, setCategories] = useState([]);

  const [name, setName] = useState("");
  const [sku, setSku] = useState("");
  const [description, setDescription] = useState("");
  const [categoryId, setCategoryId] = useState("");
  const [unit, setUnit] = useState("");
  const [price, setPrice] = useState("");
  const [isHazardous, setIsHazardous] = useState(false);
  const [expirationRequired, setExpirationRequired] = useState(false);

  // Load categories ONLY when modal opens
  useEffect(() => {
    async function loadCategories() {
      try {
        const data = await getAllCategories();
        setCategories(data);
      } catch (err) {
        console.error(err);
        toast.error("Failed to load categories");
      }
    }

    if (show) loadCategories();
  }, [show]);

  const handleCreate = async () => {
    try {
      await createProduct({
        name,
        sku,
        description,
        categoryId: Number(categoryId),
        unit,
        price: Number(price),
        isHazardous,
        expirationRequired,
      });

      toast.success("Product created!");
      onClose();
      onCreated();
    } catch (error) {
      console.error(error);
      toast.error("Failed to create product");
    }
  };

  return (
    <Modal show={show} onHide={onClose} centered>
      <Modal.Header closeButton>
        <Modal.Title>Create New Product</Modal.Title>
      </Modal.Header>

      <Modal.Body>
        <Form>
          {/* Name */}
          <Form.Group className="mb-2">
            <Form.Label>Name</Form.Label>
            <Form.Control value={name} onChange={(e) => setName(e.target.value)} />
          </Form.Group>

          {/* SKU */}
          <Form.Group className="mb-2">
            <Form.Label>SKU</Form.Label>
            <Form.Control value={sku} onChange={(e) => setSku(e.target.value)} />
          </Form.Group>

          {/* Description */}
          <Form.Group className="mb-2">
            <Form.Label>Description</Form.Label>
            <Form.Control
              value={description}
              onChange={(e) => setDescription(e.target.value)}
            />
          </Form.Group>

          {/* Category Dropdown */}
          <Form.Group className="mb-2">
            <Form.Label>Category</Form.Label>
            <Form.Select
              value={categoryId}
              onChange={(e) => setCategoryId(e.target.value)}
            >
              <option value="">Select a category</option>
              {categories.map((c) => (
                <option key={c.id} value={c.id}>
                  {c.name}
                </option>
              ))}
            </Form.Select>
          </Form.Group>

          {/* Unit */}
          <Form.Group className="mb-2">
            <Form.Label>Unit</Form.Label>
            <Form.Control value={unit} onChange={(e) => setUnit(e.target.value)} />
          </Form.Group>

          {/* Price */}
          <Form.Group className="mb-2">
            <Form.Label>Price</Form.Label>
            <Form.Control
              type="number"
              value={price}
              onChange={(e) => setPrice(e.target.value)}
            />
          </Form.Group>

          {/* Hazardous */}
          <Form.Group className="form-check mt-2">
            <Form.Check
              label="Hazardous Material"
              checked={isHazardous}
              onChange={(e) => setIsHazardous(e.target.checked)}
            />
          </Form.Group>

          {/* Requires Expiration */}
          <Form.Group className="form-check mt-2">
            <Form.Check
              label="Requires Expiration Date"
              checked={expirationRequired}
              onChange={(e) => setExpirationRequired(e.target.checked)}
            />
          </Form.Group>

          <Button
            variant="primary"
            className="w-100 mt-3"
            onClick={handleCreate}
          >
            Create Product
          </Button>
        </Form>
      </Modal.Body>

      <Modal.Footer>
        <Button variant="secondary" onClick={onClose}>
          Close
        </Button>
      </Modal.Footer>
    </Modal>
  );
}
