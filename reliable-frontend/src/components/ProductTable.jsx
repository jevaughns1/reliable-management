import React, { useState } from "react";
import { Table, Collapse, Card, Button } from "react-bootstrap";
import ProductModal from "./ProductModal";

/**
 * @file ProductTable.jsx
 * @author Jevaughn Stewart
 * @version 1.0
 */

/**
 * Reusable table component for displaying a list of products or warehouse inventory items.
 * Features include expandable rows for detailed information and optional 'Manage Stock' functionality
 * when operating in warehouse inventory mode.
 *
 * @param {object} props
 * @param {Array<object>} props.products - The list of items to display (can be bare ProductDTOs or WarehouseInventoryDTOs).
 * @param {Array<object>} [props.categories=[]] - The full list of categories for name lookup.
 * @param {boolean} [props.warehouseMode=false] - If true, the table shows quantity and allows stock management.
 * @param {number} [props.warehouseId] - The ID of the source warehouse, required for stock management/transfers.
 * @param {function} [props.onTransferSuccess] - Callback when an item is successfully transferred or deleted.
 * @returns {JSX.Element}
 */
export default function ProductTable({ products, categories = [], warehouseMode = false, warehouseId, onTransferSuccess }) {
  
  /** State to track the publicId of the currently open (expanded) row. */
  const [openRow, setOpenRow] = useState(null);
  
  /** State to control the visibility of the ProductModal. */
  const [showModal, setShowModal] = useState(false);
  
  /** State to hold the inventory item currently selected for modal management. */
  const [selectedItem, setSelectedItem] = useState(null);

  /**
   * Toggles the collapse state for a specific row.
   * @param {string} publicId - The public ID of the product row to toggle.
   */
  const toggleRow = (publicId) => {
    setOpenRow((prev) => (prev === publicId ? null : publicId));
  };

  /**
   * Opens the ProductModal for the given inventory item.
   * @param {object} item - The WarehouseInventoryDTO item.
   */
  const handleOpenModal = (item) => {
    setSelectedItem(item);
    setShowModal(true);
  };

  /**
   * Closes the ProductModal and resets the selected item and open row state.
   */
  const handleCloseModal = () => {
    
    setSelectedItem(null); 
    setShowModal(false);
    setOpenRow(null); // Collapse the row when modal closes
  };

  return (
    <>
      <div className="table-responsive">
        <Table striped hover bordered className="align-middle">
          <thead>
            <tr>
              <th>Name</th>
              <th>SKU</th>
              <th>Category</th>
              <th>Unit</th>
              <th>Price</th>
              {warehouseMode && <th>Quantity</th>}
              <th style={{ width: "50px" }}>Details</th>
            </tr>
          </thead>

          <tbody>
            {products.length > 0 ? (
              products.map((item) => {
                // Destructure relevant fields. Fields might be directly on 'item' (ProductPage) or nested under 'item.product' (Dashboard/Inventory Page)
                const { productPublicId, quantity, product, expirationDate, storageLocation } = item;

                // Lookup category name
                const categoryName =
                  categories.find((cat) => cat.id === product?.categoryId)?.name || "N/A";

                return (
                  <React.Fragment key={productPublicId}>
                    {/* Main Row */}
                    <tr
                      onClick={() => toggleRow(productPublicId)}
                      style={{ cursor: "pointer" }}
                      className={openRow === productPublicId ? "table-primary" : ""}
                    >
                      <td>{product?.name || "N/A"}</td>
                      <td>{product?.sku || "N/A"}</td>
                      <td>{categoryName}</td>
                      <td>{product?.unit || "N/A"}</td>
                      <td>${Number(product?.price || 0).toFixed(2)}</td>

                      {/* Quantity column visible only in warehouse mode */}
                      {warehouseMode && <td>{quantity ?? "—"}</td>}

                      <td>{openRow === productPublicId ? "▲" : "▼"}</td>
                    </tr>

                    {/* Collapsible Detail Row */}
                    <tr>
                      <td
                        colSpan={warehouseMode ? 7 : 6}
                        className="p-0 border-0"
                      >
                        <Collapse in={openRow === productPublicId}>
                          <div className="p-2">
                            <Card body className="bg-light p-3 border-0 rounded-0">
                              <div className="d-flex justify-content-between align-items-center">
                                {/* Details Content */}
                                <div className="row flex-grow-1 me-3">
                                  <div className="col-md-6">
                                    <p className="mb-1">
                                      <strong>Storage Location:</strong> {storageLocation || "N/A"}
                                    </p>
                                    <p className="mb-1">
                                      <strong>SKU:</strong> {product?.sku}
                                    </p>
                                  </div>

                                  <div className="col-md-6">
                                    {expirationDate && (
                                      <p className="mb-1">
                                        <strong>Exp. Date:</strong> {expirationDate}
                                      </p>
                                    )}
                                    <p className="mb-1">
                                      <strong>Description:</strong>{" "}
                                      {product?.description || "N/A"}
                                    </p>
                                  </div>
                                </div>

                                {/* Manage Stock Button (only visible in warehouse mode AND if warehouseId is passed) */}
                                {warehouseId && warehouseMode && ( // Added warehouseMode check for robustness
                                  <Button
                                    variant="primary"
                                    onClick={(e) => {
                                      e.stopPropagation();
                                      handleOpenModal(item);
                                    }}
                                  >
                                    Manage Stock
                                  </Button>
                                )}
                              </div>
                            </Card>
                          </div>
                        </Collapse>
                      </td>
                    </tr>
                  </React.Fragment>
                );
              })
            ) : (
              <tr>
                <td colSpan={warehouseMode ? 7 : 6} className="text-center py-4">
                  No products found.
                </td>
              </tr>
            )}
          </tbody>
        </Table>
      </div>


      {/* Product Management Modal */}
      {showModal && selectedItem && (
        <ProductModal
          show={showModal}
          onClose={handleCloseModal}
          warehouseId={warehouseId}
          item={selectedItem}    
          categories={categories}
          onTransferSuccess={onTransferSuccess}
        />
      )}
    </>
  );
}