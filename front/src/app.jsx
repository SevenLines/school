import React from 'react';
import { Route, BrowserRouter as Router, Routes } from 'react-router-dom';
import Header from './components/header/header';
import AuthPage from './pages/auth/auth-page';
import EventPage from './pages/event/event-page';
import RegisterPage from './pages/register/register-page';

import './scss/index.scss';

function App() {
  return (
    <div className='App'>
      <Router>
        <Header />
        <Routes>
          <Route path='/login' element={<AuthPage />} />
          <Route path='/register' element={<RegisterPage />} />
          <Route exact path='/' element={<EventPage />} />
        </Routes>
      </Router>
    </div>
  );
}

export default App;
