import { useState, useEffect } from "react";
import toast from "react-hot-toast";
import { ListGroup, Card, Row, Col, Alert, Badge } from "react-bootstrap";
import { differenceInDays, parseISO } from "date-fns";
import { getNearingExpirationAlerts, getExpiredInventory } from "../api/warehouseApi";

const EXPIRATION_WINDOW_DAYS = 30;

export default function ExpirationAlertsPage() {
    const [nearingExpired, setNearingExpired] = useState([]);
    const [expired, setExpired] = useState([]);
    const [loading, setLoading] = useState(true);

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
    }, []);

    // Helper to determine the color of the alert badge
    const getDaysRemaining = (dateString) => {
        const expirationDate = parseISO(dateString);
        return differenceInDays(expirationDate, new Date());
    };

    const getBadgeVariant = (days) => {
        if (days <= 0) return "danger";
        if (days <= 7) return "warning";
        if (days <= 30) return "primary";
        return "secondary";
    };
    
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
                <div className="flex-grow-1">
                    <strong className="text-truncate d-block">{item.product.name}</strong>
                    <small className="text-muted">
                        SKU: {item.product.sku} | Location: {item.storageLocation || 'N/A'}
                    </small>
                </div>
                
                <div className="text-end ms-3">
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
                Expiration & Obsolescence Alerts
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