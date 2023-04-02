import axios from './axios-default';

/**
 * Return array of all events. If it admin then send users in events too
 * @returns Array of events. With users or not
 */
export async function getAllEvents() {
  const { data } = await axios.get('/api/activities');
  return data.map((event) => ({
    ...event,
  }));
}
/**
 * Create new activity
 * @param {string} event.name Name of event
 * @param {string} event.description Description of event
 * @param {string} event.startDate Start date of event
 * @param {string} event.endDate End date of event
 * @param {string} event.participationPoint Point for participation
 * @param {string} event.maxParticipants Max count people to participate
 * @param {string} event.activityType ????????????
 * @return {event} Return object created event
 */
export async function createEvent(event) {
  const { data } = await axios.post('/api/activities/create', event);
  return data;
}

export async function deleteEvent(id) {
  const { data } = await axios.delete('/api/activities/' + id);
  return data;
}

/**
 * Update activity
 * @param {number} event.id Id of update event
 * @param {string} event.name Name of event
 * @param {string} event.description Description of event
 * @param {string} event.startDate Start date of event
 * @param {string} event.endDate End date of event
 * @param {string} event.participationPoint Point for participation
 * @param {string} event.maxParticipants Max count people to participate
 * @param {string} event.activityType ????????????
 * @return {event} Return object updated event
 */
export async function updateInfoEvent(event) {
  const { data } = await axios.put('/api/activities', event);
  return data;
}

/**
 * Mark the presence schooler on activity
 * @param {number} schoolchildId Id of schoolchild
 * @param {number} activityId Id of event
 * @return {Object} Empty object
 */
export async function appointmentSchoolchildOnEvent(schoolchildId, activityId) {
  const { data } = await axios.post('/api/activities/appointment', {
    schoolchildId,
    activityId,
  });
  return data;
}

/**
 * Remove mark the presence schooler on activity
 * @param {number} schoolchildId Id of schoolchild
 * @param {number} activityId Id of event
 * @return {Object} Empty object
 */
export async function removeAppointmentSchoolchildOnEvent(schoolchildId, activityId) {
  const { data } = await axios.delete('/api/activities/appointment', {
    data: { 
      schoolchildId,
      activityId,
    }
  });
  return data;
}

/**
 * Get all users registered on event by id event
 * @param {number} eventId Event id where users get
 * @returns {Array} Array of users registered on event
 */
export async function getRegisteredUsersOnEvent(eventId) {
  const { data } = await axios.get(
    `/api/schoolchildren/registered?activityId=${eventId}`
  );
  return data;
}

/**
 * Get all users appoint on event by id event
 * @param {number} eventId Event id where users get
 * @returns {Array} Array of users registered on event
 */
export async function getAppointmentUsersOnEvent(eventId) {
  const { data } = await axios.get(
    `/api/schoolchildren/appointment?activityId=${eventId}`
  );
  return data;
}

/**
 * Register schooler on activity
 * @param {number} schoolchildId Schooler id
 * @param {number} studyActivityId Event id
 * @returns Object user and activity
 */
export async function registerOnActivity(schoolchildId, activityId) {
  const { data } = await axios.post('/api/activities/register', {
    schoolchildId,
    activityId,
  });
  return data;
}

