import { useState } from "react";
import { createWarehouse } from "../api/warehouseApi";
import toast from "react-hot-toast";

export default function CreateWarehouseForm({ onCreated }) {
  const [name, setName] = useState("");
  const [location, setLocation] = useState("");
  const [maxCapacity, setMaxCapacity] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      await createWarehouse({
        name,
        location,
        maxCapacity: Number(maxCapacity),
      });

      toast.success("Warehouse created!");

      setName("");
      setLocation("");
      setMaxCapacity("");

      if (onCreated) onCreated();

    } catch (err) {
      toast.error("Failed to create warehouse.");
      console.error(err);
    }
  };

  return (
    <form className="card p-3 mb-4" onSubmit={handleSubmit}>
      <h4>Create Warehouse</h4>

      <div className="mb-2">
        <label className="form-label">Name</label>
        <input
          className="form-control"
          value={name}
          onChange={(e) => setName(e.target.value)}
          required
        />
      </div>

      <div className="mb-2">
        <label className="form-label">Location</label>
        <input
          className="form-control"
          value={location}
          onChange={(e) => setLocation(e.target.value)}
          required
        />
      </div>

      <div className="mb-2">
        <label className="form-label">Max Capacity</label>
        <input
          className="form-control"
          type="number"
          value={maxCapacity}
          onChange={(e) => setMaxCapacity(e.target.value)}
          required
        />
      </div>

      <button className="btn btn-success mt-2">Create Warehouse</button>
    </form>
  );
}
