import { BrowserRouter, Routes, Route } from "react-router-dom";
import WarehousesPage from "./components/WarehousePage"
import ProductsPage from "./components/ProductsPage";
import DashboardPage from "./components/DashboardPage";
import DashboardLayout from "./components/DashboardLayout";
import NotFoundPage from "./components/NotFoundPage";
import ExpirationAlertsPage from "./components/ExpirationAlertsPage";



function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<DashboardLayout />}>
          <Route index element={<DashboardPage />} />
          <Route path="products" element={<ProductsPage />} />
          <Route path="warehouses" element={<WarehousesPage />} />
          <Route path="alerts" element={<ExpirationAlertsPage />} />
        </Route>
        <Route path="*" element={<NotFoundPage/>} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;