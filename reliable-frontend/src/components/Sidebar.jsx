import { useState } from "react";
import { Nav, Button } from "react-bootstrap";
import { Link, useLocation } from "react-router-dom";
export default function Sidebar() {
  const location = useLocation();
  const [open, setOpen] = useState(false);

  return (
    <div
      className="sidebar bg-dark text-white p-3 shadow"
      style={{ zIndex: 1000 }}
    >
      <div className="d-flex justify-content-between align-items-center d-md-none">
        <h3 className="mb-0">Reliable Admin</h3>

        <Button
          variant="outline-light"
          className="border-0"
          onClick={() => setOpen(!open)}
        >
          ☰
        </Button>
      </div>
      <h3 className="mb-4 border-bottom pb-2 d-none d-md-block">
        
        <strong>Reliable Admin</strong>
      </h3>
      <div className={`${open ? "d-block" : "d-none"} d-md-block mt-3`}>
        <Nav className="flex-column">
          <Nav.Item className="mb-2">
            <Nav.Link
              as={Link}
              to="/"
              className={
                location.pathname === "/"
                  ? "text-primary fw-bold"
                  : "text-white"
              }
              onClick={() => setOpen(false)}
            >
             Inventory Control 
            </Nav.Link>
          </Nav.Item>

          <Nav.Item className="mb-2">
            <Nav.Link
              as={Link}
              to="/warehouses"
              className={
                location.pathname === "/warehouses"
                  ? "text-primary fw-bold"
                  : "text-white"
              }
              onClick={() => setOpen(false)}
            >
              Warehouses
            </Nav.Link>
          </Nav.Item>

          <Nav.Item className="mb-2">
            <Nav.Link
              as={Link}
              to="/products"
              className={
                location.pathname === "/products"
                  ? "text-primary fw-bold"
                  : "text-white"
              }
              onClick={() => setOpen(false)}
            >
              Products
            </Nav.Link>
          </Nav.Item>
        </Nav>
      </div>
    </div>
  );
}
