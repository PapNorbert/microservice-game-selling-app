import { Card, Col, Row, Stack } from "react-bootstrap";
import { Order } from "../../interface/OrdersInterface"
import { dateFormatOptions } from "../../util/dateOptions";
import { useNavigate } from "react-router-dom";

interface PropType {
  order: Order;
}

export default function OrderListingElement({ order }: PropType) {
  const navigate = useNavigate();

  return (
    <Card className="clickable" onClick={() => navigate(`/orders/${order.orderId}`)}>
      <Card.Header>
        <Stack direction="horizontal" gap={3}>
          <span>Order nr. </span>
          <span>{order.orderId}</span>
        </Stack>
      </Card.Header>
      <Card.Body>
        <Row>
          <Col lg='2'>
            Order date:
          </Col>
          <Col>
            {new Date(order.orderDate).toLocaleDateString('ro-RO', dateFormatOptions)}
          </Col>
        </Row>
        <Row>
          <Col lg='2'>
            Product name:
          </Col>
          <Col>
            {order.announcement.soldGameDiscName}
          </Col>
        </Row>
        <Row>
          <Col lg='2'>
            Order address:
          </Col>
          <Col>
            {order.orderAddress}
          </Col>
        </Row>
      </Card.Body>
    </Card>
  )
}
