import { FormEvent, useState } from 'react';
import { Form, FloatingLabel, Button, Alert, Nav, Row, Col } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import { useMutation } from '@tanstack/react-query';
import { AxiosError } from 'axios';

import FormContainer from "../components/FromContainer";
import configuredAxios from '../axios/configuredAxios';
import { apiPrefix } from '../config/application.json';
import { formatKeyToMessage } from '../util/stringFormatting';
import { ErrorResponseData } from '../interface/errorResponseInterface';
import useAuth from '../hooks/useAuth';

interface FormData {
  firstName: string;
  lastName: string;
  username: string;
  address: string;
  password: string;
  passwd_confirm: string;
  [key: string]: string
}

const emptyFormData = {
  firstName: '',
  lastName: '',
  username: '',
  address: '',
  password: '',
  passwd_confirm: ''
}

export default function Register() {
  const registerUrl = `/${apiPrefix}/register`;
  const { auth } = useAuth();
  const [form, setForm] = useState<FormData>(emptyFormData);
  const [errors, setErrors] = useState<FormData>(emptyFormData);
  const [succesfullReg, setSuccesfullReg] = useState<boolean>(false);
  const [submitError, setSubmitError] = useState<string | null>(null);
  const navigate = useNavigate();
  const { mutate } = useMutation({
    mutationFn: mutationFunction,
    onSuccess: handleSubmitSucces,
    onError: handleSubmitError,
  });


  function handleSubmit(e: FormEvent<HTMLFormElement>) {
    e.preventDefault();
    let noErrors = true;
    const newErrors: FormData = {
      firstName: '',
      lastName: '',
      username: '',
      address: '',
      password: '',
      passwd_confirm: ''
    };

    for (const [key, value] of Object.entries(form)) {
      // check the fields are not empty and the passwords match
      if (key === 'passwd_confirm') {
        if (!value || value === '') {
          newErrors[key] = 'Please confirm you Password';
          noErrors = false;
        }
        if (value !== form['password']) {
          newErrors[key] = 'The given Passwords must match';
          noErrors = false;
        }
      } else {
        if (!value || value === '') {
          newErrors[key] = `Please enter your ${formatKeyToMessage(key)}`;
          noErrors = false;
        }
      }
    }

    setErrors(newErrors);
    if (noErrors) {
      // if no errors send the registration request
      mutate(form);
    }
  }

  function mutationFunction(data: FormData) {
    return configuredAxios.post(registerUrl, data);
  }

  function handleSubmitSucces() {
    setForm(emptyFormData);
    setSuccesfullReg(true);
    navigate('/login');
  }

  function handleSubmitError(error: AxiosError<ErrorResponseData>) {
    setSuccesfullReg(false);
    if (error.message === 'Network Error') {
      setSubmitError('Error connecting to the server')
    } else {
      setSubmitError(error.response?.data.errorMessage || 'Error during registration');
    }
  }

  function setField(field: string, value: string) {
    const newForm = { ...form, [field]: value }
    setForm(newForm); // only changes value of the selected field
    let newErrors = { ...errors }
    if (!value || value === '') {
      let newErrorMessage = `Please enter your ${formatKeyToMessage(field)}`;
      if (field === 'passwd_confirm') {
        newErrorMessage = 'Please confirm you Password';
      }
      newErrors = { ...errors, [field]: newErrorMessage }
    } else if (errors[field] !== '') {
      newErrors = { ...errors, [field]: '' }
    }
    setErrors(newErrors);
  }

  if (auth.logged_in) {
    return (
      <FormContainer>
        <h2 className='text-center'>Registration</h2>
        <h4 className='text-center mt-5'>You are logged in! Logout first.</h4>
      </FormContainer>
    )
  }

  return (
    <FormContainer>
      <Form className='justify-content-md-center mt-5' onSubmit={handleSubmit} >
        <h2 className='text-center'>Registration</h2>
        <Row>
          <Col>
            <FloatingLabel controlId='floatingFirstNameInput'
              label='First Name' className='mb-3' >
              <Form.Control type='text' placeholder='First Name'
                value={form.firstName} isInvalid={!!errors.firstName} autoComplete='off'
                onChange={e => { setField('firstName', e.target.value) }} />
              <Form.Control.Feedback type='invalid'>
                {errors['firstName']}
              </Form.Control.Feedback>
            </FloatingLabel>
          </Col>

          <Col>
            <FloatingLabel controlId='floatingLastNameInput'
              label='Last Name' className='mb-3' >
              <Form.Control type='text' placeholder='Last Name'
                value={form.lastName} isInvalid={!!errors.lastName} autoComplete='off'
                onChange={e => { setField('lastName', e.target.value) }} />
              <Form.Control.Feedback type='invalid'>
                {errors['lastName']}
              </Form.Control.Feedback>
            </FloatingLabel>
          </Col>
        </Row>



        <FloatingLabel controlId='floatingUsernameInput'
          label='Username' className='mb-3' >
          <Form.Control type='text' placeholder='Username'
            value={form.username} isInvalid={!!errors.username} autoComplete='off'
            onChange={e => { setField('username', e.target.value) }} />
          <Form.Control.Feedback type='invalid'>
            {errors['username']}
          </Form.Control.Feedback>
        </FloatingLabel>

        <span className='address'>
          <Form.Label>Address</Form.Label>
          <Form.Control as='textarea' rows={1}
            placeholder='Address'
            value={form.address} isInvalid={!!errors.address} autoComplete='off'
            onChange={e => { setField('address', e.target.value) }} />
          <Form.Text className='text-muted'>
            Your address used for orders
          </Form.Text>
          <Form.Control.Feedback type='invalid' >
            {errors['address']}
          </Form.Control.Feedback>
        </span>

        <FloatingLabel controlId='floatingPassword'
          label='Password' className='mb-3 mt-3' >
          <Form.Control type='password' placeholder='Password'
            value={form.password} isInvalid={!!errors.password} autoComplete='off'
            onChange={e => { setField('password', e.target.value) }} />
          <Form.Control.Feedback type='invalid'>
            {errors['password']}
          </Form.Control.Feedback>
        </FloatingLabel>

        <FloatingLabel controlId='floatingPasswordConfirm'
          label='Confirm Password' className='mb-3'>
          <Form.Control type='password' placeholder='Confirm Password'
            value={form.passwd_confirm} isInvalid={!!errors.passwd_confirm} autoComplete='off'
            onChange={e => { setField('passwd_confirm', e.target.value) }} />
          <Form.Control.Feedback type='invalid'>
            {errors['passwd_confirm']}
          </Form.Control.Feedback>
        </FloatingLabel>

        <Alert key='danger' variant='danger' show={submitError !== null} onClose={() => setSubmitError(null)} dismissible >
          {submitError}
        </Alert>
        <Alert key='success' variant='success' show={succesfullReg} onClose={() => setSuccesfullReg(false)} dismissible >
          Succesfull registration
        </Alert>

        <Button type='submit' variant='secondary' className='col-md-6 offset-md-3'>
          Register
        </Button>
        <Nav variant='pills' activeKey='Login' className='mt-2'>
          <Nav.Link eventKey='Login' onClick={() => { navigate('/login') }} className='col-md-6 offset-md-3 text-center mb-5'>
            Login
          </Nav.Link>
        </Nav>


      </Form>
    </FormContainer>
  )
}