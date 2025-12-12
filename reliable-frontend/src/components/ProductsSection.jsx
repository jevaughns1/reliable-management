import { useState, useEffect } from "react";
import CreateProductModal from "../components/CreateProductModal";
import ProductTable from "../components/ProductTable";
import { getInventoryByWarehouse, getAllCategories } from "../api/warehouseApi";

/**
 * @file ProductsSection.jsx
 * @author Jevaughn Stewart
 * @version 1.0
 */

/**
 * Section component responsible for displaying the inventory list (products and their quantities)
 * for a single, currently selected warehouse. It handles data fetching, filtering, sorting,
 * and refreshing the list when inventory changes (creation, transfer, deletion).
 *
 * @param {object} props
 * @param {number|string} props.warehouseId - The ID of the warehouse whose inventory should be loaded.
 * @returns {JSX.Element}
 */
export default function ProductsSection({ warehouseId }) {
  
  /** State for controlling the visibility of the CreateProductModal. */
  const [showCreate, setShowCreate] = useState(false);
  
  /** State for storing the list of inventory items (WarehouseInventoryDTOs). */
  const [products, setProducts] = useState([]);
  
  /** State for storing the fetched list of all categories. */
  const [categories, setCategories] = useState([]);

  /** State for the text input used for search filtering. */
  const [search, setSearch] = useState("");
  
  /** State for the selected category ID filter ('all' by default). */
  const [categoryFilter, setCategoryFilter] = useState("all");
  
  /** State for the field to sort by (e.g., product 'name', 'sku', or 'price'). */
  const [sortBy, setSortBy] = useState("name");
  
  /** State for the sort direction ('asc' or 'desc'). */
  const [sortDir, setSortDir] = useState("asc");

  /** State for storing the selected warehouse's details (ID, name, location). */
  const [warehouse, setWarehouse] = useState({
    warehouseId: "",
    warehouseName: "",
    warehouseLocation: ""
  });

 
  /**
   * useEffect hook runs once on component mount to load all product categories.
   */
  useEffect(() => {
    const load = async () => {
      try {
        const data = await getAllCategories();
        setCategories(data);
      } catch (e) {
        console.error("Failed to load categories:", e);
      }
    };
    load();
  }, []);

  /**
   * useEffect hook runs when {@code warehouseId} changes.
   * Fetches the new warehouse's inventory and details using {@link getInventoryByWarehouse}.
   */
  useEffect(() => {
    const load = async () => {
      if (!warehouseId) {
        setProducts([]);
        return;
      }
      
      try {
          const inv = await getInventoryByWarehouse(warehouseId);

          setWarehouse({
            warehouseId,
            warehouseName: inv.warehouseName,
            warehouseLocation: inv.warehouseLocation,
          });

          setProducts(inv.inventory || []);
      } catch (e) {
          console.error(`Failed to load inventory for warehouse ${warehouseId}:`, e);
          setProducts([]);
      }
    };

    load();
  }, [warehouseId]);

  /**
   * Handler to force a reload of the current warehouse's inventory from the API.
   * Called after successful creation of a new product/stock batch.
   * @async
   */
  const reload = async () => {
    if (!warehouseId) return;
    
    try {
        const inv = await getInventoryByWarehouse(warehouseId);
        setProducts(inv.inventory || []);
    } catch (e) {
         console.error(`Failed to reload inventory for warehouse ${warehouseId}:`, e);
    }
  };


  /**
   * Handler called after a successful product transfer or deletion.
   * It locally removes the transferred/deleted product from the current inventory list,
   * avoiding a full API reload for better performance.
   * @param {string} transferredProductPublicId - The public ID of the product that was removed from this warehouse.
   */
  const handleTransferSuccess = (transferredProductPublicId) => {
      setProducts(currentProducts => 
          currentProducts.filter(p => p.product.publicId !== transferredProductPublicId)
      );
  };


  /**
   * Computed property that applies search, category filtering, and sorting to the inventory list.
   * Note that filtering/sorting is applied to the nested `p.product` properties.
   */
  const filteredProducts = products
    // 1. Search Filter (Product Name or SKU)
    .filter((p) => {
      const name = p.product?.name?.toLowerCase() || "";
      const sku = p.product?.sku?.toLowerCase() || "";
      const term = search.toLowerCase();

      return name.includes(term) || sku.includes(term);
    })
    // 2. Category Filter
    .filter((p) =>
      categoryFilter === "all"
        ? true
        : p.product?.categoryId === Number(categoryFilter)
    )
    // 3. Sort Logic
    .sort((a, b) => {
      // Sort keys are nested under 'product'
      let A = a.product?.[sortBy];
      let B = b.product?.[sortBy];

      if (typeof A === "string") A = A.toLowerCase();
      if (typeof B === "string") B = B.toLowerCase();

      if (A < B) return sortDir === "asc" ? -1 : 1;
      if (A > B) return sortDir === "asc" ? 1 : -1;
      return 0;
    });

  return (
    <div className="mt-4">
      <h3>
        Products in:{" "}
        <span className="text-primary fw-bold">
          {warehouse.warehouseName || "Select a warehouse"}
        </span>
      </h3>

      {/* --- Filter & Sort Controls --- */}
      <div className="card p-3 mb-4 shadow-sm">
        <div className="row g-3">

          <div className="col-md-4">
            <input
              type="text"
              placeholder="Search name or SKU..."
              className="form-control"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
            />
          </div>

          <div className="col-md-3">
            <select
              className="form-select"
              value={categoryFilter}
              onChange={(e) => setCategoryFilter(e.target.value)}
            >
              <option value="all">All Categories</option>
              {categories.map((c) => (
                <option key={c.id} value={c.id}>
                  {c.name}
                </option>
              ))}
            </select>
          </div>

          <div className="col-md-3">
            <select
              className="form-select"
              value={sortBy}
              onChange={(e) => setSortBy(e.target.value)}
            >
              <option value="name">Name</option>
              <option value="sku">SKU</option>
              <option value="price">Price</option>
            </select>
          </div>

          <div className="col-md-2">
            <select
              className="form-select"
              value={sortDir}
              onChange={(e) => setSortDir(e.target.value)}
            >
              <option value="asc">ASC</option>
              <option value="desc">DESC</option>
            </select>
          </div>

        </div>
      </div>
      
      {/* --- Product Table --- */}
      <ProductTable 
          products={filteredProducts} 
          categories={categories} 
          warehouseId={warehouseId} 
          onTransferSuccess={handleTransferSuccess} 
      />

      {/* --- Create Product Modal --- */}
      <CreateProductModal
        show={showCreate}
        onClose={() => setShowCreate(false)}
        onCreated={reload}
      />
    </div>
  );
}