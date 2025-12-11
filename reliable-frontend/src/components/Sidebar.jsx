import { Nav } from 'react-bootstrap';
import { Link, useLocation } from 'react-router-dom'; 

export default function Sidebar() {
  const location = useLocation();

  const SIDEBAR_WIDTH = "260px";

  return (
    <div
      className="d-flex flex-column p-3 text-white bg-dark shadow"
      style={{
        width: SIDEBAR_WIDTH,
        minHeight: "100vh",
        position: "fixed",
        top: 0,
        left: 0,
        zIndex: 1000, 
      }}
    >
      <h3 className="mb-4 border-bottom pb-2">Reliable Admin</h3>

      <Nav className="flex-column">
        <Nav.Item className="mb-2">
          <Nav.Link 
            as={Link} 
            to="/" 
            className={location.pathname === '/' ? 'text-primary fw-bold' : 'text-white'}
          >
            Dashboard
          </Nav.Link>
        </Nav.Item>
        <Nav.Item className="mb-2">
          <Nav.Link 
            as={Link} 
            to="/warehouses" 
            className={location.pathname === '/warehouses' ? 'text-primary fw-bold' : 'text-white'}
          >
            Warehouses
          </Nav.Link>
        </Nav.Item>
        <Nav.Item className="mb-2">
          <Nav.Link 
            as={Link} 
            to="/products" 
            className={location.pathname === '/products' ? 'text-primary fw-bold' : 'text-white'}
          >
            Products
          </Nav.Link>
        </Nav.Item>
      </Nav>
    </div>
  );
}