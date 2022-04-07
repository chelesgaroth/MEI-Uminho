

import axios from "axios";
const BASE_URL = '/record'
axios.defaults.baseURL = 'http://localhost:8080';

import authService from "../auth_service.js";

const getUserRecord = () => {
    const req = axios.get(`${BASE_URL}`, {params:{userID: authService.getUserID()}} );
    return req.then(response => response.data);
} 



export default {
    getUserRecord,
}