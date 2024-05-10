import { useState } from 'react'
import { Card, Collapse, Container, ListGroup, Row } from 'react-bootstrap'
import { ArrowDown, ArrowUp } from 'react-bootstrap-icons';



export default function ConsumerRightsCard() {
  const [expanded, setExpanded] = useState<boolean>(false);

  return (
    <Card key={`container_consumer_rights`} className='mt-4 mb-5'>
      <Container>
        <Row className="fw-bold">
          CONSUMER RIGHTS
        </Row>
        <Row className='mt-2'>
          This ad was published by a private seller.
        </Row>
        <Row className='mt-2'>
          As a result, consumer rights laws do not apply to purchases you make from this seller.
          Stay safe when conducting online transactions by being cautious of suspicious links, selecting the payment on delivery option
          to the courier for added security, and refraining from sharing personal or financial information with unknown parties.
        </Row>
        <Row>
          Always remain vigilant against phishing attempts, which can disguise themselves as legitimate messages in an attempt
          to steal your valuable data.
        </Row>
      </Container>
      <Collapse in={expanded} >
        <Container>
          <Row className='mt-2'>
            There are several entities involved when you buy a product or service on CGS.
            Each of these have the following responsibilities:
          </Row>
          <ListGroup as="ol" numbered className='mt-2 list-group-flush'>
            <ListGroup.Item as="li">
              <span className='fw-semibold'>CGS is responsible for</span> providing online services, such as a CGS account, publishing and promoting announcements,
              the ability to buy order the products.
            </ListGroup.Item>
            <ListGroup.Item as="li">
              <span className='fw-semibold'>Private sellers are responsible for</span> selling and shipping the product or providing the service exactly as described in the ad.
              If you have any questions about your purchase, we recommend that you contact the private seller directly.
            </ListGroup.Item>
            <ListGroup.Item as="li">
              <span className='fw-semibold'>Our delivery partners are responsible for</span> delivering the products to you.
            </ListGroup.Item>
          </ListGroup>
        </Container>
      </Collapse>

      <Row className='text-center mt-2 clickable' onClick={() => setExpanded(!expanded)}>
        {expanded ?
          <ArrowUp />
          :
          <ArrowDown />
        }
      </Row>

    </Card>
  )
}
