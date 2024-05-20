import { Col, Row } from "react-bootstrap";

interface PropType {
  keyString: string;
  valueString: string;
}

export default function RowWithKeyValue({ keyString, valueString }: PropType) {
  return (
    <Row>
      <Col lg='2'>
        {keyString}
      </Col>
      <Col>
        {valueString}
      </Col>
    </Row>
  )
}
