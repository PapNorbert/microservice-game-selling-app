import { Container, Row, Col } from 'react-bootstrap'
import { ChildrenProps } from '../interface/childrenPropsInterface'

export default function FormContainer({children} : ChildrenProps) {
  return (
    <Container>
      <Row className='justify-content-md-center'>
        <Col xs={12} md={6}>
          {children}
        </Col>
      </Row>
    </Container>
  )
}
