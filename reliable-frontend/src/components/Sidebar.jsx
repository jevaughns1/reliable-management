import { useState } from "react";
import { Nav, Button } from "react-bootstrap";
import { Link, useLocation } from "react-router-dom";

/**
 * @file Sidebar.jsx
 * @author Jevaughn Stewart
 * @version 1.0
 */

/**
 * Sidebar component providing the main application navigation.
 * It features a responsive design, displaying full-time on desktop/large screens
 * and collapsing into a toggle menu button on small screens (mobile).
 * Uses React Router's Link for navigation and highlights the active route
 * using the current URL path obtained via {@link useLocation}.
 *
 * @returns {JSX.Element}
 */
export default function Sidebar() {
  
  /** Hook to get the current location object from React Router, used for active link highlighting. */
  const location = useLocation();
  
  /** State to control the visibility of the collapsible menu on small screens. */
  const [open, setOpen] = useState(false);

  return (
    <div
      className="sidebar bg-dark text-white p-3 shadow"
      style={{ zIndex: 1000 }}
    >
      
      {/* --- Mobile/Small Screen Header and Toggle Button --- */}
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
      
      {/* --- Desktop/Large Screen Header --- */}
      <h3 className="mb-4 border-bottom pb-2 d-none d-md-block">
        
        <strong>Reliable Admin</strong>
      </h3>
      
      {/* --- Navigation Links (Visible on Desktop always, or when 'open' on Mobile) --- */}
      <div className={`${open ? "d-block" : "d-none"} d-md-block mt-3`}>
        <Nav className="flex-column">
          
          {/* Inventory Control Link (Root Path) */}
          <Nav.Item className="mb-2">
            <Nav.Link
              as={Link}
              to="/"
              className={
                location.pathname === "/"
                  ? "text-primary fw-bold"
                  : "text-white"
              }
              // Close the menu after clicking on mobile
              onClick={() => setOpen(false)} 
            >
             Inventory Control 
            </Nav.Link>
          </Nav.Item>

          {/* Warehouses Link */}
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

          {/* Products Link */}
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
          <Nav.Item className="mb-2">
            <Nav.Link
              as={Link}
              to="/alerts"
              className={
                location.pathname === "/alerts"
                  ? "text-primary fw-bold"
                  : "text-white"
              }
              onClick={() => setOpen(false)}
            >
              Alerts
            </Nav.Link>
          </Nav.Item>
        </Nav>
      </div>
    </div>
  );
}