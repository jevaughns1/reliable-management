import { useState } from "react";
import ProductModal from "./ProductModal";

/**
 * @file WarehouseInventoryTable.jsx
 * @author Jevaughn Stewart
 * @version 1.0
 */

/**
 * Component that displays the detailed inventory list for a specific warehouse
 * in a responsive table format. Clicking a row opens the {@link ProductModal}
 * for viewing/managing that specific stock item.
 *
 * @param {object} props
 * @param {Array<object>} props.inventory - The list of WarehouseInventoryDTO items to display.
 * @param {number|string} props.warehouseId - The ID of the source warehouse, passed to the modal for stock management.
 * @returns {JSX.Element}
 */
export default function WarehouseInventoryTable({ inventory, warehouseId }) {
  
  /** State to hold the WarehouseInventoryDTO item selected by the user, triggering the modal display. */
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
              <tr 
                key={item.productPublicId} 
                onClick={() => setSelectedItem(item)}
                style={{ cursor: "pointer" }}
              >
                <td>{item.product.name}</td>
                <td>{item.quantity}</td>
                <td>{item.storageLocation || "-"}</td>
                <td>{item.expirationDate || "-"}</td>
              </tr>
            ))}

            {/* Empty state row */}
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

   
      {/* Product Management Modal - Ensure categories and onTransferSuccess are handled by the parent if needed */}
      {selectedItem && (
        <ProductModal
          item={selectedItem}
          warehouseId={warehouseId}
          show={true}
          onClose={() => setSelectedItem(null)}
          // Placeholder for categories and onTransferSuccess, which are likely required by ProductModal
          // categories={[]} 
          // onTransferSuccess={() => {}} 
        />
      )}
    </div>
  );
}