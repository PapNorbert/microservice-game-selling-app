import { useState } from "react";
import { useMutation } from "@tanstack/react-query";
import { Alert, Button, Card, Container } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import { AxiosError } from "axios";

import { Order } from "../../interface/OrdersInterface"
import { dateFormatOptions } from "../../util/dateOptions";
import { convertConsoleTypeName } from "../../util/consoleTypeNameConversion";
import RowWithKeyValue from "../RowWithKeyValue";
import { timeAllowedForOrderCancelInMiliSec, apiPrefix } from '../../config/application.json'
import { ErrorResponseData } from "../../interface/errorResponseInterface";
import configuredAxios from "../../axios/configuredAxios";
import useAuth from "../../hooks/useAuth";

interface PropType {
  order: Order;
}

export default function OrderDetailedElement({ order }: PropType) {
  const { auth } = useAuth();
  const navigate = useNavigate();
  const currentDate = new Date();
  const orderDate = new Date(order.orderDate);
  // can be canceled if less than a day 
  const canBeCanceled = (currentDate.getTime() - orderDate.getTime()) < timeAllowedForOrderCancelInMiliSec;
  const [submitError, setSubmitError] = useState<string | null>(null);
  const [deleted, setDeleted] = useState<boolean>(false);
  const deleteUrl = `${apiPrefix}/orders/${order.orderId}`;


  const { mutate: mutateDelete } = useMutation({
    mutationFn: deleteMutationFunction,
    onSuccess: handleDeleteSubmitSucces,
    onError: handleSubmitError,
  });

  function deleteMutationFunction() {
    return configuredAxios.delete(deleteUrl);
  }

  function handleDeleteSubmitSucces() {
    setDeleted(true);
  }

  function handleSubmitError(error: AxiosError<ErrorResponseData>) {
    if (error.message === 'Network Error') {
      setSubmitError('Error connecting to the server')
    } else {
      setSubmitError(error.response?.data.errorMessage || 'Error processing the request');
    }
  }

  function handleCancleOrderButtonClicked() {
    if (auth.logged_in) {
      mutateDelete();
    } else {
      setSubmitError('Error: Cannot delete')
    }
  }

  if (deleted) {
    return (
      <Container className='text-center mt-5 w-50' >
        <Alert variant="success">
          <Alert.Heading>
            Order Canceled Succesfully
          </Alert.Heading>
        </Alert>
        <h3 className="clickable text-center fst-italic text-decoration-underline" onClick={() => navigate('/orders')}>Orders</h3>
      </Container>
    )
  }

  return (
    <>
      <h1>Details of order nr. {order.orderId}</h1>
      <Card >
        <Card.Body>
          <RowWithKeyValue keyString="Order date:"
            valueString={new Date(order.orderDate).toLocaleDateString('ro-RO', dateFormatOptions)} />
          <RowWithKeyValue keyString="Order address:" valueString={order.orderAddress} />
          <RowWithKeyValue keyString="Total price:" valueString={order.price.toString()} />
          <RowWithKeyValue keyString="Product price price:" valueString={order.announcement.price.toString()} />
          <RowWithKeyValue keyString="Product name:" valueString={order.announcement.soldGameDiscName} />
          <RowWithKeyValue keyString="Console:"
            valueString={convertConsoleTypeName(order.announcement.soldGameDiscType) || 'Unknown'} />
          <RowWithKeyValue keyString="Condition:" valueString={order.announcement.newDisc ? 'New' : 'Used'} />

          {canBeCanceled &&
            <Button className='mt-4 btn btn-orange-dark float-end' onClick={handleCancleOrderButtonClicked}>
              Cancel Order
            </Button>
          }
        </Card.Body>
      </Card>
      <Alert key='danger' variant='danger' show={submitError !== null} className='mt-3'
        onClose={() => setSubmitError(null)} dismissible >
        {submitError}
      </Alert>
    </>
  )
}
