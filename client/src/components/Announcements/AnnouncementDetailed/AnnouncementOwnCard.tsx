import { Truck } from "react-bootstrap-icons"
import { useState } from "react"
import { Alert, Button, Card, Container, OverlayTrigger, Popover, Row, Stack } from "react-bootstrap"
import { useMutation } from "@tanstack/react-query"
import { AxiosError } from "axios"

import { AnnouncementDetailedResponse } from "../../../interface/Announcements/announcementDetailedInterface"
import useAuth from "../../../hooks/useAuth"
import { dateFormatOptions } from "../../../util/dateOptions"
import { convertConsoleTypeName } from "../../../util/consoleTypeNameConversion"
import configuredAxios from "../../../axios/configuredAxios"
import { ErrorResponseData } from "../../../interface/errorResponseInterface"
import { apiPrefix } from '../../../config/application.json'

interface PropType {
  announcement: AnnouncementDetailedResponse
}

export default function AnnouncementOwnCard({ announcement }: PropType) {
  const { auth } = useAuth();
  const [error, setError] = useState<string>('');
  const [deleted, setDeleted] = useState<boolean>(false);
  const announcmentURL = `/${apiPrefix}/announcements/${announcement.announcementId}`;

  const { mutate: mutateDelete } = useMutation({
    mutationFn: deleteMutationFunction,
    onSuccess: handleDeleteSubmitSucces,
    onError: handleSubmitError,
  });


  function handleDeleteButtonClicked() {
    if (auth.logged_in && auth.userId === announcement.sellerId) {
      mutateDelete(announcmentURL);
    } else {
      setError('Error: Cannot delete announcement of another user!')
    }
  }

  function deleteMutationFunction(url: string) {
    return configuredAxios.delete(url);
  }

  function handleDeleteSubmitSucces() {
    setDeleted(true);
  }

  function handleSubmitError(error: AxiosError<ErrorResponseData>) {
    if (error.message === 'Network Error') {
      setError('Error connecting to the server')
    } else {
      setError(error.response?.data.errorMessage || 'Error processing the request');
    }
  }


  const popover = (
    <Popover>
      <Popover.Header as='h3' className='delete-popover-header'>
        Delete Announcement
      </Popover.Header>
      <Popover.Body>
        Are you sure you want to delete this announcement?
        <br></br>
        <Button variant='light' className='mx-1 border border-2 my-2 float-end'
          onClick={handleDeleteButtonClicked} >
          Delete
        </Button>
        <Button variant='light' className='mx-1 border border-2 my-2 float-end'
          onClick={() => document.body.click()} >
          Cancel
        </Button>
      </Popover.Body>
    </Popover>
  )

  if (deleted) {
    return (
      <Container className='text-center mt-5 w-50' >
        <Alert variant="success">
          <Alert.Heading>
            Announcement Deleted Succesfully
          </Alert.Heading>
        </Alert>
      </Container>
    )
  }

  return (
    <>
      <Card key={`container_${announcement.announcementId}_details`} className='mt-5 mb-3'>
        <Card.Header as='h4' key={`header_${announcement.announcementId}`} >
          {announcement.title}

          <OverlayTrigger trigger='click' placement='bottom' rootClose={true}
            overlay={popover}
          >
            <span className='float-end'>
              <Button className='btn btn-orange-dark mx-2'  >
                Delete
              </Button>
            </span>
          </OverlayTrigger>


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
      <Alert key='danger' variant='danger' show={error !== ''}
        onClose={() => setError('')} dismissible >
        {error}
      </Alert>
    </>
  )
}
