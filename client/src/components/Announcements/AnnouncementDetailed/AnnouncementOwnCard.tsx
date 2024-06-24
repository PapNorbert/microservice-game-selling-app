import { Truck } from "react-bootstrap-icons"
import { useState } from "react"
import { Alert, Button, Card, Col, Container, FloatingLabel, Form, OverlayTrigger, Popover, Row, Stack } from "react-bootstrap"
import { useMutation } from "@tanstack/react-query"
import { AxiosError } from "axios"

import { AnnouncementDetailedResponse } from "../../../interface/Announcements/announcementDetailedInterface"
import useAuth from "../../../hooks/useAuth"
import { dateFormatOptions } from "../../../util/dateOptions"
import { convertConsoleTypeName } from "../../../util/consoleTypeNameConversion"
import configuredAxios from "../../../axios/configuredAxios"
import { ErrorResponseData } from "../../../interface/errorResponseInterface"
import { apiPrefix } from '../../../config/application.json'
import { formatKeyToMessage } from "../../../util/stringFormatting"

interface PropType {
  announcement: AnnouncementDetailedResponse
}

interface AnnouncementUpdateForm {
  price: number;
  sold: boolean;
  newDisc: boolean;
  transportPaidBySeller: boolean;
  description: string;
  title: string;
  [key: string]: string | boolean | number | undefined
}

interface UpdateFormErrors {
  price: string;
  description: string;
  title: string;
  [key: string]: string;
}

const emptyErrors: UpdateFormErrors = {
  price: '',
  description: '',
  title: ''
}

export default function AnnouncementOwnCard({ announcement }: PropType) {
  const { auth } = useAuth();
  const [error, setError] = useState<string>('');
  const [deleted, setDeleted] = useState<boolean>(false);
  const announcmentURL = `/${apiPrefix}/announcements/${announcement.announcementId}`;
  const [editing, setEditing] = useState<boolean>(false);
  const { mutate: mutateDelete } = useMutation({
    mutationFn: deleteMutationFunction,
    onSuccess: handleDeleteSubmitSucces,
    onError: handleSubmitError,
  });

  const originalEditableValues: AnnouncementUpdateForm = {
    price: announcement.price,
    sold: announcement.sold,
    transportPaidBySeller: announcement.transportPaidBySeller,
    newDisc: announcement.newDisc,
    description: announcement.description,
    title: announcement.title
  }
  const [savedEditableValues, setSavedEditableValues] = useState<AnnouncementUpdateForm>(originalEditableValues);
  // savedEditableValues stores the last value of the announcement, updated after succestful put request
  const [updateForm, setUpdateForm] = useState<AnnouncementUpdateForm>(savedEditableValues);
  // stores the values during editing
  const [formErrors, setFormErrors] = useState<UpdateFormErrors>(emptyErrors);
  const { mutate: mutatePut } = useMutation({
    mutationFn: putMutationFunction,
    onSuccess: handlePutSubmitSucces,
    onError: handleSubmitError,
  });

  function handleDeleteButtonClicked() {
    if (auth.logged_in && auth.userId === announcement.sellerId) {
      mutateDelete();
    } else {
      setError('Error: Cannot delete announcement of another user!')
    }
  }

  function deleteMutationFunction() {
    return configuredAxios.delete(announcmentURL);
  }

  function putMutationFunction(data: AnnouncementUpdateForm) {
    return configuredAxios.put(announcmentURL, data);
  }

  function handleDeleteSubmitSucces() {
    setDeleted(true);
  }

  function handlePutSubmitSucces() {
    setEditing(false);
    setSavedEditableValues(updateForm);
  }

  function handleSubmitError(error: AxiosError<ErrorResponseData>) {
    if (error.message === 'Network Error') {
      setError('Error connecting to the server')
    } else {
      setError(error.response?.data.errorMessage || 'Error processing the request');
    }
  }

  function setFieldUpdateForm(field: string, value: string | boolean) {
    const newForm = { ...updateForm, [field]: value }
    setUpdateForm(newForm);
    if (field !== 'transportPaidBySeller' && field !== 'newDisc') {
      let newErrors = { ...formErrors }
      if (!value || value === '') {
        newErrors = { ...formErrors, [field]: `Please enter a ${formatKeyToMessage(field)}` }
      } else if (formErrors[field] !== '') {
        newErrors = { ...formErrors, [field]: '' }
      }
      if (field === 'price' && typeof value === 'string' && parseInt(value) < 0) {
        newErrors = { ...formErrors, [field]: 'Price cannot be a negative number' }
      }
      setFormErrors(newErrors);
    }
  }


  function handleEditSave() {
    let noErrors = true;
    let newErrors = { ...formErrors }
    if (!updateForm.description && updateForm.description === '') {
      newErrors = { ...formErrors, ['description']: `Please enter a Description for the announcement` }
      noErrors = false;
    }
    if (!updateForm.title && updateForm.title === '') {
      newErrors = { ...formErrors, ['title']: `Please enter a Title` }
      noErrors = false;
    }
    if (updateForm.price < 0) {
      newErrors = { ...formErrors, ['price']: 'Price cannot be a negative number' }
      noErrors = false;
    }
    if (noErrors) {
      if (auth.logged_in && auth.userId === announcement.sellerId) {
        mutatePut(updateForm);
      } else {
        setError('Error: Cannot edit announcement of another user!')
      }
    } else {
      setFormErrors(newErrors);
    }
  }

  function handleEditCancel() {
    setEditing(false);
    setUpdateForm(savedEditableValues);
    setFormErrors(emptyErrors);
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
          {editing ?
            <>
              <FloatingLabel
                label='Title' className='mb-2 mt-2' >
                <Form.Control type='text' placeholder='Announcement title' className="pt-5 pb-4"
                  value={updateForm.title} isInvalid={!!formErrors.title} autoComplete='off'
                  onChange={e => { setFieldUpdateForm('title', e.target.value) }} />
                <Form.Control.Feedback type='invalid'>
                  {formErrors['title']}
                </Form.Control.Feedback>
              </FloatingLabel>
            </>
            :
            savedEditableValues.title
          }

          {!announcement.sold &&
            <>
              <OverlayTrigger trigger='click' placement='bottom' rootClose={true}
                overlay={popover}
              >
                <span className='float-end'>
                  <Button className='btn btn-orange-dark mx-2'  >
                    Delete
                  </Button>
                </span>
              </OverlayTrigger>
              <span className='float-end'>
                <Button className='btn btn-orange mx-2' onClick={() => setEditing(true)} disabled={editing}>
                  Edit
                </Button>
              </span>
            </>
          }
        </Card.Header>
        <Card.Body>
          <Row key={`${announcement.announcementId}_creation_date`}>
            Posted at: {new Date(announcement.creationDate).toLocaleDateString('ro-RO', dateFormatOptions)}
          </Row>
          {editing ?
            <Row key={`${announcement.announcementId}_price_edit`} className="fw-bold fs-5 mb-2 mt-2" >
              <Col md={6}>
                <FloatingLabel
                  label='Price in lei' className='mb-3 mt-2' >
                  <Form.Control type='number' min={0} placeholder='Price'
                    value={updateForm.price} autoComplete='off' isInvalid={!!formErrors.price}
                    onChange={e => { setFieldUpdateForm('price', e.target.value) }} />
                </FloatingLabel>
              </Col>
            </Row>
            :
            <Row key={`${announcement.announcementId}_price`} className="fw-bold fs-5 mb-2 mt-2" >
              {savedEditableValues.price} Lei
            </Row>
          }
          <Row key={`${announcement.announcementId}_productName`} className="fs-5">
            Game Disc Name: {announcement.soldGameDisc.name}
          </Row>
          <Stack key={`${announcement.announcementId}_tags`} direction="horizontal" gap={3} className="mt-2">
            <span className="border rounded border-black p-1">
              Year:  {announcement.soldGameDisc.gameYear}
            </span>
            <span className="border rounded border-black p-1">
              Console:  {convertConsoleTypeName(announcement.soldGameDisc.type)}
            </span>
            {editing ?
              <Form.Check type='switch' label="It's a new game disc"
                name='newDisc' inline className='mx-3'
                checked={updateForm.newDisc} onChange={e => { setFieldUpdateForm('newDisc', e.target.checked) }} />
              :
              <span className="border rounded border-black p-1">
                Condition: {savedEditableValues.newDisc ? 'New' : 'Used'}
              </span>
            }
            {editing ?
              <Form.Check type='switch' label='Transport is paid by the seller'
                name='transportPaidBySeller' inline className='mx-3'
                checked={updateForm.transportPaidBySeller}
                onChange={e => { setFieldUpdateForm('transportPaidBySeller', e.target.checked) }} />
              :
              savedEditableValues.transportPaidBySeller &&
              <span className="border rounded border-black p-1">
                <Truck className="me-2 mb-1" />
                Transport paid by the seller
              </span>
            }
          </Stack>
          <Row className="fw-bold fs-5 mt-3 mb-1" >
            Description
          </Row>
          {editing ?
            <>
              <Form.Control as='textarea' rows={6}
                placeholder='Description'
                value={updateForm.description} isInvalid={!!formErrors.description} autoComplete='off'
                onChange={e => { setFieldUpdateForm('description', e.target.value) }} />
              <Form.Control.Feedback type='invalid' >
                {formErrors['description']}
              </Form.Control.Feedback>
            </>
            :
            <>
              {
                savedEditableValues.description.split('\n').map((textRow, i) => {
                  return (
                    <Row key={`desc_row_${i}`}>
                      {textRow}
                    </Row>
                  )
                })
              }
            </>
          }
          {editing &&
            <>
              <span className='float-end mt-3'>
                <Button className='btn btn-orange mx-2' onClick={handleEditSave} >
                  Save changes
                </Button>
              </span>
              <span className='float-end mt-3'>
                <Button className='btn btn-orange mx-2' onClick={handleEditCancel} >
                  Cancel
                </Button>
              </span>
            </>
          }
        </Card.Body>
      </Card >
      <Alert key='danger' variant='danger' show={error !== ''}
        onClose={() => setError('')} dismissible >
        {error}
      </Alert>
    </>
  )
}
