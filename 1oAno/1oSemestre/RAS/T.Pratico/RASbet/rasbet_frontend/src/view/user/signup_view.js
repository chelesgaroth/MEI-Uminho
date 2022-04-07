import enquirer from 'enquirer'
import commonPrompts from '../common/common_prompts.js'
import utils from '../common/utils.js';

export default {
    showSignup,
    showDOBPicker
};

function showSignup ()  {
    // utils.clearScreen();
    const requireNumberAndMinLength = (value) => {
        if (/\d/.test(value) && value.length >= 8)
            return true;

        return 'Passwords must have at least one number and must be at least 8 characters long.'
    }

    const requireValidEmail = (value) => {
        if (!value.includes("@"))
            return 'Inserted email is not valid!'

        return true;
    }

    const question = [
        {
            type: 'text',
            name: 'name',
            message: 'Please enter your name:',
            initial: 'example: John Connor',
        },
        {
            type: 'text',
            name: 'username',
            message: 'Please enter your username:',
            initial: 'example: John Connor',
        },
        {
            type: 'text',
            name: 'email',
            message: 'Please enter your email:',
            initial: 'example: johnConnor@email.com',
            validate: requireValidEmail
        },
        {
            type: 'password',
            name: 'password',
            mask: '*',
            message: 'Please enter a password:',
            validate: requireNumberAndMinLength
        }]

    return enquirer.prompt(question);
}

function showDOBPicker () {

   return commonPrompts.promptDatePicker("Enter your Date of Birth: ");
}

