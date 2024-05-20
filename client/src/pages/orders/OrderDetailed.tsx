import { useQuery } from "@tanstack/react-query";
import { AxiosResponse } from "axios";
import { useParams } from "react-router-dom";

import configuredAxios from "../../axios/configuredAxios";
import {apiPrefix} from '../../config/application.json'
import { Order } from "../../interface/OrdersInterface";
import OrderDetailedElement from "../../components/Orders/OrderDetailedElement";

export default function OrderDetailed() {
  const { orderId } = useParams();
  const orderUrl = `/${apiPrefix}/orders/${orderId}`;

  const { data: orderData, isError, error, isLoading } = useQuery<AxiosResponse<Order>>({
    queryKey: ["orderDetailed", orderUrl],
    queryFn: queryFunction,
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
