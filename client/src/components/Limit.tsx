import { useContext, useEffect } from 'react';
import { Col, Form, Row } from 'react-bootstrap'

import { SearchContext } from '../context/SearchContextProvider';

export default function Limit() {
  const { limit, setLimit, setPage } = useContext(SearchContext);

  useEffect(() => {
    setPage('1');
  }, [limit])

  return (
    <Row className="mb-5">
      <Col lg={{ span: 2 }} >
        <Form.Text as='h4' >
          Results per page:
        </Form.Text>
      </Col>
      <Col lg={{ span: 1 }}>
        <Form.Select value={limit} onChange={(e) => { setLimit(e.target.value); }}>
          <option value='5'>5</option>
          <option value='10'>10</option>
          <option value='15'>15</option>
          <option value='30'>30</option>
        </Form.Select>
      </Col>
    </Row>
  )
}
