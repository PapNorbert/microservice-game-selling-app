import { AnnouncementShort } from "../../interface/Announcements/announcementsListShortInterface"


interface PropType {
  announcement: AnnouncementShort
}

export default function AnnouncementListShort({announcement}: PropType) {
  return (
    <div>{announcement.title}</div>
  )
}
