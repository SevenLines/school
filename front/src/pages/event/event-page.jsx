import { React, useState } from 'react';
import Dropdown from 'react-bootstrap/Dropdown';
import Modal from 'react-bootstrap/Modal';
import EventEdit from '../../modals/event-edit/EventEdit';
import EventInfoRegistered from '../../modals/registered-students-info/registered-students';
import EventInfo from '../../modals/event-info/event-info';
import SearchBar from '../../components/search-bar/search-bar';
import authStore from '../../store/auth';
import eventStore from '../../store/events';

import './index.scss';
import EventList from './event-list';

const EventPage = () => {
  const [isModalActive, setModalActive] = useState(false);
  const [modalContent, setModalContent] = useState(null);
  const [modalOptions, setModalOptions] = useState({
    size: 'lg',
    backdrop: true,
    keyboard: true,
  });
  const isAdmin = authStore.isAdmin;
  const isAuth = authStore.isAuth;
  const showEventAddModal = () => {
    setModalContent(<EventEdit closeModal={handleClose}></EventEdit>);
    setModalOptions({ ...modalOptions, backdrop: 'static', keyboard: false });
    setModalActive(true);
  };
  const showEventInfoModal = isAdmin
    ? (event) => {
        setModalContent(
          <EventEdit event={event} closeModal={handleClose}></EventEdit>
        );
        setModalOptions({
          ...modalOptions,
          backdrop: 'static',
          keyboard: false,
        });
        setModalActive(true);
      }
    : (event) => {
        setModalContent(
          <EventInfo
            event={event}
            isAuth={isAuth}
            closeModal={handleClose}
          ></EventInfo>
        );
        setModalOptions({ ...modalOptions, backdrop: true, keyboard: true });
        setModalActive(true);
      };
  const showEventRegisteredModal = (event) => {
    setModalContent(<EventInfoRegistered event={event}></EventInfoRegistered>);
    setModalOptions({ ...modalOptions, backdrop: true, keyboard: true });
    setModalActive(true);
  };
  const handleClose = () => {
    setModalActive(false);
  };
  function _sort(arr, fn) {
    const _arr = JSON.parse(JSON.stringify(arr));
    _arr.sort(fn);
    return _arr;
  }

  const sortJsonArrayByProperty = (propName) => {
    return function (a, b) {
      const nameA = a[propName].toUpperCase();
      const nameB = b[propName].toUpperCase();
      if (nameA < nameB) {
        return -1;
      }
      if (nameA > nameB) {
        return 1;
      }
      return 0;
    };
  };

  const sortJsonArrayByDate = (propName) => {
    return function (a, b) {
      const dateA = new Date(a[propName].split('-').reverse().join('-'));
      const dateB = new Date(b[propName].split('-').reverse().join('-'));
      return dateA - dateB;
    };
  };
  return (
    <>
      <Modal
        show={isModalActive}
        onHide={handleClose}
        size={modalOptions.size}
        backdrop={modalOptions.backdrop}
        keyboard={modalOptions.keyboard}
      >
        {modalContent}
      </Modal>
      <div className='event-page mx-auto container mt-4'>
        <div className='control-bar d-flex justify-content-between pe-4'>
          <Dropdown style={{ width: '180px' }}>
            <Dropdown.Toggle variant='light' style={{ width: '100%' }}>
              Сортировка
            </Dropdown.Toggle>
            <Dropdown.Menu style={{ width: '180px' }}>
              <Dropdown.Item
                onClick={() => {
                  eventStore.filteredEvents = _sort(
                    eventStore.filteredEvents,
                    sortJsonArrayByProperty('name')
                  );
                }}
              >
                По названию
              </Dropdown.Item>
              <Dropdown.Item
                onClick={() => {
                  const sortedByStart = _sort(
                    eventStore.filteredEvents,
                    sortJsonArrayByDate('startDate')
                  );
                  eventStore.filteredEvents = _sort(
                    sortedByStart,
                    sortJsonArrayByDate('endDate')
                  );
                }}
              >
                По дате
              </Dropdown.Item>
            </Dropdown.Menu>
          </Dropdown>
          <SearchBar />
          {isAdmin && (
            <div
              className='btn btn-light'
              onClick={showEventAddModal}
              style={{ width: '240px' }}
            >
              Создать мероприятие
            </div>
          )}
        </div>
        <EventList
          isAuth={isAuth}
          isAdmin={isAdmin}
          showModalFunction={showEventInfoModal}
          showEventRegisteredModal={showEventRegisteredModal}
        />
      </div>
    </>
  );
};

export default EventPage;
