import { useQuery } from "@tanstack/react-query";
import { AxiosError, AxiosResponse } from "axios";
import { Navigate, useNavigate, useParams } from "react-router-dom";

import configuredAxios from "../../axios/configuredAxios";
import {apiPrefix} from '../../config/application.json'
import { Order } from "../../interface/OrdersInterface";
import OrderDetailedElement from "../../components/Orders/OrderDetailedElement";

export default function OrderDetailed() {
  const { orderId } = useParams();
  const orderUrl = `/${apiPrefix}/orders/${orderId}`;
  const navigate = useNavigate();

  const { data: orderData, isError, error, isLoading } = useQuery<AxiosResponse<Order>, AxiosError>({
    queryKey: ["orderDetailed", orderUrl],
    queryFn: queryFunction,
    retry: false
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

  return (
    <>
      {orderData?.data &&
        <OrderDetailedElement order={orderData.data} key={orderId}/>
      }
    </>
  )
}
