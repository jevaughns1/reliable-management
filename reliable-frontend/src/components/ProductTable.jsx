import React, { useState } from "react";
import { Table, Collapse, Card, Button } from "react-bootstrap";
import ProductModal from "./ProductModal";

export default function ProductTable({ products, categories = [], warehouseMode = false, warehouseId, onTransferSuccess }) {
  const [openRow, setOpenRow] = useState(null);
  const [showModal, setShowModal] = useState(false);
  const [selectedItem, setSelectedItem] = useState(null);

  const toggleRow = (publicId) => {
    setOpenRow((prev) => (prev === publicId ? null : publicId));
  };

  const handleOpenModal = (item) => {
    setSelectedItem(item);
    setShowModal(true);
  };

  const handleCloseModal = () => {
    
    setSelectedItem(null); 
    setShowModal(false);
    setOpenRow(null); 
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
                const { productPublicId, quantity, product, expirationDate,storageLocation } = item;

                const categoryName =
                  categories.find((cat) => cat.id === product?.categoryId)?.name || "N/A";

                return (
                  <React.Fragment key={productPublicId}>
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

                      {warehouseMode && <td>{quantity ?? "—"}</td>}

                      <td>{openRow === productPublicId ? "▲" : "▼"}</td>
                    </tr>

               
                    <tr>
                      <td
                        colSpan={warehouseMode ? 7 : 6}
                        className="p-0 border-0"
                      >
                        <Collapse in={openRow === productPublicId}>
                          <div className="p-2">
                            <Card body className="bg-light p-3 border-0 rounded-0">
                              <div className="d-flex justify-content-between align-items-center">
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

                            
                            
                                {warehouseId && ( 
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