import { useParams } from "react-router-dom";
import { useState } from "react";
import { AxiosResponse } from "axios";
import { useQuery } from "@tanstack/react-query";


import SearchBar from "../../layouts/SearchBar";
import { apiPrefix } from '../../config/application.json'
import { AnnouncementDetailedResponse } from "../../interface/Announcements/announcementDetailedInterface";
import configuredAxios from "../../axios/configuredAxios";
import AnnouncementDetailedLong from "../../components/Announcements/AnnouncementDetailedLong";


export default function AnnouncementDetailed() {
  const { announcementId } = useParams();
  const [announcementUrl, setAnnouncemensUrl] = useState<string>(`/${apiPrefix}/announcements/${announcementId}`);

  const { data: announcementData, isError, error, isLoading } = useQuery<AxiosResponse<AnnouncementDetailedResponse>>({
    queryKey: ["announcementDetailed", announcementUrl],
    queryFn: queryFunction,
  });

  function queryFunction() {
    return configuredAxios.get(announcementUrl);
  }

  if (isLoading) {
    return (
      <>
        <SearchBar />
        <h2 className="text-center">Loading...</h2>
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
      {announcementData?.data &&
        <AnnouncementDetailedLong announcement={announcementData.data} key={announcementId}/>
      }
    </>
  )
}
