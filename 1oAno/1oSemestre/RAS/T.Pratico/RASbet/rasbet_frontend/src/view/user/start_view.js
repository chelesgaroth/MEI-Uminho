import enquirer from 'enquirer'
import utils from '../common/utils.js';

const showStartupScreen = () => {
    // utils.clearScreen();
    return enquirer.prompt({
                type: 'select',
                name: 'initialAction',
                message: 'What do you want to do?',
                choices: [
                    'Sign up',
                    'Login',

                ],
            }
        );


}




export default {
    showStartupScreen,
};