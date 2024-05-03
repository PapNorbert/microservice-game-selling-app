
import { Star } from "react-bootstrap-icons"
import { AnnouncementDetailedResponse } from "../../interface/Announcements/announcementDetailedInterface"
import { Card } from "react-bootstrap"

interface PropType {
  announcement: AnnouncementDetailedResponse
}

export default function AnnouncementDetailedLong({ announcement }: PropType) {


  return (
    <>
      <Card key={`container_${announcement.announcementId}`} className='mt-5 mb-3'>
        <Card.Header as='h5' key={`header_${announcement.announcementId}`} >
          {announcement.title}
          <Star className="float-end clickable" />
        </Card.Header>
      </Card>
    </>
  )
}
