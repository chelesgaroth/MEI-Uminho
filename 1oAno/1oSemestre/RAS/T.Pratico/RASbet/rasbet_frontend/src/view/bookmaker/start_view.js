import enquirer from 'enquirer'
import utils from '../common/utils.js';


export default {
    showStartupScreen,
};

function showStartupScreen ()  {
    utils.clearScreen();
    return enquirer.prompt({
                type: 'select',
                name: 'option',
                message: '👋 Welcome to RASBET 👋',
                choices: [
                    {name:'LOGIN', value:'LOGIN', message:'Login'},
                ],
            }
        );


}




