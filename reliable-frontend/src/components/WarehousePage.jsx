import { useState, useEffect } from "react";
import WarehouseCard from "../components/WarehouseCard";
import { getAllWarehouses } from "../api/warehouseApi";

export default function WarehousePage() {
  const [warehouses, setWarehouses] = useState([]);

  useEffect(() => {
    async function load() {
      const data = await getAllWarehouses();
      setWarehouses(data);
      console.log(data);
    }
    load();
  }, []);

  return (
    <div className="container mt-4">
      <h1 className="text-center mb-4">Warehouses</h1>

      <div className="row g-4">
        {(warehouses ?? []).map((warehouse,index) => (
          <div key={index} className="col-md-6 col-lg-4">
            <WarehouseCard warehouse={warehouse} />
          </div>
        ))}
      </div>
    </div>
  );
}
