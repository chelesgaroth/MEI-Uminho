
import enquirer from 'enquirer'

import inquirer from 'inquirer'
inquirer.registerPrompt('datetime', import('inquirer-datepicker-prompt'))
import prompts from 'prompts'



function promptForAcknowledge(messageHeader, messageContent) {

    const questions = [
        {
            header: '==============================',
            type: 'list',
            message: `${messageHeader}\n${messageContent}`,
            name: 'selectedOption',
            choices: [
                'OK'
            ],
            footer: '=============================='
        }
    ];

    return enquirer.prompt(questions)
}

function promptDatePicker(msg) {

    var questions = [
        {
            type: 'date',
            name: 'date',
            message: `${msg}`,
            mask: 'YYYY-MM-DD',
        }]

    return prompts(questions);
}




export default {
    promptForAcknowledge,
    promptDatePicker,

}