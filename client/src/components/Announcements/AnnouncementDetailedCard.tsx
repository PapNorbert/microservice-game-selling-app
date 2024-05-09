import { Star, StarFill, Truck } from "react-bootstrap-icons"
import { useState } from "react"
import { Badge, Card, Col, OverlayTrigger, Row, Stack, Tooltip } from "react-bootstrap"
import { useMutation, useQueryClient } from "@tanstack/react-query"
import { AxiosError } from "axios"

import { AnnouncementDetailedResponse } from "../../interface/Announcements/announcementDetailedInterface"
import useAuth from "../../hooks/useAuth"
import configuredAxios from "../../axios/configuredAxios"
import { AnnouncementsSavesCreation } from "../../interface/announcementsSavesCreationInterface"
import { apiPrefix } from '../../config/application.json'
import { ErrorResponseData } from "../../interface/errorResponseInterface"
import { dateFormatOptions } from "../../util/dateOptions"
import { convertConsoleTypeName } from "../../util/consoleTypeNameConversion"

interface PropType {
  announcement: AnnouncementDetailedResponse
}

export default function AnnouncementDetailedCard({ announcement }: PropType) {
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
    <Card key={`container_${announcement.announcementId}_details`} className='mt-5 mb-3'>
      <Card.Header as='h4' key={`header_${announcement.announcementId}`} >
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
      <Card.Body>
        <Row key={`${announcement.announcementId}_creation_date`}>
          Posted at: {new Date(announcement.creationDate).toLocaleDateString('ro-RO', dateFormatOptions)}
        </Row>
        <Row key={`${announcement.announcementId}_price`} className="fw-bold fs-5 mb-2 mt-2" >
          {announcement.price} Lei
        </Row>
        <Row key={`${announcement.announcementId}_productName`} className="fs-5">
          Game Disc Name: {announcement.soldGameDisc.name}
        </Row>
        <Stack key={`${announcement.announcementId}_tags`} direction="horizontal" gap={3} className="mt-2">
          <span className="border rounded border-black p-1">
            Year:  {announcement.soldGameDisc.gameYear}
          </span>
          <span className="border rounded border-black p-1">
            Condition: {announcement.newDisc ? 'New' : 'Used'}
          </span>
          <span className="border rounded border-black p-1">
            Console:  {convertConsoleTypeName(announcement.soldGameDisc.type)}
          </span>
          {announcement.transportPaidBySeller &&
            <span className="border rounded border-black p-1">
              <Truck className="me-2 mb-1" />
              Transport paid by the seller
            </span>
          }
        </Stack>
        <Row className="fw-bold fs-5 mt-3 mb-1" >
          Description
        </Row>
        <Row key={`${announcement.announcementId}_description`} className="" >
          {announcement.description} 
        </Row>
      </Card.Body>
    </Card >
  )
}
