import { useState, useEffect } from "react";
import toast from "react-hot-toast";
import { ListGroup, Card, Row, Col, Alert, Badge } from "react-bootstrap";
import { differenceInDays, parseISO } from "date-fns";
import { getNearingExpirationAlerts, getExpiredInventory } from "../api/warehouseApi";

/**
 * Defines the lookahead window (in days) for "Nearing Expiration" alerts.
 * @constant {number}
 */
const EXPIRATION_WINDOW_DAYS = 30;

/**
 * @file ExpirationAlertsPage.jsx
 * @author Jevaughn Stewart
 * @version 1.0
 */

/**
 * Component that displays inventory alerts categorized into "Already Expired" and 
 * "Nearing Expiration" (within the next 30 days).
 *
 * It fetches data using {@link getNearingExpirationAlerts} and {@link getExpiredInventory}
 * and uses date-fns for calculating remaining days.
 *
 * @returns {JSX.Element}
 */
export default function ExpirationAlertsPage() {
    
    /**
     * State to hold inventory items expiring within the defined window (30 days).
     * @type {[Array<object>, function]}
     */
    const [nearingExpired, setNearingExpired] = useState([]);
    
    /**
     * State to hold inventory items that have already expired.
     * @type {[Array<object>, function]}
     */
    const [expired, setExpired] = useState([]);
    
    /**
     * State to manage the loading status of the alert data.
     * @type {[boolean, function]}
     */
    const [loading, setLoading] = useState(true);

    /**
     * useEffect hook to fetch all expiration alerts upon component mount.
     * It handles fetching both "nearing" and "expired" data simultaneously.
     */
    useEffect(() => {
        const loadAlerts = async () => {
            setLoading(true);
            try {
                // Fetch items expiring within the next 30 days
                const nearingData = await getNearingExpirationAlerts(EXPIRATION_WINDOW_DAYS);
                setNearingExpired(nearingData);
                
                // Fetch items already past their expiration date
                const expiredData = await getExpiredInventory();
                setExpired(expiredData);

            } catch (error) {
                console.error("Failed to load expiration alerts:", error);
                toast.error("Failed to load expiration alerts.");
            } finally {
                setLoading(false);
            }
        };
        loadAlerts();
    }, []); // Empty dependency array means this runs once on mount

    /**
     * Calculates the number of days remaining until the expiration date.
     * Negative numbers indicate the item is already expired.
     * @param {string} dateString - The expiration date in ISO format (YYYY-MM-DD).
     * @returns {number} The difference in days (Expiration Date - Current Date).
     */
    const getDaysRemaining = (dateString) => {
        const expirationDate = parseISO(dateString);
        return differenceInDays(expirationDate, new Date());
    };

    /**
     * Determines the Bootstrap badge variant based on the days remaining.
     * @param {number} days - The number of days remaining.
     * @returns {string} The Bootstrap variant ('danger', 'warning', 'primary', or 'secondary').
     */
    const getBadgeVariant = (days) => {
        if (days <= 0) return "danger"; // Already expired
        if (days <= 7) return "warning"; // Critical window
        if (days <= 30) return "primary"; // Near term
        return "secondary"; // Default (shouldn't happen with the API calls)
    };
    
    /**
     * Renders a single inventory alert item within a ListGroup.Item.
     * @param {object} item - The WarehouseInventoryDTO item.
     * @returns {JSX.Element|null} The rendered list item or null if no expiration date exists.
     */
    const renderAlertItem = (item) => {
        // Ensure date exists before processing
        if (!item.expirationDate) return null; 

        const daysRemaining = getDaysRemaining(item.expirationDate);
        const variant = getBadgeVariant(daysRemaining);
        const statusText = daysRemaining <= 0 ? "EXPIRED" : `${daysRemaining} days left`;
        
        return (
            <ListGroup.Item 
                key={`${item.productPublicId}-${item.storageLocation}`} 
                className={`d-flex justify-content-between align-items-center list-group-item-${variant}`}
            >
                {/* FIX: Added w-100 and overflow-hidden to the flex-grow-1 container 
                  to ensure the text-truncate class works correctly, preventing overflow. 
                */}
                <div className="flex-grow-1 w-100 overflow-hidden me-3"> 
                    {/* Ensure long text is truncated */}
                    <strong className="text-truncate d-block">{item.product.name}</strong> 
                    <small className="text-muted text-truncate d-block">
                        SKU: {item.product.sku} | Location: {item.storageLocation || 'N/A'}
                    </small>
                </div>
                
                <div className="text-end flex-shrink-0"> {/* Use flex-shrink-0 to protect the badge/qty area */}
                    <Badge bg={variant} className="mb-1 d-block">
                        {statusText}
                    </Badge>
                    <span className="fw-bold">Qty: {item.quantity}</span>
                </div>
            </ListGroup.Item>
        );
    };

    if (loading) {
        return <div className="container py-4">Loading expiration data...</div>;
    }

    const totalAlerts = expired.length + nearingExpired.length;
    
    return (
        <div className="container py-4">
            <h2 className="mb-4 d-flex justify-content-between align-items-center">
                <strong>Expiration & Obsolescence Alerts</strong>
                <Badge bg={totalAlerts > 0 ? "danger" : "success"} pill>
                    {totalAlerts} Critical Items
                </Badge>
            </h2>

            <Row className="g-4">
                {/* EXPIRED ITEMS CARD */}
                <Col md={6}>
                    <Card border="danger" className="shadow-sm h-100">
                        <Card.Header className="bg-danger text-white">
                            <h5 className="mb-0">
                                🛑 Already Expired Stock ({expired.length})
                            </h5>
                        </Card.Header>
                        <ListGroup variant="flush">
                            {expired.length > 0 ? (
                                expired.map(renderAlertItem)
                            ) : (
                                <Alert variant="success" className="m-3">
                                    No items currently expired!
                                </Alert>
                            )}
                        </ListGroup>
                    </Card>
                </Col>

                {/* NEARING EXPIRATION */}
                <Col md={6}>
                    <Card border="warning" className="shadow-sm h-100">
                        <Card.Header className="bg-warning text-dark">
                            <h5 className="mb-0">
                                ⚠️ Nearing Expiration (Next {EXPIRATION_WINDOW_DAYS} Days) ({nearingExpired.length})
                            </h5>
                        </Card.Header>
                        <ListGroup variant="flush">
                            {nearingExpired.length > 0 ? (
                                nearingExpired.map(renderAlertItem)
                            ) : (
                                <Alert variant="info" className="m-3">
                                    No items expiring soon. Good inventory management!
                                </Alert>
                            )}
                        </ListGroup>
                    </Card>
                </Col>
            </Row>

        </div>
    );
}