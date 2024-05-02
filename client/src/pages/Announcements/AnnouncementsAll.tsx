import { useContext, useEffect, useState } from "react";
import { keepPreviousData, useQuery } from "@tanstack/react-query";
import { AxiosResponse } from "axios";

import SearchBar from "../../layouts/SearchBar";
import { SearchContext } from "../../context/SearchContextProvider";
import { apiPrefix } from '../../config/application.json';
import {
  limitQuerryParamDefault, limitQuerryParamName,
  pageQuerryParamDefault, pageQuerryParamName,
  productNameParamDefault, productNameParamName,
  consoleTypeParamDefault, consoleTypeParamName
} from '../../config/application.json';
import configuredAxios from "../../axios/configuredAxios";
import { AnnouncementsListShort } from "../../interface/Announcements/announcementsListShortInterface";
import AnnouncementListShort from "../../components/Announcements/AnnouncementListShort";
import { Container } from "react-bootstrap";


export default function AnnouncementsAll() {
  const [announcementsUrl, setAnnouncementsUrl] = useState<string>(`/${apiPrefix}/announcements`);
  const { selectedConsole, productName, limit, page } = useContext(SearchContext);
  const { data: announcementsData, isError, error, isLoading } = useQuery<AxiosResponse<AnnouncementsListShort>>({
    queryKey: ["announcementsListShort", announcementsUrl],
    queryFn: queryFunction,
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
      queryParams.set(pageQuerryParamName, limit);
    }
    setAnnouncementsUrl(`/${apiPrefix}/announcements?${queryParams.toString()}`)
  }, [selectedConsole, productName, limit, page]);

  function queryFunction() {
    return configuredAxios.get(announcementsUrl);
  }

  if (isLoading) {
    return (
      <>
        <SearchBar />
        <h2 className="center">Loading...</h2>
      </>
    )
  }

  if (isError) {
    return (
      <>
        <SearchBar />
        <h2 className="error">{error.message || 'Sorry, there was an error!'}</h2>
      </>
    )
  }


  return (
    <>
      <SearchBar />
      <Container className="mx-5">
        <h3>Found {announcementsData?.data.pagination.totalCount} results</h3>
        {
          announcementsData?.data.announcements &&
            announcementsData.data.announcements.length > 0 ?
            announcementsData?.data.announcements.map(currentElement => {
              return (
                <AnnouncementListShort announcement={currentElement} key={currentElement.announcementId} />
              );
            })
            :
            <h3>No Announcements found!</h3>
        }
      </Container>

    </>
  )
}
