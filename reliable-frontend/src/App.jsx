import { BrowserRouter, Routes, Route } from "react-router-dom";
import WarehousesPage from "./components/WarehousePage"
import ProductsPage from "./components/ProductsPage";
import DashboardPage from "./components/DashboardPage";
import DashboardLayout from "./components/DashboardLayout";
import NotFoundPage from "./components/NotFoundPage";


function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<DashboardLayout />}>
          <Route index element={<DashboardPage />} />
          <Route path="products" element={<ProductsPage />} />
          <Route path="warehouses" element={<WarehousesPage />} />
        </Route>
        <Route path="*" element={<NotFoundPage/>} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;