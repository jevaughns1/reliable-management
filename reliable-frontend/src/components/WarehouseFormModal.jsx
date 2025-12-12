import { useState } from "react";
import { Modal, Button, Form } from "react-bootstrap";
// Note: Assuming 'updateWarehouse' is the PUT call for full updates
import { createWarehouse, updateWarehouse, deleteWarehouse } from "../api/warehouseApi";
import toast from "react-hot-toast";

/**
 * @file WarehouseFormModal.jsx
 * @author Jevaughn Stewart
 * @version 1.0
 */

/**
 * Modal component for creating a new warehouse or editing/deleting an existing one.
 * The mode is determined by the presence of the {@code warehouse} prop.
 *
 * @param {object} props
 * @param {boolean} props.show - Controls the visibility of the modal.
 * @param {function} props.onClose - Function to be called when the modal is closed.
 * @param {object} [props.warehouse] - The warehouse object to edit. If null/undefined, the modal is in Create mode.
 * @param {function} [props.onSaved] - Callback invoked after a successful creation or update (to trigger list refresh).
 * @param {function} [props.onDeleted] - Callback invoked after a successful deletion, receives the deleted warehouse ID.
 * @returns {JSX.Element}
 */
export default function WarehouseFormModal({
  show,
  onClose,
  warehouse,      
  onSaved,        
  onDeleted,    
}) {
  /** Flag to determine if the modal is in Edit mode (true) or Create mode (false). */
  const isEdit = !!warehouse;

  // --- State for Form Inputs ---
  
  /** State for the warehouse name. */
  const [name, setName] = useState(warehouse?.name || "");
  
  /** State for the warehouse location. */
  const [location, setLocation] = useState(warehouse?.location || "");
  
  /** State for the warehouse maximum capacity. */
  const [maxCapacity, setMaxCapacity] = useState(
    warehouse?.maxCapacity ?? ""
  );

  /**
   * Handles the creation (POST) or updating (PUT) of the warehouse data.
   * Performs basic validation before calling the appropriate API function.
   * @async
   */
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
        // Update existing warehouse
        await updateWarehouse(warehouse.warehouseId, payload);
        toast.success("Warehouse updated!");
      } else {
        // Create new warehouse
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

  /**
   * Handles the deletion of the warehouse (only available in Edit mode).
   * Requires user confirmation before proceeding.
   * @async
   */
  const handleDelete = async () => {
    if (!warehouse) return;
    if (!window.confirm(`Are you sure you want to delete warehouse "${warehouse.name}"? This action cannot be undone.`)) return;

    try {
      await deleteWarehouse(warehouse.warehouseId);
      toast.success("Warehouse deleted.");
      
      // Notify parent component of deletion, passing the ID
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

      <Modal.Footer className="d-flex justify-content-between">
        {isEdit ? (
          <Button variant="danger" onClick={handleDelete}>
            Delete
          </Button>
        ) : (
          // Placeholder div to maintain justification in create mode
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