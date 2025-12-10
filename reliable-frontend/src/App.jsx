import { BrowserRouter, Routes, Route } from "react-router-dom";
import HomePage from "./components/HomePage"
import WarehousesPage from "./components/WarehousePage"


function App() {
  return (
    <BrowserRouter>
      <Routes>

        <Route path="/" element={<HomePage />} />

        <Route path="/warehouses" element={<WarehousesPage />} />
        

      </Routes>
    </BrowserRouter>
  );
}

export default App;
