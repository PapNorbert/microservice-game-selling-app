import { useState, FormEvent } from 'react';
import { Form, FloatingLabel, Alert, Button, Container, Row, Nav, Col } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import { useMutation } from '@tanstack/react-query';
import { AxiosError, AxiosResponse } from 'axios';

import FormContainer from '../../components/FromContainer';
import useAuth from '../../hooks/useAuth';
import { formatKeyToMessage } from '../../util/stringFormatting';
import { GameDisc } from '../../interface/gameDiscInterface'
import configuredAxios from '../../axios/configuredAxios';
import { apiPrefix } from '../../config/application.json';
import { ErrorResponseData } from '../../interface/errorResponseInterface';



interface AnnouncementFormData {
  sellerId?: number;
  description: string;
  title: string;
  price: number | undefined;
  transportPaidBySeller: boolean;
  newDisc: boolean;
  [key: string]: string | boolean | number | undefined
}

interface GameDiscFormData {
  name: string;
  type: string;
  gameYear: number | undefined;
  [key: string]: string | number | undefined
}

interface FormErrors {
  description: string;
  title: string;
  name: string;
  price: string;
  gameYear: string;
  type: string;
  [key: string]: string;

}

const emptyAnnouncementFormData = {
  description: '',
  title: '',
  price: 0,
  transportPaidBySeller: false,
  newDisc: false
}

const currentYear = new Date().getFullYear();

const emptyGameDiscFormData = {
  name: '',
  type: 'PS',
  gameYear: currentYear
}

const emptyErrors: FormErrors = {
  description: '',
  title: '',
  price: '',
  name: '',
  type: '',
  gameYear: ''
}

interface sendFormType {
  sellerId?: number;
  description: string;
  title: string;
  price: number | undefined;
  transportPaidBySeller: boolean;
  newDisc: boolean;
  soldGameDisc: GameDisc;
}

export default function AnnouncementCreate() {
  const { auth } = useAuth();
  const [createdId, setCreatedId] = useState<number | undefined>(undefined);
  const [announcementForm, setAnnouncementForm] = useState<AnnouncementFormData>(emptyAnnouncementFormData);
  const [gameDiscForm, setGameDiscForm] = useState<GameDiscFormData>(emptyGameDiscFormData);
  const [errors, setErrors] = useState<FormErrors>(emptyErrors);
  const [submitError, setSubmitError] = useState<string | null>(null);
  const [succesfullCreated, setSuccesfullCreated] = useState<boolean>(false);
  const navigate = useNavigate();
  const announcementCreateUrl = `/${apiPrefix}/announcements`;
  const { mutate } = useMutation({
    mutationFn: mutationFunction,
    onSuccess: handleSubmitSucces,
    onError: handleSubmitError,
  });


  function setFieldAnnouncement(field: string, value: string | boolean) {
    const newForm = { ...announcementForm, [field]: value }
    setAnnouncementForm(newForm);
    if (field !== 'transportPaidBySeller' && field !== 'newDisc') {
      let newErrors = { ...errors }
      if (!value || value === '') {
        newErrors = { ...errors, [field]: `Please enter a ${formatKeyToMessage(field)}` }
      } else if (errors[field] !== '') {
        newErrors = { ...errors, [field]: '' }
      }
      setErrors(newErrors);
    }
  }

  function setFieldGameDisc(field: string, value: string) {
    const newForm = { ...gameDiscForm, [field]: value }
    setGameDiscForm(newForm);
    let newErrors = { ...errors }
    if (!value || value === '') {
      newErrors = { ...errors, [field]: `Please enter a ${formatKeyToMessage(field)}` }
    } else if (errors[field] !== '') {
      newErrors = { ...errors, [field]: '' }
    }
    setErrors(newErrors);

  }

  function handleSubmit(e: FormEvent<HTMLFormElement>) {
    e.preventDefault();
    if (!auth.logged_in) {
      navigate('/login');
      return;
    }
    let noErrors = true;
    const newErrors: FormErrors = {
      description: '',
      title: '',
      price: '',
      name: '',
      type: '',
      gameYear: ''
    }
    for (const [key, value] of Object.entries(announcementForm)) {
      if (key !== 'transportPaidBySeller' && key !== 'newDisc') {
        if (key === 'price' && value === 0) {
          // ok
        } else if (!value || value === '') {
          newErrors[key] = `Please enter a ${formatKeyToMessage(key)}`;
          noErrors = false;
        }
      }
    }
    for (const [key, value] of Object.entries(gameDiscForm)) {
      if (key === 'type') {
      } else if (!value || value === '') {
        newErrors[key] = `Please enter a ${formatKeyToMessage(key)}`;
        noErrors = false;
      }
    }
    setErrors(newErrors);
    if (noErrors) {
      const form = { ...announcementForm, sellerId: auth.userId, soldGameDisc: gameDiscForm }
      mutate(form);
    }
  }


  function mutationFunction(data: sendFormType) {
    return configuredAxios.post(announcementCreateUrl, data);
  }

  function handleSubmitSucces(response: AxiosResponse) {
    setCreatedId(response.data.entityId);
    setSuccesfullCreated(true);
    setAnnouncementForm(emptyAnnouncementFormData);
    setGameDiscForm(emptyGameDiscFormData);
    setErrors(emptyErrors);
  }

  function handleSubmitError(error: AxiosError<ErrorResponseData>) {
    if (error.message === 'Network Error') {
      setSubmitError('Error connecting to the server')
    } else {
      setSubmitError(error.response?.data.errorMessage || 'Error creating Announcement');
    }
    setSuccesfullCreated(false);
  }

  return (
    <>
      <FormContainer >
        <Form className='justify-content-md-center mt-5' onSubmit={handleSubmit} >
          <h2 className="text-center mb-5">Create a Sale Announcement</h2>

          <FloatingLabel
            label='Title' className='mb-3 mt-2' >
            <Form.Control type='text' placeholder='Announcement title'
              value={announcementForm.title} isInvalid={!!errors.title} autoComplete='off'
              onChange={e => { setFieldAnnouncement('title', e.target.value) }} />
            <Form.Control.Feedback type='invalid'>
              {errors['title']}
            </Form.Control.Feedback>
          </FloatingLabel>

          <Form.Label className='mt-3'>
            Description
          </Form.Label>
          <Form.Control as='textarea' rows={6}
            placeholder='Description'
            value={announcementForm.description} isInvalid={!!errors.description} autoComplete='off'
            onChange={e => { setFieldAnnouncement('description', e.target.value) }} />
          <Form.Control.Feedback type='invalid' >
            {errors['description']}
          </Form.Control.Feedback>

          <Row className="justify-content-center mt-3">
            <Col md={6}>
              <FloatingLabel
                label='Price in lei' className='mb-3 mt-2' >
                <Form.Control type='number' min={0} placeholder='Price'
                  value={announcementForm.price} autoComplete='off' isInvalid={!!errors.price}
                  onChange={e => { setFieldAnnouncement('price', e.target.value) }} />
              </FloatingLabel>
            </Col>
          </Row>

          <Row className="justify-content-center mt-4">
            <Col md={5}>
              <Form.Check type='switch' label='Transport is paid by the seller'
                name='transportPaidBySeller' inline className='mx-3'
                checked={announcementForm.transportPaidBySeller}
                onChange={e => { setFieldAnnouncement('transportPaidBySeller', e.target.checked) }} />
            </Col>
            <Col md={5}>
              <Form.Check type='switch' label="It's a new game disc"
                name='newDisc' inline className='mx-3'
                checked={announcementForm.newDisc} onChange={e => { setFieldAnnouncement('newDisc', e.target.checked) }} />
            </Col>
          </Row>

          <FloatingLabel
            label='Game Name' className='mb-3 mt-3' >
            <Form.Control type='text' placeholder='Game Name'
              value={gameDiscForm.name} isInvalid={!!errors.name} autoComplete='off'
              onChange={e => { setFieldGameDisc('name', e.target.value) }} />
            <Form.Control.Feedback type='invalid'>
              {errors['name']}
            </Form.Control.Feedback>
          </FloatingLabel>

          <Row className="justify-content-center mt-4">
            <Col md={5}>
              <FloatingLabel
                label='Game Release Year' className='mb-3 mt-2' >
                <Form.Control type='number' min={1900} max={currentYear} placeholder='Price'
                  value={gameDiscForm.gameYear} autoComplete='off' isInvalid={!!errors.gameYear}
                  onChange={e => { setFieldGameDisc('gameYear', e.target.value) }} />
              </FloatingLabel>
              <Form.Control.Feedback type='invalid' >
                {errors['gameYear']}
              </Form.Control.Feedback>
            </Col>
            <Col md={5}>
              <FloatingLabel
                label='Game type' className='mb-3 mt-2' >
                <Form.Select value={gameDiscForm.type} isInvalid={!!errors.type}
                  onChange={e => { setFieldGameDisc('type', e.target.value) }}>
                  <option key='PS' value='PS'>PS</option>
                  <option key='XBOX' value='XBOX'>XBOX</option>
                  <option key='SWITCH' value='SWITCH'>SWITCH</option>
                </Form.Select>
              </FloatingLabel>
              <Form.Control.Feedback type='invalid' >
                {errors['type']}
              </Form.Control.Feedback>
            </Col>
          </Row>


          <Alert key='danger' variant='danger' show={submitError !== null} className='mt-3'
            onClose={() => setSubmitError(null)} dismissible >
            {submitError}
          </Alert>
          <Alert key='success' variant='success' show={succesfullCreated} className='mt-3'
            onClose={() => setSuccesfullCreated(false)} dismissible >
            <Container>
              <Row>
                Sale Announcement Succesfully Created!
              </Row>
              <Row className='mx-3'>
                <Nav.Link onClick={() => { navigate(`/announcements/${createdId}`) }}>
                  Go to page
                </Nav.Link>
              </Row>
            </Container>
          </Alert>

          <Button type='submit' variant='secondary' className='col-md-6 offset-md-3 mb-5 mt-4'>
            Create
          </Button>

        </Form>

      </FormContainer >
    </>
  )
}
