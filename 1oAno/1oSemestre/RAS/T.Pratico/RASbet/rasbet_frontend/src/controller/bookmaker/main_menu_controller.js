import mainMenuView from '../../view/bookmaker/main_menu_view.js'

import authController from "./auth_controller.js"
import eventPageController from './event_page/event_page_controller.js'

export default {
    mainMenu
};


async function mainMenu () {
    const {selectedOption} = await mainMenuView.showMainMenu();

    if (selectedOption === 'EVENTS'){
        await eventPageController.eventPage();
    }
    else if (selectedOption === 'LOGOUT'){
        await authController.logout();
    }
    else {
        console.error("Invalid option selected!");
    }

    return selectedOption;
}