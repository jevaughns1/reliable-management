import { useState } from "react";
import { Modal, Button, Form } from "react-bootstrap";
// Note: Assuming 'updateWarehouse' is the PUT call for full updates
import { createWarehouse, updateWarehouse, deleteWarehouse } from "../api/warehouseApi";
import toast from "react-hot-toast";

export default function WarehouseFormModal({
  show,
  onClose,
  warehouse,      
  onSaved,        
  onDeleted,    
}) {
  const isEdit = !!warehouse;


  const [name, setName] = useState(warehouse?.name || "");
  const [location, setLocation] = useState(warehouse?.location || "");
  const [maxCapacity, setMaxCapacity] = useState(
    warehouse?.maxCapacity ?? ""
  );

  const handleSave = async () => {

    if (!name || !location || !maxCapacity) {
        toast.error("All fields are required.");
        return;
    }

    const payload = {
        name,
        location,
        maxCapacity: Number(maxCapacity),
    };

    try {
      if (isEdit) {
     
        await updateWarehouse(warehouse.warehouseId, payload);
        toast.success("Warehouse updated!");
      } else {
        await createWarehouse(payload);
        toast.success("Warehouse created!");
      }

      if (onSaved) onSaved();
      onClose();
    } catch (err) {
      console.error(err);
      const errorMessage = err.response?.data?.message || "Failed to save warehouse.";
      toast.error(errorMessage);
    }
  };

  const handleDelete = async () => {
    if (!warehouse) return;
    if (!window.confirm(`Are you sure you want to delete warehouse "${warehouse.name}"?`)) return;

    try {
      await deleteWarehouse(warehouse.warehouseId);
      toast.success("Warehouse deleted.");
      if (onDeleted) onDeleted(warehouse.warehouseId); 
      onClose();
    } catch (err) {
      console.error(err);
      const errorMessage = err.response?.data?.message || "Failed to delete warehouse.";
      toast.error(errorMessage);
    }
  };

  return (
    <Modal show={show} onHide={onClose} centered>
      <Modal.Header closeButton>
        <Modal.Title>
          {isEdit ? "Edit Warehouse" : "Create Warehouse"}
        </Modal.Title>
      </Modal.Header>

      <Modal.Body>
        <Form>
          <Form.Group className="mb-3">
            <Form.Label>Name</Form.Label>
            <Form.Control
              value={name}
              onChange={(e) => setName(e.target.value)}
              required
              placeholder="Warehouse name"
            />
          </Form.Group>

          <Form.Group className="mb-3">
            <Form.Label>Location</Form.Label>
            <Form.Control
              value={location}
              onChange={(e) => setLocation(e.target.value)}
              required
              placeholder="Warehouse location"
            />
          </Form.Group>

          <Form.Group className="mb-3">
            <Form.Label>Max Capacity</Form.Label>
            <Form.Control
              type="number"
              value={maxCapacity}
              onChange={(e) => setMaxCapacity(e.target.value)}
              required
              min="1" // Capacity must be positive
            />
          </Form.Group>
        </Form>
      </Modal.Body>

      {/* Corrected Modal.Footer class and structure */}
      <Modal.Footer className="d-flex justify-content-between">
        {isEdit ? (
          <Button variant="danger" onClick={handleDelete}>
            Delete
          </Button>
        ) : (
          // Use an empty element to keep space consistency when not in edit mode
          <div /> 
        )}

        <div>
          <Button variant="secondary" className="me-2" onClick={onClose}>
            Cancel
          </Button>
          <Button variant="primary" onClick={handleSave}>
            {isEdit ? "Save Changes" : "Create"}
          </Button>
        </div>
      </Modal.Footer>
    </Modal>
  );
}