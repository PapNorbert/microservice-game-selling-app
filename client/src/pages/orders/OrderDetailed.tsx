import { useQuery } from "@tanstack/react-query";
import { AxiosError, AxiosResponse } from "axios";
import { Navigate, useNavigate, useParams } from "react-router-dom";

import configuredAxios from "../../axios/configuredAxios";
import { apiPrefix } from '../../config/application.json'
import { Order } from "../../interface/OrdersInterface";
import OrderDetailedElement from "../../components/Orders/OrderDetailedElement";
import { Container } from "react-bootstrap";
import { useState } from "react";
import { useSubscription } from "react-stomp-hooks";

export default function OrderDetailed() {
  const { orderId } = useParams();
  const orderUrl = `/${apiPrefix}/orders/${orderId}`;
  const navigate = useNavigate();
  const [orderDataWs, setOrderDataWs] = useState<Order>();
  const [foundOrder, setFoundOrder] = useState<boolean>();
  const { data: orderData, isError, error, isLoading } = useQuery<AxiosResponse<Order>, AxiosError>({
    queryKey: ["orderDetailed", orderUrl],
    queryFn: queryFunction,
    retry: false
  });

  useSubscription(`/queue/order/${orderId}`, (message) => {
    // queue format:  /queue/order/{orderId}
    const data: Order = JSON.parse(message.body);
    if (data.orderId == null) {
      setFoundOrder(false);
    } else {
      setFoundOrder(true);
      setOrderDataWs(data);
    }
  });

  function queryFunction() {
    return configuredAxios.get(orderUrl);
  }

  if (isLoading) {
    return (
      <>
        <h2 className="text-center">Loading...</h2>
      </>
    )
  }

  if (isError) {
    if (error.response?.status === 401) {
      return (
        <Navigate to='/login' state={{ from: location }} replace />
      )
    }

    if (error?.response?.status === 403 || error?.response?.status === 404) {
      return (
        <>
          <h2 className="error">This order was not found!</h2>
          <h3 className="clickable text-center fst-italic text-decoration-underline" onClick={() => navigate('/orders')}>Orders</h3>
        </>
      )
    }

    return (
      <>
        <h2 className="error">{error.message || 'Sorry, there was an error!'}</h2>
      </>
    )
  }

  if (foundOrder === false) {
    return (
      <>
        <h2 className="error">This order was not found!</h2>
        <h3 className="clickable text-center fst-italic text-decoration-underline" onClick={() => navigate('/orders')}>Orders</h3>
      </>
    )
  }

  if ( foundOrder && orderDataWs) {
    return (
      <>
        <OrderDetailedElement order={orderDataWs} key={orderId} />
      </>
    )
  }

  if (orderData?.status === 202 && !orderDataWs) {
    return (
      <Container className="fst-italic fs-3 text-center">Loading order...</Container>
    )
  }

  if (orderData?.data) {
    return (
      <>
        <OrderDetailedElement order={orderData.data} key={orderId} />
      </>
    )
  }


}
