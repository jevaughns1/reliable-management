import { useState ,useEffect} from "react";
import CreateProductModal from "../components/CreateProductModal";
import { getAllProducts } from "../api/warehouseApi";

export default function ProductsPage() {
  const [showCreate, setShowCreate] = useState(false);
  const [products, setProducts] = useState([]);

 
   const refreshProducts = async () => {
     const data = await getAllProducts();
     setProducts(data);
    
   };

  useEffect(() => {
     const loadProducts = async () => {
    const data = await getAllProducts();
    setProducts(data);
  };
 
    loadProducts();
  }, []);

  return (
    <div className="container mt-4">
      <div className="d-flex justify-content-between align-items-center mb-3">
        <h1>Products</h1>
        <button
          className="btn btn-success"
          onClick={() => setShowCreate(true)}
        >
          + Create Product
        </button>
      </div>

      <CreateProductModal
        show={showCreate}
        onClose={() => setShowCreate(false)}
        onCreated={refreshProducts}
      />
{
   products&& products.map((product,index)=>
   <span key={index}>{product}</span>)
}
      
    </div>
  );
}
