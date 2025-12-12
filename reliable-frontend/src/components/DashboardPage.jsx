import { useState, useEffect } from "react";
import { getAllWarehouses } from "../api/warehouseApi";
import ProductsSection from "./ProductsSection";

import toast from "react-hot-toast";

/**
 * @file DashboardPage.jsx
 * @author Jevaughn Stewart
 * @version 1.0
 */

/**
 * The main page component for the Inventory Control Hub.
 * It handles fetching the list of all available warehouses and manages the user's selection
 * to dynamically load the inventory details for the chosen warehouse via the {@link ProductsSection} component.
 *
 * @returns {JSX.Element}
 */
export default function DashboardPage() {
  
  /**
   * State to hold the list of all warehouses fetched from the API.
   * @type {[Array<object>, function]}
   */
  const [warehouses, setWarehouses] = useState([]);
  
  /**
   * State to hold the ID of the currently selected warehouse. Null initially.
   * @type {[number|null, function]}
   */
  const [selectedWarehouse, setSelectedWarehouse] = useState(null);

  /**
   * useEffect hook runs once on component mount.
   * Fetches all warehouses from the API and updates the state.
   */
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
  }, []); // Empty dependency array ensures it runs only once

  return (
    <>
        <div className="container-fluid py-4"> 
          <h1 className="mb-4"><strong>Inventory Control Hub</strong></h1>

          {/* Warehouse Selection Card */}
          <div className="card p-3 mb-4 shadow-sm">
            <label className="form-label fw-bold">Select Warehouse</label>
            <select
              className="form-select"
              // Use an empty string for the value when selectedWarehouse is null, 
              // to match the initial placeholder option's value.
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

          {/* Conditional Rendering of Inventory Section */}
          {selectedWarehouse && (
            <ProductsSection warehouseId={selectedWarehouse} />
          )}
        </div>
    </>
  );
}