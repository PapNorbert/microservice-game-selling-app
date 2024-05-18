import { useContext, useEffect, useState } from "react";
import { keepPreviousData, useQuery } from "@tanstack/react-query";
import { AxiosError, AxiosResponse } from "axios";
import { Container } from "react-bootstrap";
import { Navigate, useLocation } from "react-router-dom";

import SearchBar from "../../layouts/SearchBar";
import { SearchContext } from "../../context/SearchContextProvider";
import {
  apiPrefix, chattedWithParamName,
  limitQuerryParamDefault, limitQuerryParamName, pageQuerryParamDefault, pageQuerryParamName,
} from '../../config/application.json';
import configuredAxios from "../../axios/configuredAxios";
import PaginationElement from "../../components/PaginationElement";
import Limit from "../../components/Limit";
import useAuth from "../../hooks/useAuth";
import { UsersChattedWith } from "../../interface/UsersChattedWith";
import UserChattedWith from "../../components/UserChattedWith";


export default function MessagesAll() {
  const { auth } = useAuth();
  const [usersChattedWithUrl, setUsersChattedWithUrl] = useState<string>(`/${apiPrefix}/users?${chattedWithParamName}=${auth.userId}`);
  const { limit, page } = useContext(SearchContext);
  const location = useLocation();

  const { data: chattedWithData, isError, error, isLoading } =
    useQuery<AxiosResponse<UsersChattedWith>, AxiosError>({
      queryKey: ["usersChattedWith", usersChattedWithUrl],
      queryFn: queryFunction,
      retry: false,
      placeholderData: keepPreviousData, // keeps the last succesful fetch as well beside current 
    });

  useEffect(() => {
    if (!auth.userId) {
      return;
    }
    const queryParams = new URLSearchParams();
    queryParams.set(chattedWithParamName, auth.userId.toString());

    if (limit !== limitQuerryParamDefault) {
      queryParams.set(limitQuerryParamName, limit);
    }
    if (page !== pageQuerryParamDefault) {
      queryParams.set(pageQuerryParamName, page);
    }

    setUsersChattedWithUrl(`/${apiPrefix}/users?${queryParams.toString()}`)
  }, [limit, page, auth.userId
  ]);

  function queryFunction() {
    return configuredAxios.get(usersChattedWithUrl);
  }

  if (isLoading) {
    return (
      <>
        <SearchBar showFilter={true} />
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
        <SearchBar showFilter={true} />
        <h2 className="error">{error.message || 'Sorry, there was an error!'}</h2>
      </>
    )
  }

  return (
    <>
      <h1>Messages</h1>
      <Container>
        <h3>Found {chattedWithData?.data.pagination.totalCount} results</h3>
        <Limit />
        {
          chattedWithData?.data &&
            chattedWithData.data.users.length > 0 ? (
            <Container>
              {chattedWithData?.data.users.map(currentElement => (
                <UserChattedWith user={currentElement} key={currentElement.userId} />
              ))}
              < PaginationElement totalPages={chattedWithData?.data.pagination.totalPages} />
            </Container>
          )
            :
            <h3>No Announcements found!</h3>
        }
      </Container>
    </>
  )
}
