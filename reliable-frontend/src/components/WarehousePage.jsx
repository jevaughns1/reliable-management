import { useState, useEffect } from "react";
import { Button } from "react-bootstrap";
import WarehouseCard from "../components/WarehouseCard";
import WarehouseFormModal from "../components/WarehouseFormModal"; 
import { getAllWarehouses } from "../api/warehouseApi";

/**
 * @file WarehousePage.jsx
 * @author Jevaughn Stewart
 * @version 1.0
 */

/**
 * Main page component for viewing and managing all warehouse entities.
 * It fetches and displays all warehouses using {@link WarehouseCard} components
 * and provides functionality to create new warehouses via {@link WarehouseFormModal}.
 *
 * @returns {JSX.Element}
 */
export default function WarehousePage() {
  
  /** State to store the list of all warehouses. */
  const [warehouses, setWarehouses] = useState([]);
  
  /** State to control the visibility of the modal for creating a new warehouse. */
  const [showCreateModal, setShowCreateModal] = useState(false); 

  /**
   * Helper function to sort warehouses alphabetically by name.
   * @param {Array<object>} data - The array of warehouse objects.
   * @returns {Array<object>} The sorted array.
   */
  const sortWarehouses = (data) => {
    // Assuming 'name' is the property to sort by
    return data.sort((a, b) => a.name.localeCompare(b.name));
  };

  /**
   * Fetches all warehouse data from the API, sorts it, and updates the state.
   * Used for initial load and refreshing the list.
   * @async
   */
  const refreshWarehouses = async () => {
    try {
        const data = await getAllWarehouses();
        setWarehouses(sortWarehouses(data));
    } catch (e) {
        console.error("Failed to refresh warehouses:", e);
    }
  };

  /**
   * useEffect hook runs once on component mount to load the initial list of warehouses.
   */
  useEffect(() => {
    const load = async () => {
      try {
          const data = await getAllWarehouses();
          setWarehouses(sortWarehouses(data));
      } catch (e) {
          console.error("Failed to load warehouses:", e);
      }
    };
    load();
  }, []); // Empty dependency array means this runs only once

  /**
   * Handler invoked after a warehouse is successfully created, updated, or deleted via the modal.
   * It hides the modal and triggers a full list refresh.
   */
  const handleWarehouseSaved = () => {
      setShowCreateModal(false);
      refreshWarehouses(); 
  };

  return (
    <div className="container py-4">
      
      <h1 className="mb-4"> <strong> Warehouse Metrics</strong></h1>
      
      {/* Action Buttons */}
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

      {/* Warehouse Cards Grid */}
      <div className="row g-3 mt-4">

        {(warehouses ?? []).map((w) => (
         <div key={w.warehouseId} className="col-lg-6 col-md-6 col-sm-12">
            <WarehouseCard 
              warehouse={w}  
              // Refresh is called if the card's modal updates or deletes the warehouse
              onUpdated={refreshWarehouses} 
              onDeleted={refreshWarehouses} 
            />
          </div>
        ))}
      </div>
      
   
      {/* Create Warehouse Modal */}
      {showCreateModal && (
        <WarehouseFormModal
          show={showCreateModal}
          onClose={() => setShowCreateModal(false)}
          warehouse={null} // null indicates Create mode
          onSaved={handleWarehouseSaved}
        />
      )}
  

    </div>
  );
}