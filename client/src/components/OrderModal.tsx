import { Button, Col, Form, Modal, Row } from "react-bootstrap";
import { useEffect, useState } from "react";

import useAuth from "../hooks/useAuth";
import configuredAxios from "../axios/configuredAxios";
import { apiPrefix } from '../config/application.json'


interface PropType {
  showModal: boolean;
  setShowModal: React.Dispatch<React.SetStateAction<boolean>>
  productPrice: number;
  productName: string;
}

export default function OrderModal({ showModal, setShowModal, productPrice, productName }: PropType) {
  const { auth, setAuth } = useAuth();
  const [orderAddress, setOrderAddress] = useState<string>(auth.address || '');
  const [orderAddressError, setOrderAddressError] = useState<string>('');

  useEffect(() => {
    // get user address if it wasn't set
    if (auth.logged_in && auth.userId && !auth.address) {
      configuredAxios.get(`/${apiPrefix}/users/${auth.userId}/address`)
        .then((response) => {
          setAuth((previousAuth) => ({
            ...previousAuth,
            address: response.data.address
          }));
          setOrderAddress(response.data.address);
        })
        .catch(() => {
        })
    }
  }, [auth.logged_in, auth.userId, auth.address]);


  function handleClose() {
    setShowModal(false);
  }

  function changeOrderAddress(newValue: string) {
    setOrderAddress(newValue);
    if( newValue === '' || newValue === null) {
      setOrderAddressError('Please enter an address to be delivered to!');
    } else {
      setOrderAddressError('');

    }
  }

  return (auth.logged_in &&
    <Modal show={showModal} onHide={handleClose} centered>
      <Modal.Header closeButton>
        <Modal.Title>Create order</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Row>
          <Col>
            Product
          </Col>
          <Col>
            {productName}
          </Col>
        </Row>
        <Row className="mt-2 fw-bold fs-6">
          Price
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
            Delivery
          </Col>
          <Col>
            {productPrice < 200 ? '13 lei' : '0 lei'}
          </Col>
        </Row>
        <Row className="mt-1 fw-bold">
          <Col>
            Total price:
          </Col>
          <Col>
            {productPrice < 200 ? productPrice+13 : productPrice} lei
          </Col>
        </Row>
        <Row className="mt-2 fw-bold">
          Payment
        </Row>
        <Row>
          <Col>
            Cash on delivery
          </Col>
        </Row>
        <Row className="mt-2 fw-bold fs-6">
          Delivery Address
        </Row>
        <Row>
          <Col>
              <Form.Control as='textarea' rows={1}
                placeholder='Address' className="mt-2"
                value={orderAddress} isInvalid={!!orderAddressError} autoComplete='off'
                onChange={e => { changeOrderAddress(e.target.value) }} />
              <Form.Text className='text-muted'>
                Your address used for the order
              </Form.Text>
              <Form.Control.Feedback type='invalid' >
                {orderAddressError}
              </Form.Control.Feedback>
          </Col>
        </Row>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={handleClose}>
          Close
        </Button>
        <Button className="btn btn-orange-dark" onClick={handleClose}>
          Order product
        </Button>
      </Modal.Footer>
    </Modal>
  )
}
