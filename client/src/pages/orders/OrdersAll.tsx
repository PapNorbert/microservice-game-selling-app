import { useContext, useEffect, useState } from "react";

import useAuth from "../../hooks/useAuth";
import { SearchContext } from "../../context/SearchContextProvider";
import { Navigate, useLocation } from "react-router-dom";
import {
  apiPrefix, limitQuerryParamDefault, limitQuerryParamName,
  pageQuerryParamDefault, pageQuerryParamName, buyerParamName
} from '../../config/application.json'
import configuredAxios from "../../axios/configuredAxios";
import { keepPreviousData, useQuery } from "@tanstack/react-query";
import { AxiosError, AxiosResponse } from "axios";
import { Orders } from "../../interface/OrdersInterface";
import { Container } from "react-bootstrap";
import Limit from "../../components/Limit";
import PaginationElement from "../../components/PaginationElement";
import OrderListingElement from "../../components/Orders/OrderListingElement";



export default function OrdersAll() {
  const { auth } = useAuth();
  const [ordersUrl, setOrdershUrl] = useState<string>(`/${apiPrefix}/orders?${buyerParamName}=${auth.userId}`);
  const { limit, page } = useContext(SearchContext);
  const location = useLocation();


  const { data: ordersData, isError, error, isLoading } =
    useQuery<AxiosResponse<Orders>, AxiosError>({
      queryKey: ["orders", ordersUrl],
      queryFn: queryFunction,
      retry: false,
      placeholderData: keepPreviousData, // keeps the last succesful fetch as well beside current 
    });

  useEffect(() => {
    if (!auth.userId) {
      return;
    }
    const queryParams = new URLSearchParams();
    queryParams.set(buyerParamName, auth.userId.toString());

    if (limit !== limitQuerryParamDefault) {
      queryParams.set(limitQuerryParamName, limit);
    }
    if (page !== pageQuerryParamDefault) {
      queryParams.set(pageQuerryParamName, page);
    }

    setOrdershUrl(`/${apiPrefix}/orders?${queryParams.toString()}`)
  }, [limit, page, auth.userId
  ]);

  function queryFunction() {
    return configuredAxios.get(ordersUrl);
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
    return (
      <>
        <h2 className="error">{error.message || 'Sorry, there was an error!'}</h2>
      </>
    )
  }


  return (
    <>
      <h1>Orders</h1>
      <Container>
        <h3>Found {ordersData?.data.pagination.totalCount} results</h3>
        <Limit />
        {
          ordersData?.data &&
            ordersData.data.orders.length > 0 ? (
            <Container>
              {ordersData?.data.orders.map(currentElement => (
                <OrderListingElement order={currentElement} key={currentElement.orderId} />
              ))}
              < PaginationElement totalPages={ordersData?.data.pagination.totalPages} />
            </Container>
          )
            :
            <h3>No Orders found!</h3>
        }
      </Container>
    </>)
}
