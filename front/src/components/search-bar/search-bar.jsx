import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import React from 'react';
import eventStore from '../../store/events';
import './search-bar.scss';

const SearchBar = () => {
  const handleSearchChange = (e) => {
    eventStore.searchEvents(e.target.value);
  };

  return (
    <div className='input-group mb-3' style={{ display: 'contents' }}>
      <input
        type='text'
        placeholder=''
        className='form-control'
        onChange={handleSearchChange}
        defaultValue=''
      />
      <div className='input-group-prepend'>
        <span
          className='input-group-text'
          id='basic-addon1'
          style={{ height: '100%' }}
        >
          <FontAwesomeIcon icon='fa-solid fa-magnifying-glass' />
        </span>
      </div>
    </div>
  );
};
export default SearchBar;
