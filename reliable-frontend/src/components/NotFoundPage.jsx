import { Link } from 'react-router-dom';

export default function NotFoundPage() {
  return (
    <div style={{ textAlign: 'center', padding: '50px', fontFamily: 'Arial, sans-serif' }}>
      <h1 style={{ fontSize: '72px', color: '#dc3545' }}>404</h1>
      <h2 style={{ marginBottom: '20px' }}>Page Not Found</h2>
      <p style={{ marginBottom: '30px' }}>
        We couldn't find the page you were looking for.
      </p>
      <Link to="/" style={{ textDecoration: 'none', color: '#007bff', fontSize: '18px' }}>
        Go to Dashboard
      </Link>
    </div>
  );
}