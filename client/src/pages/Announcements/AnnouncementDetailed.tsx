import { useNavigate, useParams } from "react-router-dom";
import { useState } from "react";
import { AxiosError, AxiosResponse } from "axios";
import { useQuery } from "@tanstack/react-query";


import SearchBar from "../../layouts/SearchBar";
import { apiPrefix } from '../../config/application.json'
import { AnnouncementDetailedResponse } from "../../interface/Announcements/announcementDetailedInterface";
import configuredAxios from "../../axios/configuredAxios";
import AnnouncementDetailedLong from "../../components/Announcements/AnnouncementDetailed/AnnouncementDetailedLong";


export default function AnnouncementDetailed() {
  const { announcementId } = useParams();
  const [announcementUrl] = useState<string>(`/${apiPrefix}/announcements/${announcementId}`);
  const navigate = useNavigate();

  const { data: announcementData, isError, error, isLoading } = useQuery<AxiosResponse<AnnouncementDetailedResponse>, AxiosError>({
    queryKey: ["announcementDetailed", announcementUrl],
    queryFn: queryFunction,
    retry: false
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
    if (error.response?.status === 404) {
      return (
        <>
          <SearchBar />
          <h2 className="error">Announcement not found! </h2>
          <h3 className="clickable text-center fst-italic text-decoration-underline" onClick={() => navigate('/announcements')}>Announcements</h3>
        </>
      )
    }
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
        <AnnouncementDetailedLong announcement={announcementData.data} key={announcementId} />
      }
    </>
  )
}
