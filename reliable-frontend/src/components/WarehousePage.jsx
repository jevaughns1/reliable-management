import { useState, useEffect } from "react";
import WarehouseCard from "../components/WarehouseCard";
import CreateWarehouseForm from "../components/CreateWarehouseForm";
import { getAllWarehouses } from "../api/warehouseApi";

export default function WarehousePage() {
  const [warehouses, setWarehouses] = useState([]);

  useEffect(() => {
    const load=async()=> {
      const data = await getAllWarehouses();
      setWarehouses(data);
    }
    load();
  }, []);
  const refreshWarehouses = async () => {
    const data = await getAllWarehouses();
    setWarehouses(data);
  };

  return (
    <div className="container py-4">

      <CreateWarehouseForm onCreated={refreshWarehouses} />

      <div className="row g-3 mt-4">
        {(warehouses ?? []).map((w, index) => (
          <div key={index} className="col-md-4 col-sm-6">
            <WarehouseCard warehouse={w} />
          </div>
        ))}
      </div>
    </div>
  );
}
