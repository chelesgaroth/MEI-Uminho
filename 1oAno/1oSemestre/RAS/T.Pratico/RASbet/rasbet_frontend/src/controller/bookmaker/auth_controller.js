import commonPrompts from "../../view/common/common_prompts.js";


import loginView from "../../view/bookmaker/login_view.js";
import utils from "../../view/common/utils.js";

import auth_service from "../../service/auth_service.js";

auth_service

export default {
    login,
    logout
};

async function login() {
    const { email, password } = await loginView.showLogin();

    // Attempt connection to backend here
    // authService.loginuser(email, password)
    try {
        const response = auth_service.loginUser(email, password);
        console.log(response);


        if (response === 'fail') {
            await commonPrompts.promptForAcknowledge(`👎 Failed to login 👎`, "Press enter to try again...")
            return false
        }
        else {
            await commonPrompts.promptForAcknowledge(`👋 Welcome ${email} 👋`, "Press enter to continue to main menu...")
            auth_service.setUserID(response);
            return true;
        }


    } catch (error) {
        console.error(error);
        return false;
    }

}



async function logout() {
    // Connect to backend and logout user
    // authService.logoutUser();

    utils.clearScreen();
    await commonPrompts.promptForAcknowledge("Successfully Logged Out", "😊 See you soon 😊");
    utils.clearScreen();
}