import { useContext, useEffect, useState } from "react";
import { keepPreviousData, useQuery } from "@tanstack/react-query";
import { AxiosError, AxiosResponse } from "axios";
import { Container } from "react-bootstrap";
import { Navigate, useLocation } from "react-router-dom";

import SearchBar from "../../layouts/SearchBar";
import { SearchContext } from "../../context/SearchContextProvider";
import { apiPrefix } from '../../config/application.json';
import {
  limitQuerryParamDefault, limitQuerryParamName, pageQuerryParamDefault, pageQuerryParamName,
  productNameParamDefault, productNameParamName, consoleTypeParamDefault, consoleTypeParamName,
  transportPaidParamDefault, transportPaidParamName, productTypeParamDefault, productTypeParamName,
  priceMaxParamName, priceMinParamName
} from '../../config/application.json';
import configuredAxios from "../../axios/configuredAxios";
import { AnnouncementsListShort } from "../../interface/Announcements/announcementsListShortInterface";
import AnnouncementListShort from "../../components/Announcements/AnnouncementListShort";
import PaginationElement from "../../components/PaginationElement";
import Limit from "../../components/Limit";


export default function AnnouncementsAll() {
  const [announcementsUrl, setAnnouncementsUrl] = useState<string>(`/${apiPrefix}/announcements?`);
  const {
    selectedConsole, productName, limit, page,
    transportPaid, productType, priceMin, priceMax
  } = useContext(SearchContext);
  const location = useLocation();

  const { data: announcementsData, isError, error, isLoading } =
    useQuery<AxiosResponse<AnnouncementsListShort>, AxiosError>({
      queryKey: ["announcementsListShort", announcementsUrl],
      queryFn: queryFunction,
      retry: false,
      placeholderData: keepPreviousData, // keeps the last succesful fetch as well beside current 
    });


  useEffect(() => {
    const queryParams = new URLSearchParams();
    if (selectedConsole !== consoleTypeParamDefault) {
      queryParams.set(consoleTypeParamName, selectedConsole);
    }
    if (productName !== productNameParamDefault) {
      queryParams.set(productNameParamName, productName);
    }
    if (limit !== limitQuerryParamDefault) {
      queryParams.set(limitQuerryParamName, limit);
    }
    if (page !== pageQuerryParamDefault) {
      queryParams.set(pageQuerryParamName, page);
    }
    if (transportPaid !== transportPaidParamDefault) {
      queryParams.set(transportPaidParamName, transportPaid);
    }
    if (productType !== productTypeParamDefault) {
      queryParams.set(productTypeParamName, productType);
    }
    if (priceMin) {
      queryParams.set(priceMinParamName, priceMin);
    }
    if (priceMax) {
      queryParams.set(priceMaxParamName, priceMax);
    }

    setAnnouncementsUrl(`/${apiPrefix}/announcements?${queryParams.toString()}`)
  }, [selectedConsole, productName, limit, page, transportPaid, productType,
    priceMin, priceMax
  ]);

  function queryFunction() {
    return configuredAxios.get(announcementsUrl);
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
    if(error.response?.status === 401) {
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
      <SearchBar showFilter={true} />
      <Container>
        <h3>Found {announcementsData?.data.pagination.totalCount} results</h3>
        <Limit />
        {
          announcementsData?.data.announcements &&
            announcementsData.data.announcements.length > 0 ? (
            <Container>
              {announcementsData?.data.announcements.map(currentElement => (
                <AnnouncementListShort announcement={currentElement} key={currentElement.announcementId} />

              ))}
              < PaginationElement totalPages={announcementsData?.data.pagination.totalPages} />
            </Container>
          )
            :
            <h3>No Announcements found!</h3>
        }
      </Container>
    </>
  )
}
