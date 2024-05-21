import { Navigate, Route, Routes } from "react-router-dom";
import ReviewsAll from "./ReviewsAll";

export default function ReviewsPage() {
  return (
    <Routes>
      <Route path='/:sellerId' element={<ReviewsAll />} />
      <Route path='*' element={<Navigate to="/" />} />
    </Routes>
  )
}
