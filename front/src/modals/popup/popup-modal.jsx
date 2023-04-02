import React from 'react';

import './index.scss';

const Popup_modal = ({ active, setActive, children }) => {
  return (
    <div
      className={active ? 'popup_block active' : 'popup_block'}
      onClick={() => setActive(false)}
    >
      <div className='popup_content' onClick={(e) => e.stopPropagation()}>
        {children}
      </div>
    </div>
  );
};

export default Popup_modal;
