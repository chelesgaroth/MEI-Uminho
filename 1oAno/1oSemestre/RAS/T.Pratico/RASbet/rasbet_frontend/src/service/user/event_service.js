

import axios from "axios";
import authService from "../auth_service.js";
const BASE_URL = '/events'
axios.defaults.baseURL = 'http://localhost:8080';


const getEventFeed = () => {

    const userID = "coolUserID"
    const req = axios.get(`${BASE_URL}`, {params:{userID: userID} });
    return req.then(response => response.data);
} 


export default {
    getEventFeed,
}