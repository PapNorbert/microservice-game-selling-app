import { Container, Nav } from 'react-bootstrap'
import { useNavigate } from 'react-router-dom'

export default function UnauthorizedPage() {
  const navigate = useNavigate();


  return (
    <Container className='text-center'>
      <h2 className='error mt-5'>
        Unauthorized page accessed!
      </h2>
      <Container>
        <Nav.Link onClick={() => { navigate('/'); }} >
          <span className='fw-bold'>Home</span>
        </Nav.Link>
      </Container>
    </Container>
  )
}
