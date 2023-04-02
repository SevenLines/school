import React from 'react';

import './index.scss';

const EventElement = ({
  event,
  isAdmin,
  isAuth,
  showModalFunction,
  showEventRegisteredModal,
}) => {
  const actionDescription = isAdmin || !isAuth ? 'Подробнее' : 'Подать заявку';
  return (
    <div className='event-element mb-4 row'>
      <div className='ms-4 col-2'>
        <div className='event-picture'></div>
      </div>
      <div className='event-name align-middle col-6 '>{event.name}</div>
      <div className='event-date col'>{event.startDate}</div>
      <div
        className='event-action col btn btn-light'
        onClick={() => showModalFunction(event)}
      >
        {actionDescription}
      </div>
      {isAdmin && (
        <div
          className='event-action col btn btn-light'
          onClick={() => showEventRegisteredModal(event)}
        >
          {'Посещение'}
        </div>
      )}
    </div>
  );
};

export default EventElement;
