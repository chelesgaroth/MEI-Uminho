

import axios from "axios";
const BASE_URL = '/bookmaker'
axios.defaults.baseURL = 'http://localhost:8080';


import authService from "../auth_service.js";

const getBookmakerEvents = () => {
    const req = axios.get(`${BASE_URL}/events`,  {params:{bookmakerID: authService.getUserID()} } );
    return req.then(response => response.data);
} 

const submitEvent = (sport, competition, {date, startTime}, betConfig) => {
    const req = axios.post(`${BASE_URL}/events/submit/${authService.getUserID()}`, 
        {
            sport: sport,
            competition: competition,
            date: date,
            startTime: startTime,
            betConfig: betConfig
        }
    );
    return req.then(response => response.data);
} 

const closeEvent = (eventID) => {
    const req = axios.post(`${BASE_URL}/events/${authService.getUserID()}/close/${eventID}`);
    return req.then(response => response.data);
} 

const cancelEvent = (eventID) => {
    const req = axios.post(`${BASE_URL}/events/${authService.getUserID()}/cancel/${eventID}`);
    return req.then(response => response.data);
} 

const suspendBets = (eventID, betsToSuspend) => {
    const req = axios.post(`${BASE_URL}/events/suspend/${authService.getUserID()}/${eventID}`, {betsToSuspend: betsToSuspend});
    return req.then(response => response.data);
} 

const unsuspendBets = (eventID, betsToSuspend) => {
    const req = axios.post(`${BASE_URL}/events/unsuspend/${authService.getUserID()}/${eventID}`, {betsToSuspend: betsToSuspend});
    return req.then(response => response.data);
} 

export default {
    getBookmakerEvents,
    submitEvent,
    closeEvent,
    cancelEvent,
    suspendBets,
    unsuspendBets,
}