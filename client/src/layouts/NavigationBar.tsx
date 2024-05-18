import { useNavigate } from 'react-router-dom'
import { Navbar, Nav, Container, NavDropdown, NavbarText } from 'react-bootstrap'

import useAuth from '../hooks/useAuth'
import configuredAxios from '../axios/configuredAxios';
import { apiPrefix } from '../config/application.json';
import { ChatFill, StarFill } from 'react-bootstrap-icons';
import { useQueryClient } from '@tanstack/react-query';


export default function Navigationbar() {
  const navigate = useNavigate();
  const { auth, setAuth } = useAuth();
  const queryClient = useQueryClient();

  function logout() {
    configuredAxios.get(`/${apiPrefix}/logout`)
      .then(() => {
        setAuth({ username: undefined, logged_in: false, role: undefined, userId: undefined });
        queryClient.invalidateQueries({ queryKey: ['savedAnnouncementsListShort'] });
        navigate('/');
      })
      .catch((err) => {
        console.log(err);
      })
  }


  return (
    <Navbar collapseOnSelect expand='lg' bg='customColor' sticky='top' className='px-5 pb-1 pt-1 mb-4' >
      <Container fluid className='mx-5 '>
        <Navbar.Brand className='ms-5 me-auto clickable' onClick={() => { navigate('/') }} >
          <img
            alt=""
            src="\src\assets\logo2.png"
            width="235"
            height="43"
            className="d-inline-block align-top"
          />
        </Navbar.Brand>

        <Navbar.Toggle aria-controls='navbar-nav' />
        <Navbar.Collapse id='navbar-nav'>
          <Nav className='me-5 ms-auto'>
            <Nav.Link className='me-4 mr-4 nav-text fw-bold' >
              <ChatFill className='me-1' />
              <NavbarText > Messages </NavbarText>
            </Nav.Link>
            <Nav.Link className='me-4 mr-4 nav-text fw-bold'
              onClick={() => { navigate('/announcements/saved') }}>
              <StarFill className='me-1' />
              <NavbarText> Saved </NavbarText>
            </Nav.Link>
            {auth.logged_in &&
              <Nav.Link className='me-4 mr-4 nav-text fw-bold'
                onClick={() => { navigate('/announcements/me') }}>
                My Announcements
              </Nav.Link>}
            {auth.logged_in &&
              <Nav.Link className='me-4 mr-4 nav-text fw-bold'
                onClick={() => { navigate('/announcements/create') }}>
                Create Sale Announcement
              </Nav.Link>}
          </Nav>
        </Navbar.Collapse>

        {!auth.logged_in &&
          <Nav className='ms-3'>
            <Nav.Link className='me-4 nav-text border border-dark rounded-5 px-3 auth-button'
              onClick={() => { navigate('/login') }}>
              Login
            </Nav.Link>
            <Nav.Link className='me-3 nav-text border border-dark rounded-5 px-3 auth-button'
              onClick={() => { navigate('/register') }}>
              Create account
            </Nav.Link>
          </Nav>
        }
        {
          auth.logged_in &&
          <Nav className='ms-3'>
            <Navbar.Text className='ms-1 nav-text fw-bold'>
              Logged in as:
            </Navbar.Text>
            <NavDropdown title={auth.username} className='me-4 nav-text'>
              <NavDropdown.Item onClick={logout}>
                Logout
              </NavDropdown.Item>
            </NavDropdown>
          </Nav>
        }

      </Container>
    </Navbar>
  )
}