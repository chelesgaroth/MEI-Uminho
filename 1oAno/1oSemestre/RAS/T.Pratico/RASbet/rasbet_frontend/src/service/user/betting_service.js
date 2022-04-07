

import axios from "axios";
import authService from "../auth_service.js";
const BASE_URL = '/bet'
axios.defaults.baseURL = 'http://localhost:8080';


const submitBettingSlip = (bettingSlip) => {
    const req = axios.put(`${BASE_URL}/submit`, {bets: bettingSlip}, {params: {userID: authService.getUserID()}}  );
    return req.then(response => {response.data});
} 




export default {
    submitBettingSlip,
}