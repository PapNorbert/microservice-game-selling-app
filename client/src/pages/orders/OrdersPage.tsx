import { Navigate, Route, Routes } from "react-router-dom";
import RequireAuth from "../../components/RequireAuth";
import OrdersAll from "./OrdersAll";
import OrderDetailed from "./OrderDetailed";

export default function OrdersPage() {
  return (
    <Routes>
      <Route element={<RequireAuth />}>
        <Route path='' element={<OrdersAll />} />
        <Route path='/:orderId' element={<OrderDetailed />} />
      </Route>
      <Route path='*' element={<Navigate to="/" />} />
    </Routes>
  )
}
