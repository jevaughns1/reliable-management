import Sidebar from './Sidebar';
import { Container } from 'react-bootstrap';
import { Outlet } from 'react-router-dom';

/**
 * @file DashboardLayout.jsx
 * @author Jevaughn Stewart
 * @version 1.0
 */

/**
 * Defines the overall layout structure for the application's dashboard pages.
 * It provides a fixed sidebar and a main content area that adjusts based on screen size.
 *
 * This layout component handles the arrangement of two primary sections:
 * 1. {@link Sidebar}: For navigation.
 * 2. Main Content Area: Renders the active child route component via {@link Outlet}.
 *
 * @returns {JSX.Element}
 */
export default function DashboardLayout() {
  return (
    <> 
   
    <div className="d-flex flex-column flex-md-row">
      
    <Sidebar />
      <div className="flex-grow-1 content-with-sidebar p-3">
        {/* Container fluid ensures the content takes up the full width of the main content area */}
        <Container fluid>
          {/* Outlet renders the content of the currently matched nested route */}
          <Outlet />
        </Container>
      </div>
    </div></>
    
  );
}