
import signupView from "../../view/user/signup_view.js";
import loginView from "../../view/user/login_view.js";
import commonPrompts from "../../view/common/common_prompts.js";
import walletController from "./wallet_controller.js";
import utils from "../../view/common/utils.js";
import authService from "../../service/auth_service.js";

export default {
    signup,
    login,
    logout,
}

async function signup() {
    const { name, username, email, password } = await signupView.showSignup();

    const dobInput = await signupView.showDOBPicker();

    ;
    console.log(name)
    console.log(email)
    console.log(password)
    console.log(dobInput)

    let dob = new Date(dobInput);

    try {
        const response = await authService.registerUser(
            {
                idUser: username,
                email: email,
                name: name,
                password: password,
                dob: '2021-10-26' // TODO: DOB IS HARDCODED!
            }
        )
        
        if (response === 'OK') {
                authService.setUserID(username);
                utils.clearScreen();
                await commonPrompts.promptForAcknowledge(`👋 Account created!👋`, "Press enter to continue...")
                return true;
        } else {
                await commonPrompts.promptForAcknowledge(`👎 Failed to create account. 👎`, "Press enter to try again...")
                return false;
        }
        
    } catch (error) {
        console.error(error)
        await commonPrompts.promptForAcknowledge(`👎 Failed to create account. 👎`, "Press enter to try again...")
        return false;
    }
   
}

async function login() {
    console.log("HEre")
    const { email, password } = await loginView.showLogin();

    // Attempt connection to backend here
    try {

        const resp = await authService.loginUser(email, password)
        console.log(resp)
       

        if (resp === 'fail') {
            await commonPrompts.promptForAcknowledge(`👎 Login Failed!👎`, "Press enter to return...")
            return false;
        } else {
            authService.setUserID(resp);
            await commonPrompts.promptForAcknowledge(`👋 Welcome !👋`, "Press enter to continue...")
            return true;
        }

    } catch (error) {
        await commonPrompts.promptForAcknowledge(`👎 Login Failed!👎`, "Press enter to return...")
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