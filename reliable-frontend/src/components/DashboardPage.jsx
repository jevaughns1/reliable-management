import { useState, useEffect } from "react";
import { getAllWarehouses } from "../api/warehouseApi";
import ProductsSection from "./ProductsSection";

import toast from "react-hot-toast";

export default function DashboardPage() {
  const [warehouses, setWarehouses] = useState([]);
  const [selectedWarehouse, setSelectedWarehouse] = useState(null);

  useEffect(() => {
    const loadWarehouses = async () => {
      try {
        const data = await getAllWarehouses();
        setWarehouses(data);
      } catch (error) {
        console.error("Failed to load warehouses:", error);
       toast.error("Failed to load warehouses");
      }
    };
    loadWarehouses();
  }, []);

  return (
    <>
        <div className="container-fluid py-4"> 
          <h1 className="mb-4">Dashboard</h1>

          <div className="card p-3 mb-4 shadow-sm">
            <label className="form-label fw-bold">Select Warehouse</label>
            <select
              className="form-select"
      
              value={selectedWarehouse || ""} 
              onChange={(e) => setSelectedWarehouse(Number(e.target.value))}
            >
              <option value="">-- Choose Warehouse --</option>

              {warehouses.map((w) => (
                <option key={w.warehouseId} value={w.warehouseId}>
                  {w.name} — {w.location}
                </option>
              ))}
            </select>
          </div>

    
          {selectedWarehouse && (
            <ProductsSection warehouseId={selectedWarehouse} />
          )}
        </div>
    </>
  );
}