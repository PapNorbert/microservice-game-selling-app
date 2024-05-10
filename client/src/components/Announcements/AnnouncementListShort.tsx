import { useState } from "react";
import { Card, Row, OverlayTrigger, Tooltip, Col, Badge, Alert } from "react-bootstrap"
import { useNavigate } from "react-router-dom";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { AxiosError } from "axios";

import { AnnouncementShort } from "../../interface/Announcements/announcementsListShortInterface"
import { Star, StarFill, Truck } from "react-bootstrap-icons";
import { dateFormatOptions } from "../../util/dateOptions";
import { convertConsoleTypeName } from "../../util/consoleTypeNameConversion";
import useAuth from "../../hooks/useAuth";
import { AnnouncementsSavesCreation } from "../../interface/announcementsSavesCreationInterface"
import { apiPrefix } from '../../config/application.json'
import { ErrorResponseData } from "../../interface/errorResponseInterface"
import configuredAxios from "../../axios/configuredAxios";

interface PropType {
  announcement: AnnouncementShort
}

export default function AnnouncementListShort({ announcement }: PropType) {
  const navigate = useNavigate();
  const [savedByUser, setSavedByUser] = useState<boolean>(announcement.savedByUser);
  const { auth } = useAuth();
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
    queryClient.invalidateQueries({ queryKey: ['savedAnnouncementsListShort'] });
  }

  function handleSubmitError(error: AxiosError<ErrorResponseData>) {
    if (error.message === 'Network Error') {
      setError('Error connecting to the server')
    } else {
      setError(error.response?.data.errorMessage || 'Error creating Announcement');
    }
  }


  return (
    <>
      <Card key={`container_${announcement.announcementId}`} className='mt-4 mb-3'>
        <Card.Header as='h5' key={`header_${announcement.announcementId}`} >
          {announcement.title}
          {
            auth.logged_in && auth.userId !== announcement.sellerId && (
              savedByUser ?
                <span className="clickable" onClick={onRemoveSavedClick}>
                  <OverlayTrigger placement="bottom"
                    overlay={
                      <Tooltip key="remove-saved-tooltip">Remove from saved</Tooltip>
                    }>
                    <StarFill className="float-end mt-1" />
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
        <Alert key='danger' variant='danger' show={error !== ''}
          onClose={() => setError('')} dismissible className="mt-2">
          {error}
        </Alert>
        <Card.Body className="clickable"
          onClick={() => navigate(`/announcements/${announcement.announcementId}`)} >
          <Row key={`${announcement.announcementId}_price`} className="fw-bold fs-5 mb-1" >
            <Col key={`${announcement.announcementId}_newDistTag`} >
              <Badge bg="secondary">
                {announcement.newDisc ? 'New' : 'Used'}
              </Badge>
            </Col>
            <Col className="text-end">
              {announcement.transportPaidBySeller &&
                <OverlayTrigger placement="bottom"
                  overlay={
                    <Tooltip key="transport-tooltip">Transport paid by the seller</Tooltip>
                  }>
                  <Truck className="me-2 mb-1" />
                </OverlayTrigger>
              }
              {announcement.price} Lei
            </Col>
          </Row>
          <Row key={`${announcement.announcementId}_product`} className="fs-5">
            <Col xs lg={2}>
              Product name:
            </Col>
            <Col xs >
              {`${announcement.soldGameDiscName},   ${convertConsoleTypeName(announcement.soldGameDiscType)}`}
            </Col>
          </Row>

          <Row key={`${announcement.announcementId}_creation_date`} className="mt-2">
            Posted at: {new Date(announcement.creationDate).toLocaleDateString('ro-RO', dateFormatOptions)}
          </Row>
        </Card.Body>
      </Card>
    </>
  )
}
