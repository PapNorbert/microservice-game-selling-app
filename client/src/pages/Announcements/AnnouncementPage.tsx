import { Navigate, Route, Routes } from "react-router-dom";

import AnnouncementCreate from "./AnnouncementCreate";
import RequireAuth from "../../components/RequireAuth";
import AnnouncementsAll from "./AnnouncementsAll";

export default function AnnouncementPage() {

  return (
    <>
      <Routes>
        <Route path='' element={<AnnouncementsAll />} />
        <Route element={<RequireAuth />}>
          <Route path='/create' element={<AnnouncementCreate />} />
        </Route>
        <Route path='*' element={<Navigate to="/" />} />
      </Routes>
    </>
  )
}
