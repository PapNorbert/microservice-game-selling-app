import { Card, Col, Row } from "react-bootstrap";

import { User } from "../interface/UsersChattedWith"
import { dateFormatShortOptions } from "../util/dateOptions";

interface PropType {
  user: User;
}

export default function UserElement({ user }: PropType) {
  return (
    <Card className='mt-4 mb-3'>
      <Row>
        <Col xs lg={2}>
          User:
        </Col>
        <Col xs >
          {user.username}
        </Col>
      </Row>
      <Row className='fst-italic mt-1'>
        Registered since: {new Date(user.registrationDate).toLocaleDateString('ro-RO', dateFormatShortOptions)}
      </Row>
    </Card>
  )
}
