import { Truck } from "react-bootstrap-icons"
import { useState } from "react"
import { Card, Row, Stack } from "react-bootstrap"
import { useQueryClient } from "@tanstack/react-query"

import { AnnouncementDetailedResponse } from "../../../interface/Announcements/announcementDetailedInterface"
import useAuth from "../../../hooks/useAuth"
import { dateFormatOptions } from "../../../util/dateOptions"
import { convertConsoleTypeName } from "../../../util/consoleTypeNameConversion"

interface PropType {
  announcement: AnnouncementDetailedResponse
}

export default function AnnouncementOwnCard({ announcement }: PropType) {
  const { auth } = useAuth();

  const [error, setError] = useState<string>('');
  const queryClient = useQueryClient();
  


  return (
    <Card key={`container_${announcement.announcementId}_details`} className='mt-5 mb-3'>
      <Card.Header as='h4' key={`header_${announcement.announcementId}`} >
        {announcement.title}
      </Card.Header>
      <Card.Body>
        <Row key={`${announcement.announcementId}_creation_date`}>
          Posted at: {new Date(announcement.creationDate).toLocaleDateString('ro-RO', dateFormatOptions)}
        </Row>
        <Row key={`${announcement.announcementId}_price`} className="fw-bold fs-5 mb-2 mt-2" >
          {announcement.price} Lei
        </Row>
        <Row key={`${announcement.announcementId}_productName`} className="fs-5">
          Game Disc Name: {announcement.soldGameDisc.name}
        </Row>
        <Stack key={`${announcement.announcementId}_tags`} direction="horizontal" gap={3} className="mt-2">
          <span className="border rounded border-black p-1">
            Year:  {announcement.soldGameDisc.gameYear}
          </span>
          <span className="border rounded border-black p-1">
            Condition: {announcement.newDisc ? 'New' : 'Used'}
          </span>
          <span className="border rounded border-black p-1">
            Console:  {convertConsoleTypeName(announcement.soldGameDisc.type)}
          </span>
          {announcement.transportPaidBySeller &&
            <span className="border rounded border-black p-1">
              <Truck className="me-2 mb-1" />
              Transport paid by the seller
            </span>
          }
        </Stack>
        <Row className="fw-bold fs-5 mt-3 mb-1" >
          Description
        </Row>
        <Row key={`${announcement.announcementId}_description`} className="" >
          {announcement.description}
        </Row>
      </Card.Body>
    </Card >
  )
}
