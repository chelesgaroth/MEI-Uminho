import enquirer from "enquirer";
import utils from '../common/utils.js'

export default {
    showMainMenu
};

function showMainMenu () {
    // utils.clearScreen();
    console.log("=============== Main Menu ==================")
    const question = {
        type:'select',
        name:'selectedOption',
        message:'Available Actions:',
        choices: [
            {name: 'FEED', message:'📰 My Feed'},
            {name: 'PROFILE', message:'🧍 My Profile'},
            {name: 'WALLET', message:'👛 My Wallet'},
            {name: 'RECORD', message:'📊 My Record'},
            {role: 'separator'},
            {name: 'LOGOUT', message:'🚪🚶 Logout'},
        ]
    }

    return enquirer.prompt(question);
}



