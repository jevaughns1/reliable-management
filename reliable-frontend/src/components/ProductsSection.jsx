import { useState, useEffect } from "react";
import CreateProductModal from "../components/CreateProductModal";
import ProductTable from "../components/ProductTable";
import { getInventoryByWarehouse, getAllCategories } from "../api/warehouseApi";

export default function ProductsSection({ warehouseId }) {
  const [showCreate, setShowCreate] = useState(false);
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);

  const [search, setSearch] = useState("");
  const [categoryFilter, setCategoryFilter] = useState("all");
  const [sortBy, setSortBy] = useState("name");
  const [sortDir, setSortDir] = useState("asc");

  const [warehouse, setWarehouse] = useState({
    warehouseId: "",
    warehouseName: "",
    warehouseLocation: ""
  });

 
  useEffect(() => {
    const load = async () => {
      const data = await getAllCategories();
      setCategories(data);
    };
    load();
  }, []);

  useEffect(() => {
    const load = async () => {
      if (!warehouseId) {
        setProducts([]);
        return;
      }

      const inv = await getInventoryByWarehouse(warehouseId);

      setWarehouse({
        warehouseId,
        warehouseName: inv.warehouseName,
        warehouseLocation: inv.warehouseLocation,
      });

      setProducts(inv.inventory || []);
    };

    load();
  }, [warehouseId]);

  // Reload after adding product
  const reload = async () => {
    if (!warehouseId) return;

    const inv = await getInventoryByWarehouse(warehouseId);
    setProducts(inv.inventory || []);
  };


  const handleTransferSuccess = (transferredProductPublicId) => {
      setProducts(currentProducts => 
          currentProducts.filter(p => p.product.publicId !== transferredProductPublicId)
      );
  };


  // FILTER + SORT
  const filteredProducts = products
    .filter((p) => {
      const name = p.product?.name?.toLowerCase() || "";
      const sku = p.product?.sku?.toLowerCase() || "";
      const term = search.toLowerCase();

      return name.includes(term) || sku.includes(term);
    })
    .filter((p) =>
      categoryFilter === "all"
        ? true
        : p.product?.categoryId === Number(categoryFilter)
    )
    .sort((a, b) => {
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
      
  
      <ProductTable 
          products={filteredProducts} 
          categories={categories} 
          warehouseId={warehouseId} 
          onTransferSuccess={handleTransferSuccess} 
      />

      <CreateProductModal
        show={showCreate}
        onClose={() => setShowCreate(false)}
        onCreated={reload}
      />
    </div>
  );
}