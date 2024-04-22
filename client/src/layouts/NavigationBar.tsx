import { useNavigate } from 'react-router-dom'
import { Navbar, Nav, Container, NavDropdown } from 'react-bootstrap'

import useAuth from '../hooks/useAuth'
import configuredAxios from '../axios/configuredAxios';
import { apiPrefix } from '../config/application.json';


export default function Navigationbar() {
  const navigate = useNavigate();
  const { auth, setAuth } = useAuth();


  function logout() {
    configuredAxios.get(`/${apiPrefix}/logout`)
      .then(() => {
        setAuth({ username: undefined, logged_in: false });
        navigate('/');

      })
      .catch((err) => {
        console.log(err);
      })
  }


  return (
    <Navbar collapseOnSelect expand='md' bg='customColor' sticky='top' className='px-5 pb-1 pt-1 mb-4' >
      <Container fluid className='mx-5 '>
        <Navbar.Brand href="/" className='ms-5 me-auto' >
        <img
              alt=""
              src="\src\assets\logo2.png"
              width="230"
              height="49"
              className="d-inline-block align-top"
            />
        </Navbar.Brand>

        <Nav className='me-5'>
          <Nav.Link className='me-4 mr-4 nav-text fw-bold' >
            Messages
          </Nav.Link>
          <Nav.Link className='me-4 mr-4 nav-text fw-bold' >
            Favourites
          </Nav.Link>
        </Nav>

        {!auth.logged_in &&
          <Nav className='me-3'>
            <Nav.Link className='me-4 nav-text border border-dark rounded-5 px-3 auth-button'
              onClick={() => { navigate('/login',) }}>
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
          <Nav className='me-3'>
            <Navbar.Text className='me-1 nav-text'>
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