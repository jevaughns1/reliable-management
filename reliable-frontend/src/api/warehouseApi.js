import axios from "axios";

/**
 * Base URL for the Reliable API.
 * @type {string}
 */
const API_URL = "https://api.reliable.click";

/**
 * Configured Axios instance for making requests to the Reliable API.
 * Sets the base URL and standard headers.
 * @author Jevaughn Stewart
 * @version 1.0
 */
const api = axios.create({
  baseURL: API_URL,
  headers: { "Content-Type": "application/json" }
});

/**
 * Fetches all warehouse records.
 * Maps to GET /warehouses
 * @async
 * @returns {Promise<Array<object>>} A promise that resolves to a list of WarehouseDTOs.
 */
export const getAllWarehouses = async () => {
  const res = await api.get("/warehouses");
  return res.data;
};


/**
 * Fetches all inventory records across all warehouses.
 * NOTE: The path used here `/warehouses` appears to be incorrect for general inventory.
 * Assuming the intention was to fetch all inventory (e.g., from an endpoint like /warehouses/inventory).
 * If `/warehouses` is correct, it returns warehouses, not inventory.
 * Maps to GET /warehouses (Returns all warehouses, potentially incorrect for inventory).
 * @async
 * @returns {Promise<Array<object>>} A promise that resolves to a list of WarehouseDTOs or InventoryDTOs (depending on actual endpoint logic).
 */
export const getAllInventory = async () => {
  const res = await api.get("/warehouses"); 
  return res.data;
};

/**
 * Fetches all inventory records for a specific warehouse.
 * Maps to GET /warehouses/inventory/{warehouseId}
 * @async
 * @param {number} warehouseId - The ID of the warehouse.
 * @returns {Promise<object>} A promise that resolves to a WarehouseInventoryByWarehouseDTO.
 */
export const getInventoryByWarehouse = async (warehouseId) => {
  const res = await api.get(`/warehouses/inventory/${warehouseId}`);
  return res.data;
};

/**
 * Fully updates (PUT) a product record identified by its public ID.
 * Maps to PUT /api/warehouse/products/{id}
 * @param {string} id - The public ID (UUID) of the product to update.
 * @param {object} body - The ProductUpdateDTO payload.
 * @returns {Promise<object>} A promise that resolves to the updated ProductDTO.
 */
export const updateProduct = (id, body) =>
  api.put(`/api/warehouse/products/${id}`, body);


/**
 * Logically deletes (soft-delete) a product record identified by its public ID.
 * Maps to DELETE /api/warehouse/products/{id}
 * @param {string} id - The public ID (UUID) of the product to delete.
 * @returns {Promise<void>} A promise that resolves on successful deletion.
 */
export const deleteProduct = (id) =>
  // NOTE: Cleaned up the URL interpolation here to use the established `api` instance's baseURL
  api.delete(`/api/warehouse/products/${id}`); 


/**
 * Fetches all category records.
 * Maps to GET /api/categories
 * @async
 * @returns {Promise<Array<object>>} A promise that resolves to a list of CategoryDTOs.
 */
export const getAllCategories = async () => {
  const res = await api.get("/api/categories");
  return res.data;
};

/**
 * Fetches all active product records.
 * Maps to GET /api/warehouse/products
 * @async
 * @returns {Promise<Array<object>>} A promise that resolves to a list of ProductDTOs.
 */
export const getAllProducts = async () => {
  const res = await api.get("/api/warehouse/products");
  return res.data;
};


/**
 * Adds a new product inventory record to a specified warehouse.
 * Maps to POST /warehouses/inventory/{warehouseId}
 * @async
 * @param {number} warehouseId - The ID of the target warehouse.
 * @param {object} dto - The WarehouseInventoryCreateDTO payload.
 * @returns {Promise<object>} A promise that resolves to the created WarehouseInventoryDTO.
 */
export const addProductToWarehouse = async (warehouseId, dto) => {
  const res = await api.post(`/warehouses/inventory/${warehouseId}`, dto);
  return res.data;
};

/**
 * Updates an inventory record (quantity, expiration, location) for a specific product in a specific warehouse.
 * Maps to PUT /warehouses/inventory/{warehouseId}/{productPublicId}
 * @async
 * @param {number} warehouseId - The ID of the warehouse.
 * @param {string} productPublicId - The public ID of the product.
 * @param {object} body - The WarehouseInventoryUpdateDTO payload.
 * @returns {Promise<object>} A promise that resolves to the updated WarehouseInventoryDTO.
 */
export const updateInventory = async (warehouseId, productPublicId, body) => {
  const res = await api.put(
    `/warehouses/inventory/${warehouseId}/${productPublicId}`,
    body
  );
  return res.data;
};


/**
 * Deletes a product's entire inventory record from a specific warehouse.
 * Maps to DELETE /warehouses/inventory/{warehouseId}/{productPublicId}
 * @async
 * @param {number} warehouseId - The ID of the warehouse.
 * @param {string} productPublicId - The public ID of the product.
 * @returns {Promise<void>} A promise that resolves on successful deletion.
 */
export const deleteInventory = async (warehouseId, productPublicId) => {
  const res = await api.delete(
    `/warehouses/inventory/${warehouseId}/${productPublicId}`
  );
  return res.data;
};

/**
 * Initiates an inventory transfer from a source to a destination warehouse.
 * Maps to POST /warehouses/inventory/transfer
 * @async
 * @param {object} transferDto - The InventoryTransferDTO payload.
 * @returns {Promise<object>} A promise that resolves to the Axios response for the transfer.
 */
export const transferInventory = async (transferDto) => {
  return api.post("/warehouses/inventory/transfer", transferDto);
};


/**
 * Creates a new warehouse record.
 * Maps to POST /warehouses
 * @async
 * @param {object} dto - The WarehouseCreateDTO payload.
 * @returns {Promise<object>} A promise that resolves to the created WarehouseDTO.
 */
export const createWarehouse =async(dto)=>{
  const res = await api.post(`/warehouses`, dto);
  return res.data;
}

/**
 * Creates a new product record.
 * Maps to POST /api/warehouse/products
 * @async
 * @param {object} productDto - The ProductDTO payload.
 * @returns {Promise<object>} A promise that resolves to the created ProductDTO.
 */
export const createProduct = async (productDto) => {
  const res = await api.post("/api/warehouse/products", productDto);
  return res.data;
};



/**
 * Updates an entire warehouse record (PUT request).
 * Requires all non-null fields defined in WarehouseUpdateDTO.
 * Maps to PUT /warehouses/{id}
 * @async
 * @param {number} id - The ID of the warehouse to update.
 * @param {object} dto - The WarehouseUpdateDTO payload.
 * @returns {Promise<object>} A promise that resolves to the updated WarehouseDTO.
 */
export const updateWarehouse = async (id, dto) => {
  const res = await api.put(`/warehouses/${id}`, dto);
  return res.data;
};

/**
 * Partially updates a warehouse record (PATCH request).
 * Only sends fields that need updating (WarehousePatchDTO).
 * Maps to PATCH /warehouses/{id}
 * @async
 * @param {number} id - The ID of the warehouse to patch.
 * @param {object} dto - The WarehousePatchDTO payload.
 * @returns {Promise<object>} A promise that resolves to the updated WarehouseDTO.
 */
export const patchWarehouse = async (id, dto) => {
  const res = await api.patch(`/warehouses/${id}`, dto);
  return res.data;
};

/**
 * Deletes a warehouse record (DELETE request).
 * Maps to DELETE /warehouses/{id}
 * @param {number} id - The ID of the warehouse to delete.
 * @returns {Promise<void>} A promise that resolves on success (HTTP 204 No Content).
 */
export const deleteWarehouse = (id) => {
  return api.delete(`/warehouses/${id}`);
};


/**
 * Retrieves inventory items that will expire within the next N days.
 * Maps to GET /warehouses/inventory/alerts/expiring/{days}
 * @async
 * @param {number} days - The lookahead window (e.g., 30 days).
 * @returns {Promise<Array<object>>} A promise that resolves to a list of WarehouseInventoryDTOs.
 */
export const getNearingExpirationAlerts = async (days) => {
    const res = await api.get(`/warehouses/inventory/alerts/expiring/${days}`);
    return res.data;
};

/**
 * Retrieves inventory items that have already expired.
 * Maps to GET /warehouses/inventory/alerts/expired
 * @async
 * @returns {Promise<Array<object>>} A promise that resolves to a list of WarehouseInventoryDTOs.
 */
export const getExpiredInventory = async () => {
    const res = await api.get("/warehouses/inventory/alerts/expired");
    return res.data;
};