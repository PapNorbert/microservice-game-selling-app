import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom'
import { StompSessionProvider } from 'react-stomp-hooks';
import 'bootstrap/dist/css/bootstrap.css';
import './App.css'
import './Buttons.css'

import Home from './pages/Home'
import Register from './pages/Register'
import Login from './pages/Login'
import Navigationbar from './layouts/NavigationBar';
import AnnouncementPage from './pages/Announcements/AnnouncementPage';
import UnauthorizedPage from './pages/UnauthorizedPage';
import SearchContextProvider from './context/SearchContextProvider';
import { wsServerUrl } from './config/application.json'
import MessagesPage from './pages/messages/MessagesPage';
import OrdersPage from './pages/orders/OrdersPage';
import ReviewsPage from './pages/reviews/ReviewsPage';

function App() {

  return (
    <div className='app'>
      <Router>
        <StompSessionProvider url={wsServerUrl} >
          <SearchContextProvider>
            <Navigationbar />
            <div className='container container-fluid'>
              <Routes>
                <Route path='/' element={<Home />} />
                <Route path='/home' element={<Home />} />
                <Route path='/register' element={<Register />} />
                <Route path='/login' element={<Login />} />
                <Route path='/announcements/*' element={<AnnouncementPage />} />
                <Route path='/reviews/*' element={<ReviewsPage />} />
                <Route path='/orders/*' element={<OrdersPage />} />
                <Route path='/messages/*' element={<MessagesPage />} />
                <Route path='/unauthorized' element={<UnauthorizedPage />} />
                <Route path='*' element={<Navigate to="/" />} />
              </Routes>
            </div>
          </SearchContextProvider>
        </StompSessionProvider>
      </Router>

    </div>
  )
}

export default App
