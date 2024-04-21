import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import 'bootstrap/dist/css/bootstrap.css';  
import './App.css'

import Home from './pages/Home'
import Register from './pages/Register'
import Login from './pages/Login'


function App() {

  return (
    <div className='app'>
      <Router>
        <div className='container container-fluid'>
          <Routes>
            <Route path='/' element={<Home />} />
            <Route path='/home' element={<Home />} />
            <Route path='/register' element={<Register />} />
            <Route path='/login' element={<Login />} />

          </Routes>
        </div>
      </Router>

    </div>
  )
}

export default App
