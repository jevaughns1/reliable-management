import { useState } from "react";
import { Modal, Button, Form } from "react-bootstrap";
import {
  updateInventory,
  deleteInventory,
  transferInventory,
} from "../api/warehouseApi";

export default function ProductModal({ item, warehouseId, show, onClose }) {
  const [quantity, setQuantity] = useState(item.quantity);
  const [storageLocation, setStorageLocation] = useState(item.storageLocation || "");
  const [expirationDate, setExpirationDate] = useState(item.expirationDate || "");
  const [destinationId, setDestinationId] = useState("");

  const handleUpdate = async () => {
    await updateInventory(warehouseId, item.productPublicId, {
      quantity,
      storageLocation,
      expirationDate,
    });
    onClose();
  };

  const handleDelete = async () => {
    await deleteInventory(warehouseId, item.productPublicId);
    onClose();
  };

  const handleTransfer = async () => {
    await transferInventory({
      productPublicId: item.productPublicId,
      sourceWarehouseId: warehouseId,
      destinationWarehouseId: parseInt(destinationId),
      transferNotes: "Full quantity transfer",
    });
    onClose();
  };

  return (
    <Modal show={show} onHide={onClose} centered>
      <Modal.Header closeButton>
        <Modal.Title>{item.product.name}</Modal.Title>
      </Modal.Header>

      <Modal.Body>
        <Form>
          <Form.Group className="mb-3">
            <Form.Label>Quantity</Form.Label>
            <Form.Control
              type="number"
              value={quantity}
              onChange={(e) => setQuantity(Number(e.target.value))}
            />
          </Form.Group>

          <Form.Group className="mb-3">
            <Form.Label>Storage Location</Form.Label>
            <Form.Control
              value={storageLocation}
              onChange={(e) => setStorageLocation(e.target.value)}
            />
          </Form.Group>

          <Form.Group className="mb-3">
            <Form.Label>Expiration Date</Form.Label>
            <Form.Control
              type="date"
              value={expirationDate}
              onChange={(e) => setExpirationDate(e.target.value)}
            />
          </Form.Group>

          <div className="d-flex gap-2 mb-3">
            <Button variant="primary" className="w-50" onClick={handleUpdate}>
              Update
            </Button>
            <Button variant="danger" className="w-50" onClick={handleDelete}>
              Delete
            </Button>
          </div>

          <hr />

          <h5>Transfer Product</h5>
          <Form.Control
            className="mb-2"
            type="number"
            placeholder="Destination Warehouse ID"
            value={destinationId}
            onChange={(e) => setDestinationId(e.target.value)}
          />
          <Button variant="warning" className="w-100" onClick={handleTransfer}>
            Transfer
          </Button>
        </Form>
      </Modal.Body>

      <Modal.Footer>
        <Button variant="secondary" onClick={onClose}>Close</Button>
      </Modal.Footer>
    </Modal>
  );
}
