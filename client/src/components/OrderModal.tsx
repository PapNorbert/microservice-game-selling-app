import { Alert, Button, Col, Container, Form, Modal, Row } from "react-bootstrap";
import { useEffect, useState } from "react";
import { useMutation } from "@tanstack/react-query";
import { useSubscription } from "react-stomp-hooks";
import { useNavigate } from "react-router-dom";

import useAuth from "../hooks/useAuth";
import configuredAxios from "../axios/configuredAxios";
import { apiPrefix } from '../config/application.json'
import { OrderCreation } from "../interface/OrderCreation";
import { AxiosError, AxiosResponse } from "axios";
import { ErrorResponseData } from "../interface/errorResponseInterface";


interface PropType {
  showModal: boolean;
  setShowModal: React.Dispatch<React.SetStateAction<boolean>>
  productPrice: number;
  productName: string;
  announcementId: number
}

export default function OrderModal({ showModal, setShowModal, productPrice, productName, announcementId }: PropType) {
  const { auth, setAuth } = useAuth();
  const [orderAddress, setOrderAddress] = useState<string>(auth.address || '');
  const [orderAddressError, setOrderAddressError] = useState<string>('');
  const orderPrice = productPrice < 200 ? productPrice + 13 : productPrice;
  const orderUrl = `/${apiPrefix}/orders`;
  const [error, setError] = useState<string>('');
  const navigate = useNavigate();
  const [processingRequest, setProcessingRequest] = useState<boolean>(false);

  const { mutate: mutatePost } = useMutation({
    mutationFn: postMutationFunction,
    onSuccess: handleSubmitSucces,
    onError: handleSubmitError,
  });

  useSubscription(`/queue/order/create`, (message) => {
    // response for post request after processing finished
    const creationResponse: OrderModificationResponse = JSON.parse(message.body);
    setProcessingRequest(false);
    if( creationResponse.transactionSuccess) {
      navigate(`/orders/${creationResponse.orderId}`);
    } else {
      setError("There was an error creating your order.");
    }

  });

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


  function postMutationFunction(data: OrderCreation) {
    return configuredAxios.post(orderUrl, data);
  }

  function handleSubmitSucces(response: AxiosResponse) {
    if (error) {
      setError('');
    }
    if (response.status === 202) {
      setProcessingRequest(true);
    }
  }

  function handleSubmitError(error: AxiosError<ErrorResponseData>) {
    if (error.message === 'Network Error') {
      setError('Error connecting to the server')
    } else {
      setError(error.response?.data.errorMessage || 'Error creating the order');
    }
  }

  function handleClose() {
    setShowModal(false);
  }

  function handleOrder() {
    if (auth.logged_in && auth.userId) {
      mutatePost({
        announcementId: announcementId,
        buyerId: auth.userId,
        price: orderPrice,
        orderAddress: orderAddress
      });
    }
  }

  function changeOrderAddress(newValue: string) {
    setOrderAddress(newValue);
    if (newValue === '' || newValue === null) {
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
            {orderPrice} lei
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
      {processingRequest &&
        <Container className="fst-italic fs-4 text-center mb-2">Creating order...</Container>
      }
      {
        <Modal.Footer>
          <Button variant="secondary" onClick={handleClose}>
            Close
          </Button>
          <Button className="btn btn-orange-dark" onClick={handleOrder}>
            Order product
          </Button>
        </Modal.Footer>
      }
      <Alert key='danger' variant='danger' show={error !== ''}
        onClose={() => setError('')} dismissible className="mt-2">
        {error}
      </Alert>
    </Modal>
  )
}
