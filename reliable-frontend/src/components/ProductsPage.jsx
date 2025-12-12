import { useState, useEffect } from "react";
import CreateProductModal from "../components/CreateProductModal";
import { getAllProducts, getAllCategories } from "../api/warehouseApi";

/**
 * @file ProductsPage.jsx
 * @author Jevaughn Stewart
 * @version 1.0
 */

/**
 * Page component for managing the catalog of all products (metadata).
 * It allows users to view, filter, and sort the product list, and trigger the
 * creation of new products via a modal.
 *
 * @returns {JSX.Element}
 */
export default function ProductsPage() {
  
  /** State for controlling the visibility of the CreateProductModal. */
  const [showCreate, setShowCreate] = useState(false);
  
  /** State for storing the fetched list of all products. */
  const [products, setProducts] = useState([]);
  
  /** State for storing the fetched list of all categories. */
  const [categories, setCategories] = useState([]);
  
  /** State for the text input used to search by name or SKU. */
  const [search, setSearch] = useState("");
  
  /** State for the selected category ID filter ('all' by default). */
  const [categoryFilter, setCategoryFilter] = useState("all");
  
  /** State for the field to sort by ('name', 'sku', 'price'). */
  const [sortBy, setSortBy] = useState("name");
  
  /** State for the sort direction ('asc' or 'desc'). */
  const [sortDir, setSortDir] = useState("asc");
  
  /**
   * useEffect hook runs once on component mount.
   * Fetches all products and all categories concurrently.
   */
  useEffect(() => {
    const loadProducts = async () => {
      try {
          const data = await getAllProducts();
          setProducts(data);
      } catch (e) {
          console.error("Failed to load products:", e);
      }
    };

    const loadCats = async () => {
      try {
          const data = await getAllCategories();
          setCategories(data);
      } catch (e) {
          console.error("Failed to load categories:", e);
      }
    };

    loadProducts();
    loadCats();
  }, []); 
  
  /**
   * Handler to refresh the product list from the API.
   * Called after a successful product creation.
   * @async
   */
  const refreshProducts = async () => {
    try {
        const data = await getAllProducts();
        setProducts(data);
    } catch (e) {
        console.error("Failed to refresh products:", e);
    }
  };

 
  /**
   * Computed property that applies search, category filtering, and sorting to the product list.
   * The logic is executed on every render cycle where state dependencies change.
   */
  const filteredProducts = products
    // 1. Search Filter (Name or SKU)
    .filter((p) =>
      p.name.toLowerCase().includes(search.toLowerCase()) ||
      p.sku.toLowerCase().includes(search.toLowerCase())
    )
    // 2. Category Filter
    .filter((p) =>
      categoryFilter === "all"
        ? true
        : p.categoryId === Number(categoryFilter)
    )
    // 3. Sort Logic
    .sort((a, b) => {
      let A = a[sortBy];
      let B = b[sortBy];

      // Case-insensitive comparison for strings
      if (typeof A === "string") A = A.toLowerCase();
      if (typeof B === "string") B = B.toLowerCase();

      if (A < B) return sortDir === "asc" ? -1 : 1;
      if (A > B) return sortDir === "asc" ? 1 : -1;
      return 0;
    });


  return (
    <div className="container py-4">
      
      
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h1 className="m-0"><strong>Products</strong></h1>

        <button className="btn btn-success" onClick={() => setShowCreate(true)}>
          + Create Product
        </button>
      </div>

   
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
              <option value="name">Sort by: Name</option>
              <option value="sku">Sort by: SKU</option>
              <option value="price">Sort by: Price</option>
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


      {/* --- Products Table --- */}
      <div className="card shadow-sm table-responsive">
        <table className="table table-hover mb-0 ">
          <thead className="table-light">
            <tr>
              <th>Name</th>
              <th>SKU</th>
              <th>Category</th>
              <th>Unit</th>
              <th>Price</th>
              <th>Hazardous</th>
              <th>Exp. Required</th>
            </tr>
          </thead>

          <tbody>
            {filteredProducts.map((p) => (
              <tr key={p.publicId}>
                <td>{p.name}</td>
                <td>{p.sku}</td>
   <td>{categories.find(cat=> cat.id === p.categoryId )?.name}</td>
                <td>{p.unit}</td>
                <td>${p.price.toFixed(2)}</td>
                <td>{p.isHazardous ? "Yes" : "No"}</td>
                <td>{p.expirationRequired ? "Yes" : "No"}</td>
              </tr>
            ))}

            {/* Empty state row */}
            {filteredProducts.length === 0 && (
              <tr>
                <td colSpan="7" className="text-center py-4 text-muted">
                  No products found.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>


      {/* --- Create Product Modal --- */}
      <CreateProductModal
        show={showCreate}
        onClose={() => setShowCreate(false)}
        onCreated={refreshProducts}
      />
    </div>
  );
}