
import enquirer from 'enquirer'
import commonPrompts from '../../common/common_prompts.js';
import SportIcon from '../../common/sport_icon.js';
import utils from '../../common/utils.js';

export default {
    askSport,
    askCompetition,
    askDateTime,
    askBetType,
    ask1X2Config
}

function askSport() {

    /* We would get this from the backend service */
    const availableSports = [
        {sportName: 'Soccer', sportID: 'FOT'},
        {sportName: 'Basketball', sportID: 'BASK'},
        {sportName: 'F1', sportID: 'F1'},
    ]

    let sportChoices = []
    availableSports.map(sport => sportChoices.push(
        {
            name: sport.sportID,
            message: `${SportIcon.getSportIcon(sport.sportID)} ${sport.sportName}`
        }
    ))



    const questions = [
        {
            type: 'select',
            name: 'sport',
            message: 'Available Sports:',
            choices: sportChoices
        }
    ];

    return enquirer.prompt(questions)
}


function askCompetition() {

    const questions = [
        {
            type: 'select',
            name: 'competition',
            message: 'Available Competitions:',
            choices: [
                { name: "CL", message: "Champion's league" },
                { name: 'EL', message: 'European League' },
                { name: 'F1WC', message: 'Formula One World Championship' },
                { name: 'NBA', message: 'American Basketball Championship' },
            ]
        }
    ];

    return enquirer.prompt(questions)
}

async function askDateTime() {
    const {date} = await commonPrompts.promptDatePicker("Enter the event's date:");

    const timeQuestions = [
        {
            type: 'input',
            name: 'startTime',
            message: "Enter the event's start time:",
        }]

    const {startTime} = await enquirer.prompt(timeQuestions);

    return {date, startTime}
}

function askBetType() {
    const questions = [
        {
            type: 'select',
            name: 'betType',
            message: 'Select a bet type:',
            choices: [
                { enabled: true, name: '1X2', message: '1x2' },
                { role: 'separator' },
                { disabled: true, name: 'TOTAL', message: 'Total' },
                { disabled: true, name: 'BTTS', message: 'BTTS' },
            ]
        }
    ];

    return enquirer.prompt(questions)
}

function ask1X2Config() {
    const questions = [
        {
            type: 'select',
            name: 'team1',
            message: 'Select the first team:',
            choices: [
                { name: 'Racer3', message: 'Valtteri Bottas' },
                { name: 'SCB', message: 'SC Braga' },
                { name: 'SLB', message: 'Benfica' },
            ]
        },
        {
            type: 'input',
            name: 'team1Odd',
            message: "Enter the first team's odd:",
        },
        {
            type: 'select',
            name: 'team2',
            message: 'Select the second team:',
            choices: [
                { name: 'Racer3', message: 'Valtteri Bottas' },
                { name: 'SCB', message: 'SC Braga' },
                { name: 'SLB', message: 'Benfica' },
            ]
        },
        {
            type: 'input',
            name: 'team2Odd',
            message: "Enter the second team's odd:",
        }
    ];

    return enquirer.prompt(questions)
}