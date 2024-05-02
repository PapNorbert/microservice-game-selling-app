import { useContext, useState } from 'react';
import { Form, Row, Col, Button, InputGroup } from 'react-bootstrap';
import { Search } from 'react-bootstrap-icons';

import { SearchContext } from '../context/SearchContextProvider';
import { useNavigate } from 'react-router-dom';
import {
  limitQuerryParamDefault, limitQuerryParamName,
  pageQuerryParamDefault, pageQuerryParamName,
  productNameParamDefault, productNameParamName,
  consoleTypeParamDefault, consoleTypeParamName
} from '../config/application.json'

export default function SearchBar() {
  const { selectedConsole, setSelectedConsole, productName, setProductName, limit, page } = useContext(SearchContext);
  const [currentSelectedConsole, setCurrentSelectedConsole] = useState<string>(selectedConsole);
  const [currentProductName, setCurrentProductName] = useState<string>(productName);
  const navigate = useNavigate();


  function handleSearch() {
    const queryParams = new URLSearchParams();
    // update search values
    if (selectedConsole !== currentSelectedConsole) {
      setSelectedConsole(currentSelectedConsole);
    }
    if (productName !== currentProductName) {
      setProductName(currentProductName);
    }
    // update querry parameters
    if (currentSelectedConsole !== consoleTypeParamDefault) {
      queryParams.set(consoleTypeParamName, currentSelectedConsole);
    }
    if (currentProductName !== productNameParamDefault) {
      queryParams.set(productNameParamName, currentProductName);
    }
    if (limit !== limitQuerryParamDefault) {
      queryParams.set(limitQuerryParamName, limit);
    }
    if (page !== pageQuerryParamDefault) {
      queryParams.set(pageQuerryParamName, limit);
    }

    navigate(`/announcements?${queryParams.toString()}`);
  }

  return (
    <Row className='mx-5 mt-5 mb-5'>
      <Col xs lg={{ span: 6, offset: 1 }}  >
        <InputGroup>
          <InputGroup.Text>
            <Search className='mr-5' />
          </InputGroup.Text>
          <Form.Control type='text' value={currentProductName} onChange={e => setCurrentProductName(e.target.value)}
            placeholder='Name of searched product' />
        </InputGroup>

      </Col>
      <Col xs lg={{ span: 3, offset: 0 }} >
        <Form.Select value={currentSelectedConsole} onChange={e => setCurrentSelectedConsole(e.target.value)}>
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