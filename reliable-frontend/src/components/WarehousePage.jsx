import { useState, useEffect } from "react";
import { Button } from "react-bootstrap";
import WarehouseCard from "../components/WarehouseCard";
import WarehouseFormModal from "../components/WarehouseFormModal"; 
import { getAllWarehouses } from "../api/warehouseApi";

export default function WarehousePage() {
  const [warehouses, setWarehouses] = useState([]);
  const [showCreateModal, setShowCreateModal] = useState(false); 

  const sortWarehouses = (data) => {
    return data.sort((a, b) => a.name.localeCompare(b.name));
  };

  const refreshWarehouses = async () => {
    const data = await getAllWarehouses();
    setWarehouses(sortWarehouses(data));
  };

  useEffect(() => {
    const load = async () => {
      const data = await getAllWarehouses();
      setWarehouses(sortWarehouses(data));
    };
    load();
  }, []); 

  const handleWarehouseSaved = () => {
      setShowCreateModal(false);
      refreshWarehouses(); 
  };

  return (
    <div className="container py-4">
      
      <h1 className="mb-4"> <strong> Warehouse Metrics</strong></h1>
      
      <div className="d-flex justify-content-end mb-3">
        <Button 
          variant="success" 
          onClick={() => setShowCreateModal(true)}
          className="me-3"
        >
          ➕ Create New Warehouse
        </Button>
        <Button 
          variant="outline-secondary" 
          onClick={refreshWarehouses}
        >
          ⟳ Refresh
        </Button>
      </div>

      <div className="row g-3 mt-4">

        {(warehouses ?? []).map((w) => (
         <div key={w.warehouseId} className="col-lg-6 col-md-6 col-sm-12">
            <WarehouseCard 
              warehouse={w}  
              onUpdated={refreshWarehouses} 
              onDeleted={refreshWarehouses} 
            />
          </div>
        ))}
      </div>
      
   
      {showCreateModal && (
        <WarehouseFormModal
          show={showCreateModal}
          onClose={() => setShowCreateModal(false)}
          warehouse={null} 
          onSaved={handleWarehouseSaved}
        />
      )}
  

    </div>
  );
}