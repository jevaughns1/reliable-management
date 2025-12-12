import { useState, useEffect } from "react";
import CreateProductModal from "../components/CreateProductModal";
import { getAllProducts, getAllCategories } from "../api/warehouseApi";

export default function ProductsPage() {
  const [showCreate, setShowCreate] = useState(false);
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [search, setSearch] = useState("");
  const [categoryFilter, setCategoryFilter] = useState("all");
  const [sortBy, setSortBy] = useState("name");
  const [sortDir, setSortDir] = useState("asc");
  useEffect(() => {
    const loadProducts = async () => {
      const data = await getAllProducts();
      setProducts(data);
    };

    const loadCats = async () => {
      const data = await getAllCategories();
     
      setCategories(data);
    };

    loadProducts();
    loadCats();
  }, []); 
  const refreshProducts = async () => {
    const data = await getAllProducts();
    setProducts(data);
  };

 
  const filteredProducts = products
    .filter((p) =>
      p.name.toLowerCase().includes(search.toLowerCase()) ||
      p.sku.toLowerCase().includes(search.toLowerCase())
    )
    .filter((p) =>
      categoryFilter === "all"
        ? true
        : p.categoryId === Number(categoryFilter)
    )
    .sort((a, b) => {
      let A = a[sortBy];
      let B = b[sortBy];

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


      <CreateProductModal
        show={showCreate}
        onClose={() => setShowCreate(false)}
        onCreated={refreshProducts}
      />
    </div>
  );
}
