import { Col, Container, Row } from "react-bootstrap";

import { Review } from "../interface/review/reviewsInterface"
import { dateFormatOptions } from "../util/dateOptions";

interface PropType {
  review: Review;
}

export default function ReviewElement({ review }: PropType) {
  return (
    <Container className='border border-2 border-secondary rounded mb-3'>
      <Row className='mt-2'>
        <Col>
          Review by:
          <span className="fw-semibold ms-1">
            {review.reviewer.username}
          </span>
        </Col>
      </Row>
      <Row>
        <Col xs lg={2} className='ms-5'>
          {new Date(review.creationDate).toLocaleDateString('ro-RO', dateFormatOptions)}
        </Col>
        <Col xs lg={8} className='mb-4'>
          {
            review.reviewText.split('\n').map((textRow, i) => {
              return (
                <span key={`text_row_container_${i}`}>
                  <span key={`text_row_${i}`}>
                    {textRow}
                  </span>
                  <br />
                </span>
              )
            })
          }
        </Col>
      </Row>
    </Container >
  )
}
