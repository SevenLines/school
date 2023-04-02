import React from 'react';

import logo from '../../assets/images/logo-istu.png';

import './index.scss';

const IstuLogo = () => {
  return (
    <div className='istu-logo d-flex align-items-center'>
      <img className='istu-logo__image' src={logo} alt='istu logo' />
      <div className='istu-logo__titles'>
        <h3 className='istu-logo__title mb-2'>Иркутский политех</h3>
      </div>
    </div>
  );
};

export default IstuLogo;
