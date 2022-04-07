

import axios from "axios";
const BASE_URL = '/auth'
axios.defaults.baseURL = 'http://localhost:8080';

let _userID = 'default_id';

const registerUser = (userObj) => {
    const req = axios.put(`${BASE_URL}/register`, userObj );
    return req
        .then(response => {
            // console.log(response)
            return response.data
        })
        .catch(err => console.error(err));
} 

const loginUser = (email, password) => {
    const req = axios.post(`${BASE_URL}/login`,  {email: email, password: password});
    return req
    .then(response => response.data)
    .catch(err => console.error(err));

} 

const setUserID = (userID) => {
    _userID = userID
}

const getUserID = () => {
    return _userID;
}

export default {
    registerUser,
    loginUser,
    getUserID,
    setUserID
}