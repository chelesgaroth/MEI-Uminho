import enquirer from 'enquirer'



export default {
    askSelectEvent,
    askEventResult,
    askConfirmationCloseEvent,
}

function askSelectEvent() {
    const question = [{
        type: 'input',
        name: 'eventID',
        message: `Enter the ID of the event you wish to close:`
    }]

    return enquirer.prompt(question);
}


function askEventResult(event) {
    const questions = [
        {
            type: 'select',
            name: 'eventResult',
            message: `Select the event's result:`,
            choices: [
                { name: 'TEAM_A',   message:  `${event.teamA} won` },
                { name: 'TIE',   message:  `Tie` },
                { name: 'TEAM_B',   message: `${event.teamB} won` },
            ]
        }
    ];

    return enquirer.prompt(questions)
  
}

function askConfirmationCloseEvent(eventID) {
    const questions = [
        {
            type: 'select',
            name: 'confirmed',
            message: `Are you sure you want to close event ${eventID} ?`,
            choices: [
                { name: 'YES',   message:  '👍 Yes, close event.' },
                { name: 'FALE',   message: '👎 No, go back.' },
            ]
        }
    ];

    return enquirer.prompt(questions)
  
}