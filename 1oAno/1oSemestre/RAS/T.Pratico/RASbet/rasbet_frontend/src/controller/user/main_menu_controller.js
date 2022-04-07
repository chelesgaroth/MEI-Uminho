import mainMenuView from  "../../view/user/main_menu_view.js";

import feedController from "./feed_controller.js";
import profileController from "./profile_controller.js";
import walletController from "./wallet_controller.js"
import recordController from "./record_controller.js"
import authController from "./auth_controller.js"

export default {
    mainMenu
}

async function mainMenu () {
    const {selectedOption} = await mainMenuView.showMainMenu();

    if (selectedOption === 'FEED') {
        await feedController.feed()
    }
    else if (selectedOption === 'PROFILE') {
        await profileController.profile();
    }
    else if (selectedOption === 'WALLET') {
        await walletController.wallet();
    }
    else if (selectedOption === 'RECORD') {
        await recordController.record();
    }
    else if (selectedOption === 'LOGOUT'){
        await authController.logout();
    }

    return selectedOption;
}