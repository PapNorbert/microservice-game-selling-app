
import { Star, StarFill } from "react-bootstrap-icons"
import { useState } from "react"
import { Card, OverlayTrigger, Tooltip } from "react-bootstrap"
import { useMutation, useQueryClient } from "@tanstack/react-query"
import { AxiosError } from "axios"

import { AnnouncementDetailedResponse } from "../../interface/Announcements/announcementDetailedInterface"
import useAuth from "../../hooks/useAuth"
import configuredAxios from "../../axios/configuredAxios"
import { AnnouncementsSavesCreation } from "../../interface/announcementsSavesCreationInterface"
import { apiPrefix } from '../../config/application.json'
import { ErrorResponseData } from "../../interface/errorResponseInterface"

interface PropType {
  announcement: AnnouncementDetailedResponse
}

export default function AnnouncementDetailedLong({ announcement }: PropType) {
  const { auth } = useAuth();
  const [savedByUser, setSavedByUser] = useState<boolean>(announcement.savedByUser);
  const announcementsSavesUrl = `/${apiPrefix}/announcementsSaves`;
  const [error, setError] = useState<string>('');
  const queryClient = useQueryClient();
  const { mutate: mutateDelete } = useMutation({
    mutationFn: deleteMutationFunction,
    onSuccess: handleSubmitSucces,
    onError: handleSubmitError,
  });
  const { mutate: mutatePost } = useMutation({
    mutationFn: postMutationFunction,
    onSuccess: handleSubmitSucces,
    onError: handleSubmitError,
  });

  function onRemoveSavedClick() {
    if (auth.logged_in && auth.userId) {
      mutateDelete(
        `${announcementsSavesUrl}?announcementId=${announcement.announcementId}&userId=${auth.userId}`);
    }
  }

  function onSaveClick() {
    if (auth.logged_in && auth.userId) {
      mutatePost({
        announcementId: announcement.announcementId,
        userId: auth.userId
      });
    }

  }


  function deleteMutationFunction(url: string) {
    return configuredAxios.delete(url);
  }

  function postMutationFunction(data: AnnouncementsSavesCreation) {
    return configuredAxios.post(announcementsSavesUrl, data);
  }


  function handleSubmitSucces() {
    if (error) {
      setError('');
    }
    setSavedByUser(!savedByUser);
    queryClient.invalidateQueries({ queryKey: ['announcementsListShort'] });
  }

  function handleSubmitError(error: AxiosError<ErrorResponseData>) {
    setError(error.response?.data.errorMessage || 'Error creating Announcement');
  }



  return (
    <>
      <Card key={`container_${announcement.announcementId}`} className='mt-5 mb-3'>
        <Card.Header as='h5' key={`header_${announcement.announcementId}`} >
          {announcement.title}
          {
            auth.logged_in && (
              savedByUser ?
                <span className="clickable" onClick={onRemoveSavedClick}>
                  <OverlayTrigger placement="bottom"
                    overlay={
                      <Tooltip key="remove-saved-tooltip">Remove from saved</Tooltip>
                    }>
                    <StarFill className="float-end  mt-1" />
                  </OverlayTrigger>
                </span>
                :
                <span className="clickable" onClick={onSaveClick}>
                  <OverlayTrigger placement="bottom"
                    overlay={
                      <Tooltip key="remove-saved-tooltip">Save</Tooltip>
                    }>
                    <Star className="float-end  mt-1" />
                  </OverlayTrigger>
                </span>
            )
          }
        </Card.Header>
      </Card >
    </>
  )
}
