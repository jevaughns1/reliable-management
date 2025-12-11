import { useState } from "react";
import ProductModal from "./ProductModal";

export default function WarehouseInventoryTable({ inventory, warehouseId }) {
  const [selectedItem, setSelectedItem] = useState(null);

  return (
    <div className="card shadow-sm">
      <div className="table-responsive">
        <table className="table table-hover mb-0">
          <thead className="table-light">
            <tr>
              <th>Product</th>
              <th>Quantity</th>
              <th>Storage</th>
              <th>Expiration</th>
            </tr>
          </thead>

          <tbody>
            {inventory.map((item) => (
              <tr key={item.productPublicId} onClick={() => setSelectedItem(item)}>
                <td>{item.product.name}</td>
                <td>{item.quantity}</td>
                <td>{item.storageLocation || "-"}</td>
                <td>{item.expirationDate || "-"}</td>
              </tr>
            ))}

            {inventory.length === 0 && (
              <tr>
                <td colSpan="4" className="text-center py-4 text-muted">
                  No inventory in this warehouse
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

   
      {selectedItem && (
        <ProductModal
          item={selectedItem}
          warehouseId={warehouseId}
          show={true}
          onClose={() => setSelectedItem(null)}
        />
      )}
    </div>
  );
}
