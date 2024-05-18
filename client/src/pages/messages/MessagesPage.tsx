import { Navigate, Route, Routes } from "react-router-dom";
import MessagesAll from "./MessagesAll";
import RequireAuth from "../../components/RequireAuth";

export default function MessagesPage() {
  return (
    <Routes>
      <Route element={<RequireAuth />}>
        <Route path='' element={<MessagesAll />} />
      </Route>
      <Route path='*' element={<Navigate to="/" />} />
    </Routes>
  )
}
