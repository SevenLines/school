import { React, useEffect } from 'react';
import EventElement from '../../components/event-element/event-element';
import eventStore from '../../store/events';
import { observer } from 'mobx-react-lite';


const EventList = observer(({isAuth, isAdmin, showModalFunction, showEventRegisteredModal}) => {
  useEffect(() => {
    eventStore.fetchEvents();
  }, []);
  
  const events = eventStore.filteredEvents ?? [];
  return (
    <>
      <div className='event-page-content container-fluid mt-3'>
        {events.map((event) => {
          return (
            <EventElement
              key={event.id}
              event={event}
              isAuth={isAuth}
              isAdmin={isAdmin}
              showModalFunction={showModalFunction}
              showEventRegisteredModal={showEventRegisteredModal}
            ></EventElement>
          );
        })}
      </div>
    </>
  );
});

export default EventList;
