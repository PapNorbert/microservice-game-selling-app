import { useContext } from "react";
import { Col, Container, Form, OverlayTrigger, Row, Tooltip } from "react-bootstrap";
import { X } from "react-bootstrap-icons";

import { SearchContext } from "../context/SearchContextProvider";
import {
  productTypeParamDefault, transportPaidParamDefault
} from '../config/application.json'

interface PropType {
  currentTransportPaid: string;
  setCurrentTransportPaid: React.Dispatch<React.SetStateAction<string>>;
  currentProductType: string;
  setCurrentProductType: React.Dispatch<React.SetStateAction<string>>;
}

export default function FilterBar(
  { currentTransportPaid, setCurrentTransportPaid, currentProductType, setCurrentProductType }: PropType) {
  const { priceMinRef, priceMaxRef } = useContext(SearchContext);

  function handleFilterClear() {
    setCurrentTransportPaid(transportPaidParamDefault);
    setCurrentProductType(productTypeParamDefault);
    if(priceMinRef?.current) {
      priceMinRef.current.value = '';
    }
    if(priceMaxRef?.current) {
      priceMaxRef.current.value = '';
    }
  }

  return (
    <Container className='mx-5 mb-5'>
      <Row >
        <Col xs lg={{ span: 1, offset: 1 }} className="fs-5 fw-semibold" >
          Filters
          <OverlayTrigger placement="bottom"
            overlay={
              <Tooltip key="filter-tooltip">Clear Filters</Tooltip>
            }>
            <X className="clickable " onClick={handleFilterClear} />
          </OverlayTrigger>

        </Col>

      </Row>
      <Row className="mt-1">
        <Col lg={{ span: 2, offset: 1 }}>
          <Form.Text>Transport paid by</Form.Text>
          <Form.Select value={currentTransportPaid} onChange={e => setCurrentTransportPaid(e.target.value)}>
            <option value="ALL">Show all</option>
            <option value='SELLER'>Paid by the seller</option>
            <option value='BUYER'>Paid by the buyer</option>
          </Form.Select>
        </Col>
        <Col lg={{ span: 2 }}>
          <Form.Text>Product type</Form.Text>
          <Form.Select value={currentProductType} onChange={e => setCurrentProductType(e.target.value)}>
            <option value="ALL">Show all</option>
            <option value='NEW'>New</option>
            <option value='USED'>Used</option>
          </Form.Select>
        </Col>
        <Col lg={{ span: 2 }}>
          <Form.Text>Price from</Form.Text>
          <Form.Control type='number' min={0} ref={priceMinRef}
            placeholder='Price min' />
        </Col>
        <Col lg={{ span: 2 }}>
          <Form.Text>Price to</Form.Text>
          <Form.Control type='number' min={0} ref={priceMaxRef}
            placeholder='Price max' />
        </Col>


      </Row>
    </Container>
  )
}
