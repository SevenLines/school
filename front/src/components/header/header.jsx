import React from 'react';
import Navbar from '../navbar/navbar';

import './index.scss';

const Header = () => {
  return (
    <header className='header d-flex align-items-center'>
      <Navbar className='header__navbar' />
    </header>
  );
};

export default Header;
