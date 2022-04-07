
import startView from '../../view/bookmaker/start_view.js'

import authController from './auth_controller.js'
import mainMenuController from './main_menu_controller.js'

export default {
    bookmakerAppStart
}

async function bookmakerAppStart ()  {
    
    /*
    Skip for now, debug only
    
    const userResponse = await startView.showStartupScreen();
    
    if (userResponse.option === "LOGIN")
        await authController.login();

    */
    let running = true;
    while (running) {
        const selectedOption = await mainMenuController.mainMenu();

        if (selectedOption === 'LOGOUT')
        {
            running = false;
        }
    }
}


