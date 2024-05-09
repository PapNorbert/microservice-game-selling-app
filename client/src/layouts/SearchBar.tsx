import { useContext, useState } from 'react';
import { Form, Row, Col, Button, InputGroup } from 'react-bootstrap';
import { Search } from 'react-bootstrap-icons';

import { SearchContext } from '../context/SearchContextProvider';
import { useNavigate } from 'react-router-dom';
import {
  limitQuerryParamDefault, pageQuerryParamDefault, productNameParamDefault, consoleTypeParamDefault,
  productNameParamName, consoleTypeParamName, limitQuerryParamName,
  productTypeParamDefault, productTypeParamName, transportPaidParamDefault, transportPaidParamName,
  priceMaxParamName, priceMinParamName, datePostedParamDefault, datePostedParamName
} from '../config/application.json'
import FilterBar from './FilterBar';

interface PropType {
  showFilter?: boolean
}

export default function SearchBar({ showFilter = false }: PropType) {
  const {
    selectedConsole, setSelectedConsole, productName, setProductName,
    limit, transportPaid, setTransportPaid, productType, setProductType,
    setPriceMax, setPriceMin, priceMaxRef, priceMinRef, setPage,
    datePosted, setDatePosted
  } = useContext(SearchContext);
  const [currentSelectedConsole, setCurrentSelectedConsole] = useState<string>(selectedConsole);
  const [currentProductName, setCurrentProductName] = useState<string>(productName);
  const [currentTransportPaid, setCurrentTransportPaid] = useState<string>('ALL');
  const [currentProductType, setCurrentProductType] = useState<string>('ALL');
  const [currentDatePosted, setCurrentDatePosted] = useState<string>('ALL');
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
    if (transportPaid !== currentTransportPaid) {
      setTransportPaid(currentTransportPaid);
    }
    if (productType !== currentProductType) {
      setProductType(currentProductType);
    }
    if (priceMinRef?.current ) {
      setPriceMin(priceMinRef.current.value);
    }
    if (priceMaxRef?.current) {
      setPriceMax(priceMaxRef.current.value);
    }
    if (datePosted !== currentDatePosted) {
      setDatePosted(currentDatePosted);
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
    setPage(pageQuerryParamDefault);
    if (currentTransportPaid !== transportPaidParamDefault) {
      queryParams.set(transportPaidParamName, currentTransportPaid);
    }
    if (currentProductType !== productTypeParamDefault) {
      queryParams.set(productTypeParamName, currentProductType);
    }
    if (priceMinRef?.current && priceMinRef.current.value) {
      queryParams.set(priceMinParamName, priceMinRef.current.value);
    }
    if (priceMaxRef?.current && priceMaxRef.current.value) {
      queryParams.set(priceMaxParamName, priceMaxRef.current.value);
    }
    if (currentDatePosted !== datePostedParamDefault) {
      queryParams.set(datePostedParamName, currentDatePosted);
    }
    navigate(`/announcements?${queryParams.toString()}`);
  }

  return (
    <>
      <Row className='mx-5 mt-5 mb-4'>
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
      {
        showFilter &&
        <FilterBar currentProductType={currentProductType} currentTransportPaid={currentTransportPaid}
          setCurrentProductType={setCurrentProductType} setCurrentTransportPaid={setCurrentTransportPaid} 
          currentDatePosted={currentDatePosted} setCurrentDatePosted={setCurrentDatePosted} />
      }
    </>
  )
}