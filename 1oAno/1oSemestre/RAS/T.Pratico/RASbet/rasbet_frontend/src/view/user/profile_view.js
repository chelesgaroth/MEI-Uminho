
import enquirer from 'enquirer'
import utils from '../common/utils.js';


const showProfileScreen = (userData) => {
   // utils.clearScreen();
    console.log("==================== My Profile ====================");

    console.log(`🧍 name: ${userData.name}`);
    console.log(`📧 email: ${userData.email}`);
    console.log(`🔑 password: ${'*'.repeat(userData.password.length)}`);
    console.log(`🎂 date of birth: ${userData.dob}`);
    console.log("\n");



    const questions = [
        {
            type: 'select',
            name: 'option',
            message: 'Available Actions: ',
            choices: [
                {name: 'CHANGE_EMAIL', message:'Change Email Address'},
                {name: 'CHANGE_PASSWORD', message:'Change Password'},
                {name: 'RETURN', message:'Return to Menu'},
            ]
        }
    ]

    return enquirer.prompt(questions);
}

const showChangeEmailScreen = () => {
    const question = [
        {
            type: 'input',
            name: 'newEmail',
            message: 'Please enter your new email:',
        }]

    return enquirer.prompt(question);
}

const showChangePasswordScreen = () => {
    const question = [
        {
            type: 'password',
            name: 'newPassword',
            message: 'Please enter your new password:',
        }]

    return enquirer.prompt(question);
}

export default {
    showProfileScreen,
    showChangeEmailScreen,
    showChangePasswordScreen
};