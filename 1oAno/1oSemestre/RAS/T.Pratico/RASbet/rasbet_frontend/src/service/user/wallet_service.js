import authService from "../auth_service.js";

import axios from "axios";
const BASE_URL = '/wallet'
axios.defaults.baseURL = 'http://localhost:8080';


const getWallet = () => {
    const req = axios.get(`${BASE_URL}`, {params: {userID: authService.getUserID()}});
    return req.then(response => response.data);
} 



const deposit = (coin, quantity) => {
    const req = axios.post(`${BASE_URL}/update`, {coin: coin, quantity: quantity, action: 1}, {params:{userID:authService.getUserID()}});
    return req.then(response => response.data);
} 


const withdraw = (coin, quantity) => {
    const req = axios.post(`${BASE_URL}/update`,  {coin: coin, quantity: quantity, action: 0}, {params:{userID:authService.getUserID()}});
    return req.then(response => response.data);
} 


export default {
    getWallet,
    deposit,
    withdraw
}