import { Link } from 'react-router-dom';

/**
 * @file NotFoundPage.jsx
 * @author Jevaughn Stewart
 * @version 1.0
 */

/**
 * Simple error page component displayed when the user navigates to an undefined route.
 * It provides a clear 404 message and a link back to the application's dashboard.
 * * Note: Uses inline CSS for basic styling suitable for a static error page.
 *
 * @returns {JSX.Element}
 */
export default function NotFoundPage() {
  return (
    <div style={{ textAlign: 'center', padding: '50px', fontFamily: 'Arial, sans-serif' }}>
      <h1 style={{ fontSize: '72px', color: '#dc3545' }}>404</h1>
      <h2 style={{ marginBottom: '20px' }}>Page Not Found</h2>
      <p style={{ marginBottom: '30px' }}>
        We couldn't find the page you were looking for.
      </p>
      {/* Use Link from react-router-dom for SPA navigation */}
      <Link to="/" style={{ textDecoration: 'none', color: '#007bff', fontSize: '18px' }}>
        Go to Dashboard
      </Link>
    </div>
  );
}