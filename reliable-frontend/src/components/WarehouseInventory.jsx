import { useState } from "react";
import ProductModal from "./ProductModal";

/**
 * @file WarehouseInventory.jsx
 * @author Jevaughn Stewart
 * @version 1.0
 */

/**
 * Component that displays the detailed inventory list for a specific warehouse
 * and allows the user to click on an inventory item to open the {@link ProductModal}
 * for viewing/managing that specific stock.
 *
 * @param {object} props
 * @param {object} props.warehouse - The warehouse data object, expected to include an {@code inventory} array.
 * @returns {JSX.Element}
 */
export default function WarehouseInventory({ warehouse }) {
  
  /** State to hold the WarehouseInventoryDTO item selected by the user, triggering the modal display. */
  const [selectedItem, setSelectedItem] = useState(null);

  return (
    <div>
      <h3>{warehouse.name} Inventory</h3>

      <div className="list-group mt-3">
        {warehouse.inventory.map((item) => (
          <button
            key={item.productPublicId}
            className="list-group-item list-group-item-action d-flex justify-content-between"
            // Set the selected item to open the modal
            onClick={() => setSelectedItem(item)}
          >
            <span>{item.product.name}</span>
            <span className="badge bg-primary">{item.quantity}</span>
          </button>
        ))}
      </div>

      {/* Render ProductModal when an item is selected */}
      {selectedItem && (
        <ProductModal
          item={selectedItem}
          show={true}
          // Note: Assuming 'warehouse.id' is available for the warehouse ID
          warehouseId={warehouse.id} 
          // Close handler resets the selected item
          onClose={() => setSelectedItem(null)}
          // Placeholder for categories and onTransferSuccess, which are likely required by ProductModal
          // categories={[]} 
          // onTransferSuccess={() => {}} 
        />
      )}
    </div>
  );
}