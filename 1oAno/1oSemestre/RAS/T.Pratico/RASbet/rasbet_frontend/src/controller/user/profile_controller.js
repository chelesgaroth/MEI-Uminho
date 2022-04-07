
import profileView from '../../view/user/profile_view.js'
import acknowledgePrompts from '../../view/common/common_prompts.js';

import profileService from '../../service/user/profile_service.js';
import utils from '../../view/common/utils.js';

import commonPrompts from "../../view/common/common_prompts.js";

let userData = {}

async function profile() {

    try {
        let userData = await profileService.getUserProfile();
        console.log(userData);

        const { option } = await profileView.showProfileScreen(userData);

        if (option === 'CHANGE_EMAIL') {
            await changeEmailScreen();
        }
        else if (option === 'CHANGE_PASSWORD') {
            await changePasswordScreen();
        }
        else if (option === 'RETURN') {
            return;
        }

    } catch (error) {
        // utils.clearScreen();
        await commonPrompts.promptForAcknowledge(`👎 Failed to fetch profile. 👎`, "Press enter to try again...")
    }
}

async function changeEmailScreen() {
    const { newEmail } = await profileView.showChangeEmailScreen()

    // Send new email to backend, and wait for response
    // backendConnection.newEmail()



    try {
        await profileService.updateUserEmail(userData, newEmail);
        // Show success message if email was correctly changed
        await acknowledgePrompts.promptForAcknowledge('🙌 Email change successfull 🙌', `Email changed to ${newEmail}`);
    } catch (error) {
        console.error(error);
    }

}

async function changePasswordScreen(userProfileData) {
    const { newPassword } = await profileView.showChangePasswordScreen()
    console.log(newPassword);

    // Send new password to backend, and wait for response
    // backendConnection.newPassword(), algo assim

    try {
        userProfileData.password = newPassword;
        await profileService.updateUserPassword(userProfileData)
        console.clear();
        await acknowledgePrompts.promptForAcknowledge('🙌 Password change successfull 🙌', `[DEBUG] Password changed to ${newPassword}`);
    } catch (error) {
        console.error(error);
    }

}


export default {
    profile,
};