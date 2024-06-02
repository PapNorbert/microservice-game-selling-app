import { useContext, useEffect, useState } from "react";
import { useQuery } from "@tanstack/react-query";
import { AxiosError, AxiosResponse } from "axios";
import { Container } from "react-bootstrap";
import { Navigate, useLocation } from "react-router-dom";
import { useSubscription } from "react-stomp-hooks";

import { SearchContext } from "../../context/SearchContextProvider";
import {
  apiPrefix, limitQuerryParamDefault, limitQuerryParamName,
  pageQuerryParamDefault, pageQuerryParamName,
} from '../../config/application.json';
import configuredAxios from "../../axios/configuredAxios";
import PaginationElement from "../../components/PaginationElement";
import Limit from "../../components/Limit";
import useAuth from "../../hooks/useAuth";
import { User, UsersChattedWith } from "../../interface/UsersChattedWith";
import UserElement from "../../components/UserElement";
import ChatBoxComponent from "../../components/ChatBoxComponent";


export default function MessagesAll() {
  const { auth } = useAuth();
  const [usersChattedWithUrl, setUsersChattedWithUrl] = useState<string>(`/${apiPrefix}/chats/users`);
  const { limit, page } = useContext(SearchContext);
  const location = useLocation();
  const [selectedUser, setSelectedUser] = useState<User | null>(null);
  const [chattedWithData, setChattedWithData] = useState<UsersChattedWith>();

  const { isError, error, isLoading } =
    useQuery<AxiosResponse, AxiosError>({
      queryKey: ["usersChattedWith", usersChattedWithUrl],
      queryFn: queryFunction,
      retry: false,
    });

  useSubscription(`/queue/chats/${auth.userId}`, (message) => {
    // queue format:  /queue/chats/{requestUserId}
    const data: UsersChattedWith = JSON.parse(message.body);
    setChattedWithData(data);
  });

  useEffect(() => {
    if (!auth.userId) {
      return;
    }
    const queryParams = new URLSearchParams();

    if (limit !== limitQuerryParamDefault) {
      queryParams.set(limitQuerryParamName, limit);
    }
    if (page !== pageQuerryParamDefault) {
      queryParams.set(pageQuerryParamName, page);
    }

    setUsersChattedWithUrl(`/${apiPrefix}/chats/users?${queryParams.toString()}`)
  }, [limit, page, auth.userId
  ]);

  function queryFunction() {
    return configuredAxios.get(usersChattedWithUrl);
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

  return (chattedWithData ?
    <>
      <h1>Messages</h1>
      <Container>
        <h3>Found {chattedWithData.pagination.totalCount} results</h3>
        <Limit />
        {
          chattedWithData.users.length > 0 ? (
            <Container>
              {chattedWithData.users.map(currentElement => (
                <Container className="clickable" onClick={() => setSelectedUser(currentElement)}
                  key={`container${currentElement.userId}`}>
                  <UserElement user={currentElement} key={currentElement.userId} />
                </Container>
              ))}
              < PaginationElement totalPages={chattedWithData.pagination.totalPages} />
              {
                selectedUser !== null &&
                <ChatBoxComponent receiverId={selectedUser.userId} receiverUsername={selectedUser.username} />
              }
            </Container>
          )
            :
            <h3>No Messages found!</h3>
        }
      </Container>
    </>
    :
    <>
      <h1>Messages</h1>
      <Container className="fst-italic fs-3">Loading users...</Container>
    </>
  )
}
