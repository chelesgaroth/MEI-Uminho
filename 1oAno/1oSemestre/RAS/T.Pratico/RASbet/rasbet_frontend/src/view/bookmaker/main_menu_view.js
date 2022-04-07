import enquirer from "enquirer";
import utils from '../common/utils.js'

export default {
    showMainMenu
};

function showMainMenu () {
   //  utils.clearScreen();
    console.log("=============== Main Menu ==================")
    const question = {
        type:'select',
        name:'selectedOption',
        message:'Available Actions:',
        choices: [
            {name: 'EVENTS', message:'📋   My Events'},
            {name: 'LOGOUT', message:'🚪🚶 Logout'},
        ]
    }

    return enquirer.prompt(question);
}



