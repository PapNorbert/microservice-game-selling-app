import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom'
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


function App() {

  return (
    <div className='app'>
      <Router>
        <SearchContextProvider>
          <Navigationbar />
          <div className='container container-fluid'>
            <Routes>
              <Route path='/' element={<Home />} />
              <Route path='/home' element={<Home />} />
              <Route path='/register' element={<Register />} />
              <Route path='/login' element={<Login />} />
              <Route path='/announcements/*' element={<AnnouncementPage />} />
              <Route path='/unauthorized' element={<UnauthorizedPage />} />
              <Route path='*' element={<Navigate to="/" />} />
            </Routes>
          </div>
        </SearchContextProvider>
      </Router>

    </div>
  )
}

export default App
