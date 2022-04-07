import authService from "../auth_service.js";

import axios from "axios";
const BASE_URL = '/profile'
axios.defaults.baseURL = 'http://localhost:8080';

const getUserProfile = () => {
    const req = axios.get(`${BASE_URL}`, {params:{userID: authService.getUserID()} });
    return req.then(response => response.data);
}


const updateUserEmail = (userData, newEmail) => {
    const req = axios.post(`${BASE_URL}/update`, userData, {params: {action: 'EMAIL', data: newEmail}});
    return req.then(response => response.data);
}


const updateUserPassword = (newUserObject) => {
    const req = axios.post(`${BASE_URL}/profile/update/?id=${authService.getUserID()}`, newUserObject);
    return req.then(response => response.data);
}


export default {
    getUserProfile,
    updateUserPassword,
    updateUserEmail,
}