import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Card, Table, ToggleButton } from 'react-bootstrap';

import './index.scss';
import { observer } from 'mobx-react-lite';
import { useState } from 'react';
import eventsStore from '../../store/events';
import { useEffect } from 'react';

const EventInfoRegistered = observer(({ event }) => {
  const eventData = {
    id: event.id,
    name: event.name,
    description: event.description,
    date: event.date,
    from_ball: event.participationPoint,
  };
  useEffect(() => {
    const fetch = async () => {
      const users = await eventsStore.fetchRegisteredStudent(event.id);
      setAccountsData(users);
      setToggleActive(users.reduce((acc, elem) => {
        acc[elem.id] = elem.attendActivity;
        return acc;
      }, {}));
    };
    fetch();
  }, []);

  const [accountsData, setAccountsData] = useState([]);
  const [toggleActive, setToggleActive] = useState([]);
  const viewInfoBtn = (elem, key) => (
    <ToggleButton
      key={key}
      type='checkbox'
      id={'toggle_checkbox' + key}
      variant='outline-secondary'
      checked={toggleActive[key]}
      onChange={async () => {
        if (!toggleActive[key]) {
          await eventsStore.checkUsers(elem.id, eventData.id);
        } else {
          await eventsStore.removeUsers(elem.id, eventData.id);
        }
        setToggleActive((arr) => ({ ...arr, [key]: !toggleActive[key] }));
      }}
    >
      <FontAwesomeIcon
        style={{ width: '2em' }}
        icon={toggleActive[key] ? 'fa-check' : 'fa-xmark'}
      />
    </ToggleButton>
  );

  return (
    <>
      <div style={{ width: '80%', marginLeft: '10%' }}>
        <div>
          <Card className='brick' style={{ fontSize: '1.8em' }}>
            {eventData.name}
          </Card>
        </div>
        <Card
          className='brick'
          style={{ marginBottom: '1em', whiteSpace: 'pre-line' }}
        >
          {eventData.description}
        </Card>
        <Table striped bordered hover>
          <thead>
            <tr>
              <td>Фамилия</td>
              <td>Имя</td>
              <td>Отчество</td>
              <td>Присутствие</td>
            </tr>
          </thead>
          <tbody>
            {accountsData.map((elem) => (
              <tr key={elem.id}>
                <td>{elem.surname}</td>
                <td>{elem.name}</td>
                <td>{elem.fatherName}</td>
                <td>{viewInfoBtn(elem, elem.id)}</td>
              </tr>
            ))}
          </tbody>
        </Table>
      </div>
    </>
  );
});

export default EventInfoRegistered;
