const isSearchEvent = (event, query) => {
  if (query === '') return true;
  return (
    event.name.toLowerCase().includes(query) ||
      event.description.toLowerCase().includes(query) ||
      event.startDate.toLowerCase().includes(query)
  );
};

export {
  isSearchEvent
};
