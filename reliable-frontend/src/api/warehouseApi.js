import axios from "axios";

const API_URL = "https://api.reliable.click";

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
  // NOTE: Cleaned up the URL interpolation here to use the established `api` instance's baseURL
  api.delete(`/api/warehouse/products/${id}`); 


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



/**
 * Updates an entire warehouse record (PUT request).
 * Requires all non-null fields defined in WarehouseUpdateDTO.
 * @param {number} id - The ID of the warehouse to update.
 * @param {object} dto - The WarehouseUpdateDTO payload.
 * @returns {Promise<object>} The updated WarehouseDTO.
 */
export const updateWarehouse = async (id, dto) => {
  const res = await api.put(`/warehouses/${id}`, dto);
  return res.data;
};

/**
 * Partially updates a warehouse record (PATCH request).
 * Only sends fields that need updating (WarehousePatchDTO).
 * @param {number} id - The ID of the warehouse to patch.
 * @param {object} dto - The WarehousePatchDTO payload.
 * @returns {Promise<object>} The updated WarehouseDTO.
 */
export const patchWarehouse = async (id, dto) => {
  const res = await api.patch(`/warehouses/${id}`, dto);
  return res.data;
};

/**
 * Deletes a warehouse record (DELETE request).
 * @param {number} id - The ID of the warehouse to delete.
 * @returns {Promise<void>} Resolves on success (HTTP 204 No Content).
 */
export const deleteWarehouse = (id) => {
  return api.delete(`/warehouses/${id}`);
};


/**
 * Retrieves inventory items that will expire within the next N days.
 * Maps to GET /warehouses/inventory/alerts/expiring/{days}
 * @param {number} days - The lookahead window (e.g., 30 days).
 * @returns {Promise<Array<object>>} List of WarehouseInventoryDTOs.
 */
export const getNearingExpirationAlerts = async (days) => {
    const res = await api.get(`/warehouses/inventory/alerts/expiring/${days}`);
    return res.data;
};

/**
 * Retrieves inventory items that have already expired.
 * Maps to GET /warehouses/inventory/alerts/expired
 * @returns {Promise<Array<object>>} List of WarehouseInventoryDTOs.
 */
export const getExpiredInventory = async () => {
    const res = await api.get("/warehouses/inventory/alerts/expired");
    return res.data;
};