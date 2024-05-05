import { Card, Row, OverlayTrigger, Tooltip, Col, Badge } from "react-bootstrap"
import { useNavigate } from "react-router-dom";

import { AnnouncementShort } from "../../interface/Announcements/announcementsListShortInterface"
import { Star, Truck } from "react-bootstrap-icons";
import { dateFormatOptions } from "../../util/dateOptions";
import { convertConsoleTypeName } from "../../util/consoleTypeNameConversion";


interface PropType {
  announcement: AnnouncementShort
}

export default function AnnouncementListShort({ announcement }: PropType) {
  const navigate = useNavigate();

  return (
    <>
      <Card key={`container_${announcement.announcementId}`} className='mt-4 mb-3'>
        <Card.Header as='h5' key={`header_${announcement.announcementId}`} >
          {announcement.title}
        </Card.Header>
        <Card.Body className="clickable"
          onClick={() => navigate(`/announcements/${announcement.announcementId}`)} >
          <Row key={`${announcement.announcementId}_price`} className="fw-bold fs-5 mb-1" >
            <Col key={`${announcement.announcementId}_newDistTag`} >
              <Badge bg="secondary">
                {announcement.newDisc ? 'New' : 'Used'}
              </Badge>
            </Col>
            <Col className="text-end">
              {announcement.transportPaidBySeller &&
                <OverlayTrigger placement="bottom"
                  overlay={
                    <Tooltip key="transport-tooltip">Transport paid by the seller</Tooltip>
                  }>
                  <Truck className="me-2 mb-1" />
                </OverlayTrigger>
              }
              {announcement.price} Lei
            </Col>
          </Row>
          <Row key={`${announcement.announcementId}_product`} className="fs-5">
            <Col xs lg={2}>
              Product name:
            </Col>
            <Col xs >
              {`${announcement.soldGameDiscName},   ${convertConsoleTypeName(announcement.soldGameDiscType)}`}
            </Col>
          </Row>

          <Row key={`${announcement.announcementId}_creation_date`} className="mt-2">
            Posted at: {new Date(announcement.creationDate).toLocaleDateString('ro-RO', dateFormatOptions)}
          </Row>
        </Card.Body>
      </Card>
    </>
  )
}
