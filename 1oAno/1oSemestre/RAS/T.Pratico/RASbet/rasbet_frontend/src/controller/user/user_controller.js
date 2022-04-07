
import auth_service from "../../service/auth_service.js";
import commonPrompts from "../../view/common/common_prompts.js";
import startView from "../../view/user/start_view.js";

import authController from './auth_controller.js'
import mainMenuController from './main_menu_controller.js'



export default {
    userAppStart
};

async function userAppStart() {

    // Use this to skip login/signup
    let loggedIn = false;

    while (!loggedIn) {

        const userResponse = await startView.showStartupScreen();

        if (userResponse.initialAction === "Sign up") {

                const success = await authController.signup();

                if (success)
                    loggedIn = true;
        }
        else {
                const success = await authController.login();
                if (success)
                    loggedIn = true;
        }
    }


    //  Sign in a user for testing only
    // auth_service.setUserID("user_id_1");

    
    let running = true;
    while (running) {
        const selectedOption = await mainMenuController.mainMenu();

        if (selectedOption === 'LOGOUT') {
            running = false;
        }
    }
}