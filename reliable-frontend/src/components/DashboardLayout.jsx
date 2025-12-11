import Sidebar from './Sidebar'; 
import { Container } from 'react-bootstrap';
import { Outlet } from 'react-router-dom';


const SIDEBAR_WIDTH = "260px";

export default function DashboardLayout() {
  return (<>
   <div className="d-flex">
      <Sidebar /> 
      <div 
        className="flex-grow-1"
        style={{ marginLeft: SIDEBAR_WIDTH, padding: '15px' }} 
      >
        <Container fluid>
            <Outlet />
        </Container>
      </div>
    </div>
  </>
   
  );
}