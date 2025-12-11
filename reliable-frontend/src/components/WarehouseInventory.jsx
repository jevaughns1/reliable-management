import { useState } from "react";
import ProductModal from "./ProductModal";

export default function WarehouseInventory({ warehouse }) {
  const [selectedItem, setSelectedItem] = useState(null);

  return (
    <div>
      <h3>{warehouse.name} Inventory</h3>

      <div className="list-group mt-3">
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

      {selectedItem && (
        <ProductModal
          item={selectedItem}
          show={true}
          warehouseId={warehouse.id}
          onClose={() => setSelectedItem(null)}
        />
      )}
    </div>
  );
}
