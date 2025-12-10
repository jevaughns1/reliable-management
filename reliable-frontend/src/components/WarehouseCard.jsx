import { useState } from "react";
import ProductModal from "./ProductModal";

export default function WarehouseCard({ warehouse }) {
  const [selectedItem, setSelectedItem] = useState(null);

  return (
    <div className="card shadow-sm h-100">
        <div className="card-header">   <h5 className="card-title">{warehouse.name}</h5>   <p className="card-text text-muted">{warehouse.location}</p></div>
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
            <button
              key={item.productPublicId}
              className="list-group-item list-group-item-action d-flex justify-content-between"
              onClick={() => setSelectedItem(item)}
            >
              <span>{item.product.name}</span>
              <span className="badge bg-primary">{item.quantity}</span>
            </button>
          ))}
        </div>
      </div>

      {selectedItem && (
        <ProductModal
  item={selectedItem}
  warehouseId={warehouse.warehouseId}
  show={!!selectedItem}
  onClose={() => setSelectedItem(null)}
/>

      )}
    </div>
  );
}
