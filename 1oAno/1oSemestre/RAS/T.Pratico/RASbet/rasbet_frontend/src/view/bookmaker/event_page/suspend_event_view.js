import enquirer from 'enquirer'

export default {
    askForEventID,
    askForBets
}

function askForEventID() {
    const question = [{
        type: 'input',
        name: 'eventID',
        message: `Enter the ID of the event:`
    }]

    return enquirer.prompt(question);
}

function askForBets(event, msg) {

    let choices = []
    const _1x2Bet = event.bets._1x2;
    



    const question = [{
        type: 'multiselect',
        name: 'selections',
        message: `${msg}`,
        hint: '(Use <space> to select, <return> to submit)',
        choices: [
            {name: 'TEAM1',  message: `🤼 ${event.teamA}: ${_1x2Bet.odd1}`},
            {name: 'TIE',    message:   `✖️ Tie: ${_1x2Bet.oddX}`},
            {name: 'TEAM2',  message: `🤼 ${event.teamB}: ${_1x2Bet.odd2}`}
        ],
        validate(value) {
            return value.length === 0 ? `Select at least one option.` : true;
        }
    }]

    return enquirer.prompt(question);
}