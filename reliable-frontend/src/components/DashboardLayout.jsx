import Sidebar from './Sidebar';
import { Container } from 'react-bootstrap';
import { Outlet } from 'react-router-dom';

export default function DashboardLayout() {
  return (
    <div className="d-flex flex-column flex-md-row">
      <Sidebar />

      <div className="flex-grow-1 content-with-sidebar p-3">
        <Container fluid>
          <Outlet />
        </Container>
      </div>
    </div>
  );
}
