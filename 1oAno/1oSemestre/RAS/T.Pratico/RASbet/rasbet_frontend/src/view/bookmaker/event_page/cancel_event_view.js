import enquirer from 'enquirer'

export default {
    askSelectEvent,
}

function askSelectEvent() {
    const question = [{
        type: 'input',
        name: 'eventID',
        message: `Enter the ID of the event:`
    }]

    return enquirer.prompt(question);
}
