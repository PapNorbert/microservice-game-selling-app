import { useState } from 'react';
import { Button, Card, Col, Row } from 'react-bootstrap'
import OrderModal from '../../OrderModal';
import useAuth from '../../../hooks/useAuth';


interface PropType {
  productPrice: number;
  announcementId: number
  productName: string;
}

export default function OrderCard({ productPrice, productName, announcementId }: PropType) {
  const [showModal, setShowModal] = useState<boolean>(false);
  const { auth } = useAuth();

  return (
    <>
      <OrderModal showModal={showModal} setShowModal={setShowModal} announcementId={announcementId}
        productPrice={productPrice} productName={productName} />
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
          <Button className='btn-orange-dark' disabled={!auth.logged_in} onClick={() => setShowModal(true)}>
            { auth.logged_in? 'Order' : 'Log in first to order'}
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
    </>
  )
}
