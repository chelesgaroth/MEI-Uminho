import enquirer from 'enquirer'
import utils from '../common/utils.js'

export default {
    showLogin
}

function showLogin() {
    utils.clearScreen();
    const requireValidEmail = (value) => {
        if (!value.includes("@"))
            return 'Inserted email is not valid!'

        return true;
    }

    const question = [
        {
            type: 'text',
            name: 'email',
            message: 'Please enter your email:',
            validate: requireValidEmail
        },
        {
            type: 'password',
            name: 'password',
            mask: '*',
            message: 'Please enter your password:',
        }]
    return enquirer.prompt(question);
}

