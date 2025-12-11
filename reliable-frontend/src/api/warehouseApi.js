import axios from "axios";

const API_URL = "http://3.141.170.4:8080";

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
export const updateProduct = (id, body) =>
  api.put(`/api/warehouse/products/${id}`, body);


export const deleteProduct = (id) =>
  api.delete(`${API_URL}/api/warehouse/products/${id}`);


export const getAllCategories = async () => {
  const res = await api.get("/api/categories");
  return res.data;
};

export const getAllProducts = async () => {
  const res = await api.get("/api/warehouse/products");
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


export const createWarehouse =async(dto)=>{
  const res = await api.post(`/warehouses`, dto);
  return res.data;
}
export const createProduct = async (productDto) => {
  const res = await api.post("/api/warehouse/products", productDto);
  return res.data;
};
