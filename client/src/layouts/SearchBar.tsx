import { useState } from 'react';

import { Form, Row, Col, Button, InputGroup } from 'react-bootstrap'
import { Search } from 'react-bootstrap-icons'

export default function SearchBar() {
  const [selectedConsole, setSelectedConsole] = useState<string>('ALL');
  const [productName, setProductName] = useState<string>('');

  function handleSearch() {

  }


  return (
    <Row className='mx-5 mt-5 mb-5'>
      <Col xs lg={{ span: 6, offset: 1 }}  >
        <InputGroup>
          <InputGroup.Text>
            <Search className='mr-5' />
          </InputGroup.Text>
          <Form.Control type='text' value={productName} onChange={e => setProductName(e.target.value)}
            placeholder='Name of searched product' />
        </InputGroup>

      </Col>
      <Col xs lg={{ span: 3, offset: 0 }} >
        <Form.Select value={selectedConsole} onChange={e => setSelectedConsole(e.target.value)}>
          <option value="ALL">All Consoles</option>
          <option value='XBOX'>Xbox Series</option>
          <option value='PS'>PlayStation Series</option>
          <option value='SWITCH'>Nintendo Switch Series</option>
        </Form.Select>
      </Col>
      <Col xs lg={{ span: 2, offset: 0 }} >
        <Button variant="secondary" onClick={handleSearch} >Search</Button>
      </Col>
    </Row>
  )
}