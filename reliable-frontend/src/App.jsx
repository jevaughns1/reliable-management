import { BrowserRouter, Routes, Route } from "react-router-dom";
import WarehousesPage from "./components/WarehousePage"
import ProductsPage from "./components/ProductsPage";
import DashboardPage from "./components/DashboardPage";
import DashboardLayout from "./components/DashboardLayout";
import NotFoundPage from "./components/NotFoundPage";
import ExpirationAlertsPage from "./components/ExpirationAlertsPage";

/**
 * @file App.jsx
 * @author Jevaughn Stewart
 * @version 1.0
 */

/**
 * The main component of the application, responsible for setting up the 
 * client-side routing using React Router DOM.
 *
 * It establishes a dashboard layout with nested routes for the main application pages.
 *
 * @returns {JSX.Element}
 */
function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/*
          Route with DashboardLayout acts as the parent layout. 
          Child routes render their content inside the <Outlet /> defined in DashboardLayout.
        */}
        <Route path="/" element={<DashboardLayout />}>
          
          {/* Default route for the dashboard (renders at '/') */}
          <Route index element={<DashboardPage />} />
          
          {/* Main application pages */}
          <Route path="products" element={<ProductsPage />} />
          <Route path="warehouses" element={<WarehousesPage />} />
          <Route path="alerts" element={<ExpirationAlertsPage />} />
        </Route>
        
        {/* Catch-all route for any undefined path (404 Page) */}
        <Route path="*" element={<NotFoundPage/>} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;