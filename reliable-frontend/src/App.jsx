import { BrowserRouter, Routes, Route } from "react-router-dom";
import HomePage from "./components/HomePage"
import WarehousesPage from "./components/WarehousePage"
import ProductsPage from "./components/ProductsPage";


function App() {
  return (
    <BrowserRouter>
      <Routes>

        <Route path="/" element={<HomePage />} />

        <Route path="/warehouses" element={<WarehousesPage />} />
         <Route path="/warehouses/products" element={<ProductsPage />} />

      </Routes>
    </BrowserRouter>
  );
}

export default App;
