import { useState } from "react";

import WarehouseFormModal from "./WarehouseFormModal";

/**
 * @file WarehouseCard.jsx
 * @author Jevaughn Stewart
 * @version 1.0
 */

/**
 * Component representing a single card view of a warehouse, including its capacity,
 * location, and a summary of its current inventory.
 * Provides an 'Edit' button to open the {@link WarehouseFormModal} for modification or deletion.
 *
 * @param {object} props
 * @param {object} props.warehouse - The warehouse data object (including inventory summary).
 * @param {function} [props.onUpdated] - Callback function invoked when the warehouse data is successfully updated.
 * @param {function} [props.onDeleted] - Callback function invoked when the warehouse is successfully deleted, receives the deleted ID.
 * @returns {JSX.Element}
 */
export default function WarehouseCard({ warehouse, onUpdated, onDeleted }) { 
  
  /** State to control the visibility of the {@link WarehouseFormModal} for editing. */
  const [showForm, setShowForm] = useState(false);
  
  /**
   * Closes the modal and triggers the parent's update handler.
   */
  const handleWarehouseSaved = () => {
    setShowForm(false);
    if (onUpdated) onUpdated();
  };
  
  /**
   * Closes the modal and triggers the parent's delete handler.
   * @param {number} id - The ID of the warehouse that was deleted.
   */
  const handleWarehouseDeleted = (id) => {
    setShowForm(false);
    if (onDeleted) onDeleted(id);
  };

  return (
    <div className="card shadow-sm h-100">
      <div className="card-header d-flex justify-content-between align-items-start">
        <div>
          <h5 className="card-title mb-1">{warehouse.name}</h5>
          <p className="card-subtitle text-muted small">
            {warehouse.location}
          </p>
        </div>
        <div className="d-flex flex-column align-items-end">
          <button
            className="btn btn-sm btn-outline-primary mb-1"
            onClick={() => setShowForm(true)}
          >
            Edit
          </button>
        </div>

      </div>
      <div className="card-body">

        <p>
          Capacity:{" "}
          <strong>
            {warehouse.currentCapacity} / {warehouse.maxCapacity}
          </strong>
        </p>

        <hr />

        <h6>Inventory</h6>

        <div className="list-group overflow-auto" style={{ maxHeight: "180px" }}>
          {/* Renders a summary of products and their total quantity in this warehouse */}
          {warehouse.inventory.map((item) => (
            <div
              key={item.product.publicId}
              className="list-group-item list-group-item-action d-flex justify-content-between"
            >
              <span>{item.product.name}</span>
              <span className="badge bg-primary">{item.quantity}</span>
            </div>
          ))}
        </div>

      </div>

      {/* Warehouse Edit/Create Modal */}
      {showForm && (
        <WarehouseFormModal
          show={showForm}
          onClose={() => setShowForm(false)}
          warehouse={warehouse} // Pass the existing warehouse object for editing
          onSaved={handleWarehouseSaved}
          onDeleted={handleWarehouseDeleted}
        />
      )}

    </div>
  );
}