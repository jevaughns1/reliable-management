import axios from "axios";

/**
 * @file warehouseApi.js
 * @description Centralized API client for all inventory, warehouse, product, 
 * and category management endpoints. It utilizes Axios for making HTTP requests 
 * to the Spring Boot backend API.
 * @author Jevaughn Stewart
 * @version 1.0
 */

/**
 * The base URL for the backend API.
 * @constant {string}
 */
const API_URL = "https://api.reliable.click";

/**
 * Configures an Axios instance with the base URL and default headers.
 */
const api = axios.create({
  baseURL: API_URL,
  headers: { "Content-Type": "application/json" }
});


/**
 * Retrieves a list of all warehouses.
 * Maps to GET /warehouses
 * @async
 * @returns {Promise<Array<object>>} List of all WarehouseDTOs.
 */
export const getAllWarehouses = async () => {
  const res = await api.get("/warehouses");
  return res.data;
};

/**
 * Creates a new warehouse record.
 * Maps to POST /warehouses
 * @async
 * @param {object} dto - The WarehouseCreationDTO payload.
 * @returns {Promise<object>} The newly created WarehouseDTO.
 */
export const createWarehouse = async(dto) => {
  const res = await api.post(`/warehouses`, dto);
  return res.data;
}

/**
 * Updates an entire warehouse record (PUT request).
 * Maps to PUT /warehouses/{id}
 * @async
 * @param {number|string} id - The ID of the warehouse to update.
 * @param {object} dto - The WarehouseUpdateDTO payload.
 * @returns {Promise<object>} The updated WarehouseDTO.
 */
export const updateWarehouse = async (id, dto) => {
  const res = await api.put(`/warehouses/${id}`, dto);
  return res.data;
};

/**
 * Partially updates a warehouse record (PATCH request).
 * Maps to PATCH /warehouses/{id}
 * @async
 * @param {number|string} id - The ID of the warehouse to patch.
 * @param {object} dto - The WarehousePatchDTO payload.
 * @returns {Promise<object>} The updated WarehouseDTO.
 */
export const patchWarehouse = async (id, dto) => {
  const res = await api.patch(`/warehouses/${id}`, dto);
  return res.data;
};

/**
 * Deletes a warehouse record (DELETE request).
 * Maps to DELETE /warehouses/{id}
 * @param {number|string} id - The ID of the warehouse to delete.
 * @returns {Promise<void>} Resolves on success (HTTP 204 No Content).
 */
export const deleteWarehouse = (id) => {
  return api.delete(`/warehouses/${id}`);
};


// --- PRODUCT MANAGEMENT (CATALOG) ---

/**
 * Retrieves a list of all product categories.
 * Maps to GET /api/categories
 * @async
 * @returns {Promise<Array<object>>} List of CategoryDTOs.
 */
export const getAllCategories = async () => {
  const res = await api.get("/api/categories");
  return res.data;
};

/**
 * Retrieves a list of all products in the catalog.
 * Maps to GET /api/warehouse/products
 * @async
 * @returns {Promise<Array<object>>} List of ProductDTOs.
 */
export const getAllProducts = async () => {
  const res = await api.get("/api/warehouse/products");
  return res.data;
};

/**
 * Creates a new product in the catalog.
 * Maps to POST /api/warehouse/products
 * @async
 * @param {object} productDto - The ProductCreationDTO payload.
 * @returns {Promise<object>} The newly created ProductDTO.
 */
export const createProduct = async (productDto) => {
  const res = await api.post("/api/warehouse/products", productDto);
  return res.data;
};

/**
 * Updates a product in the catalog.
 * Maps to PUT /api/warehouse/products/{id}
 * @param {number|string} id - The ID of the product to update.
 * @param {object} body - The ProductUpdateDTO payload.
 * @returns {Promise<object>} The updated ProductDTO.
 */
export const updateProduct = (id, body) =>
  api.put(`/api/warehouse/products/${id}`, body);


/**
 * Deletes a product from the catalog.
 * Maps to DELETE /api/warehouse/products/{id}
 * @param {number|string} id - The ID of the product to delete.
 * @returns {Promise<void>} Resolves on success (HTTP 204 No Content).
 */
export const deleteProduct = (id) =>
  api.delete(`/api/warehouse/products/${id}`); 


// --- INVENTORY / STOCK MANAGEMENT ---

/**
 * Retrieves inventory details across all warehouses.
 * Maps to GET /warehouses
 * *NOTE: The endpoint used here returns warehouses, not raw inventory. It may need to be corrected 
 * to a dedicated /inventory endpoint on the backend.*
 * @async
 * @returns {Promise<Array<object>>} List of all warehouses (containing inventory data).
 */
export const getAllInventory = async () => {
  // Check backend endpoint: this likely returns warehouses, not flat inventory list
  const res = await api.get("/warehouses"); 
  return res.data;
};

/**
 * Retrieves the inventory for a specific warehouse.
 * Maps to GET /warehouses/inventory/{warehouseId}
 * *NOTE: Based on the path, the correct API path is likely /warehouses/{warehouseId}/inventory.*
 * @async
 * @param {number|string} warehouseId - The ID of the warehouse.
 * @returns {Promise<Array<object>>} List of WarehouseInventoryDTOs for the warehouse.
 */
export const getInventoryByWarehouse = async (warehouseId) => {
  const res = await api.get(`/warehouses/inventory/${warehouseId}`);
  return res.data;
};

/**
 * Adds a new product stock to a specific warehouse inventory.
 * Maps to POST /warehouses/inventory/{warehouseId}
 * @async
 * @param {number|string} warehouseId - The ID of the target warehouse.
 * @param {object} dto - The InventoryAdditionDTO payload (quantity, location, etc.).
 * @returns {Promise<object>} The updated WarehouseInventoryDTO.
 */
export const addProductToWarehouse = async (warehouseId, dto) => {
  const res = await api.post(`/warehouses/inventory/${warehouseId}`, dto);
  return res.data;
};

/**
 * Updates the stock details (e.g., quantity, location) for a specific product in a warehouse.
 * Maps to PUT /warehouses/inventory/{warehouseId}/{productPublicId}
 * @async
 * @param {number|string} warehouseId - The ID of the warehouse.
 * @param {number|string} productPublicId - The ID of the product item.
 * @param {object} body - The InventoryUpdateDTO payload.
 * @returns {Promise<object>} The updated WarehouseInventoryDTO.
 */
export const updateInventory = async (warehouseId, productPublicId, body) => {
  const res = await api.put(
    `/warehouses/inventory/${warehouseId}/${productPublicId}`,
    body
  );
  return res.data;
};

/**
 * Removes (deletes) a specific product stock record from a warehouse inventory.
 * Maps to DELETE /warehouses/inventory/{warehouseId}/{productPublicId}
 * @async
 * @param {number|string} warehouseId - The ID of the warehouse.
 * @param {number|string} productPublicId - The ID of the product item to remove.
 * @returns {Promise<void>} Resolves on success (HTTP 204 No Content).
 */
export const deleteInventory = async (warehouseId, productPublicId) => {
  const res = await api.delete(
    `/warehouses/inventory/${warehouseId}/${productPublicId}`
  );
  return res.data;
};

/**
 * Initiates the transfer of stock between two warehouses.
 * Maps to POST /warehouses/inventory/transfer
 * @async
 * @param {object} transferDto - The InventoryTransferDTO payload.
 * @returns {Promise<object>} The HTTP response object.
 */
export const transferInventory = async (transferDto) => {
  return api.post("/warehouses/inventory/transfer", transferDto);
};

// --- EXPIRATION ALERTS ---

/**
 * Retrieves inventory items that will expire within the next N days.
 * Maps to GET /warehouses/inventory/alerts/expiring/{days}
 * @async
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
 * @async
 * @returns {Promise<Array<object>>} List of WarehouseInventoryDTOs.
 */
export const getExpiredInventory = async () => {
    const res = await api.get("/warehouses/inventory/alerts/expired");
    return res.data;
};