import axios from "axios";

const API_URL = "http://localhost:8282";

const api = axios.create({
  baseURL: API_URL,
  headers: { "Content-Type": "application/json" }
});



export const getAllWarehouses = async () => {
  const res = await api.get("/warehouses");
  return res.data;
};



export const getAllInventory = async () => {
  const res = await api.get("/warehouses");
  return res.data;
};

export const getInventoryByWarehouse = async (warehouseId) => {
  const res = await api.get(`/warehouses/inventory/${warehouseId}`);
  return res.data;
};

export const addProductToWarehouse = async (warehouseId, dto) => {
  const res = await api.post(`/warehouses/inventory/${warehouseId}`, dto);
  return res.data;
};

export const updateInventory = async (warehouseId, productPublicId, body) => {
  const res = await api.put(
    `/warehouses/inventory/${warehouseId}/${productPublicId}`,
    body
  );
  return res.data;
};

export const deleteInventory = async (warehouseId, productPublicId) => {
  const res = await api.delete(
    `/warehouses/inventory/${warehouseId}/${productPublicId}`
  );
  return res.data;
};

export const transferInventory = async (transferDto) => {
  return api.post("/warehouses/inventory/transfer", transferDto);
};


export const getAllProducts = async () => {
  const res = await api.get("/api/warehouse/products");
  return res.data;
};
