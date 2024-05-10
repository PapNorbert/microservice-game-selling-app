import { Button, Card, Col, Row } from 'react-bootstrap'


interface PropType {
  productPrice: number;
}


export default function OrderCard({ productPrice }: PropType) {
  return (
    <Card key={`container_order`} className='mt-5 mb-3'>
      <Row className='fs-5 fw-semibold'>
        Order
      </Row>
      <Row>
        <Col>
          Product
        </Col>
        <Col>
          {productPrice} lei
        </Col>
      </Row>
      <Row>
        <Col>
          Delivery from*
        </Col>
        <Col>
          {productPrice < 200 ? '13 lei' : '0 lei'}
        </Col>
      </Row>
      <Row className='fst-italic'>
        *Free delivery is valid for orders over 199 lei
      </Row>
      <Row className='mt-3 mb-3'>
        <Button className='btn-orange-dark' onClick={() => { }}>
          Order
        </Button>
      </Row>
      
      <Row>
        <Col className='fw-semibold'>
          Delivery
        </Col>
        <Col>
          FAN Courier
        </Col>
      </Row>
      <Row>
        <Col className='fw-semibold'>
          Payment
        </Col>
        <Col>
          Cash on delivery
        </Col>
      </Row>
    </Card>
  )
}
