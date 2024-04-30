import { Navigate, Route, Routes } from "react-router-dom";

import AnnouncementCreate from "./AnnouncementCreate";
import RequireAuth from "../../components/RequireAuth";

export default function AnnouncementPage() {

  return (
    <>
      <Routes>
        <Route element={<RequireAuth />}>
          <Route path='/create' element={<AnnouncementCreate />} />
        </Route>
        <Route path='*' element={<Navigate to="/" />} />
      </Routes>
    </>
  )
}
