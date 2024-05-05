
import { Star, StarFill } from "react-bootstrap-icons"
import { useState } from "react"
import { Card } from "react-bootstrap"


import { AnnouncementDetailedResponse } from "../../interface/Announcements/announcementDetailedInterface"
import useAuth from "../../hooks/useAuth"

interface PropType {
  announcement: AnnouncementDetailedResponse
}

export default function AnnouncementDetailedLong({ announcement }: PropType) {
  const { auth } = useAuth();
  const [savedByUser, setSavedByUser] = useState<boolean>(announcement.savedByUser);


  function onStarClick() {
    setSavedByUser(!savedByUser);
  }


  return (
    <>
      <Card key={`container_${announcement.announcementId}`} className='mt-5 mb-3'>
        <Card.Header as='h5' key={`header_${announcement.announcementId}`} >
          {announcement.title}
          {
            auth.logged_in && savedByUser &&
            <span className="clickable" onClick={onStarClick}>
              <StarFill className="float-end  mt-1" />
              <span className="float-end me-2" >Remove from saved</span>
            </span>
          }
          {
            auth.logged_in && !savedByUser &&

            <span className="clickable" onClick={onStarClick}>
              <Star className="float-end mt-1" />
              <span className="float-end me-2" >Save</span>
            </span>
          }
        </Card.Header>
      </Card>
    </>
  )
}
