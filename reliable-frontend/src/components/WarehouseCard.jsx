import { useState } from "react";

import WarehouseFormModal from "./WarehouseFormModal";


export default function WarehouseCard({ warehouse, onUpdated, onDeleted }) { 
  
  const [showForm, setShowForm] = useState(false);
  const handleWarehouseSaved = () => {
    setShowForm(false);
    if (onUpdated) onUpdated();
  };
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

      {showForm && (
        <WarehouseFormModal
          show={showForm}
          onClose={() => setShowForm(false)}
          warehouse={warehouse} // Pass the warehouse object for editing
          onSaved={handleWarehouseSaved}
          onDeleted={handleWarehouseDeleted}
        />
      )}

    </div>
  );
}