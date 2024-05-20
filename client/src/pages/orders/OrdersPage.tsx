import { Navigate, Route, Routes } from "react-router-dom";
import RequireAuth from "../../components/RequireAuth";
import OrdersAll from "./OrdersAll";

export default function OrdersPage() {
  return (
    <Routes>
      <Route element={<RequireAuth />}>
        <Route path='' element={<OrdersAll />} />
      </Route>
      <Route path='*' element={<Navigate to="/" />} />
    </Routes>
  )
}
