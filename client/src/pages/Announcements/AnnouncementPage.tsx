import { Navigate, Route, Routes } from "react-router-dom";

import AnnouncementCreate from "./AnnouncementCreate";
import RequireAuth from "../../components/RequireAuth";
import AnnouncementsAll from "./AnnouncementsAll";
import AnnouncementDetailed from "./AnnouncementDetailed";

export default function AnnouncementPage() {

  return (
    <>
      <Routes>
        <Route path='' element={<AnnouncementsAll />} />
        <Route path='/:announcementId' element={<AnnouncementDetailed />} />
        <Route element={<RequireAuth />}>
          <Route path='/create' element={<AnnouncementCreate />} />
        </Route>
        <Route path='*' element={<Navigate to="/" />} />
      </Routes>
    </>
  )
}
