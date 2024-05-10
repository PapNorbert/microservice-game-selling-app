import { FormEvent, useState } from 'react';
import { Form, FloatingLabel, Button, Alert, Nav, Container } from 'react-bootstrap';
import { useNavigate, useLocation } from 'react-router-dom';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { AxiosError, AxiosResponse } from 'axios';

import FormContainer from "../components/FromContainer";
import configuredAxios from '../axios/configuredAxios';
import { apiPrefix } from '../config/application.json';
import { formatKeyToMessage } from '../util/stringFormatting';
import decodeJwtAccesToken from '../util/decodeJwt';
import { ErrorResponseData } from '../interface/errorResponseInterface';
import useAuth from '../hooks/useAuth'

interface FormData {
  username: string;
  password: string;
  [key: string]: string
}

const emptyFormData = {
  username: '',
  password: ''
}


export default function Login() {
  const loginUrl = `/${apiPrefix}/login`;
  const { auth } = useAuth();
  const [form, setForm] = useState<FormData>(emptyFormData);
  const [errors, setErrors] = useState<FormData>(emptyFormData);
  const [submitError, setSubmitError] = useState<string | null>(null);
  const navigate = useNavigate();
  const { setAuth } = useAuth();
  const location = useLocation();
  const from = location.state?.from?.pathname || '/';
  const queryClient = useQueryClient();

  const { mutate } = useMutation({
    mutationFn: mutationFunction,
    onSuccess: handleSubmitSucces,
    onError: handleSubmitError,
  });

  function handleSubmit(e: FormEvent<HTMLFormElement>) {
    e.preventDefault();
    let noErrors = true;
    const newErrors: FormData = {
      username: '',
      password: ''
    };
    for (const [key, value] of Object.entries(form)) {
      if (!value || value === '') {
        newErrors[key] = `Please enter your ${formatKeyToMessage(key)}`;
        noErrors = false;
      }
    }

    setErrors(newErrors);
    if (noErrors) {
      mutate(form); // send the login request to server
    }
  }


  function mutationFunction(data: FormData) {
    return configuredAxios.post(loginUrl, data);
  }

  function handleSubmitSucces(response: AxiosResponse) {
    setAuth(decodeJwtAccesToken(response.data.token));
    setField('password', '');
    setErrors({ ...errors, password: '' });
    queryClient.invalidateQueries({ queryKey: ['savedAnnouncementsListShort', 'savedAnnouncementsListShort'] });
    navigate(from, { replace: true });
  }

  function handleSubmitError(error: AxiosError<ErrorResponseData>) {
    if (error.message === 'Network Error') {
      setSubmitError('Error connecting to the server')
    } else {
      setSubmitError(error.response?.data.errorMessage || 'Incorrect username or password');
    }
    setField('password', '');
    setErrors({ ...errors, password: '' });
  }

  function setField(field: string, value: string) {
    const newForm = { ...form, [field]: value }
    setForm(newForm); // only changes value of the selected field
    let newErrors = { ...errors }
    if (!value || value === '') {
      newErrors = { ...errors, [field]: `Please enter your ${formatKeyToMessage(field)}` }
    } else if (errors[field] !== '') {
      newErrors = { ...errors, [field]: '' }
    }
    setErrors(newErrors);
  }


  if (auth.logged_in) {
    return (
      <FormContainer>
        <h2 className='text-center'>Login</h2>
        <h4 className='text-center mt-5'>You are logged in! Logout first.</h4>
      </FormContainer>
    )
  }


  return (
    <FormContainer>
      <Form className='justify-content-md-center mt-5 col-md-6 offset-md-3' onSubmit={handleSubmit} >
        <h2 className='text-center'>Login</h2>

        <FloatingLabel controlId='floatingUsernameInput'
          label='Username' className='mb-3' >
          <Form.Control type='text' placeholder='Username'
            value={form.username} isInvalid={!!errors.username} autoComplete='off'
            onChange={e => { setField('username', e.target.value) }} />
          <Form.Control.Feedback type='invalid'>
            {errors['username']}
          </Form.Control.Feedback>
        </FloatingLabel>

        <FloatingLabel controlId='floatingPassword'
          label='Password' className='mb-3' >
          <Form.Control type='password' placeholder='Password'
            value={form.password} isInvalid={!!errors.password} autoComplete='off'
            onChange={e => { setField('password', e.target.value) }} />
          <Form.Control.Feedback type='invalid'>
            {errors['password']}
          </Form.Control.Feedback>
        </FloatingLabel>


        <Alert key='danger' variant='danger' show={submitError !== null} onClose={() => setSubmitError(null)} dismissible>
          {submitError}
        </Alert>
        <Button type='submit' variant='secondary' className='col-md-6 offset-md-3 '>
          Login
        </Button>
        <br />
        <Container className='text-center text-muted mt-2'>
          Don't have an account? Register now!
        </Container>
        <Nav variant='pills' activeKey='Login' className='mt-2'>
          <Nav.Link eventKey='Login' onClick={() => { navigate('/register') }} className='col-md-6 offset-md-3 text-center '>
            Register
          </Nav.Link>
        </Nav>

      </Form>
    </FormContainer>
  )
}