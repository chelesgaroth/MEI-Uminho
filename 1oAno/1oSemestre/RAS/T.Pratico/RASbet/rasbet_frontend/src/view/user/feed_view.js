import enquirer from 'enquirer'
import Table from 'cli-table3'
import SportIcon from '../common/sport_icon.js';



export default {
    drawEventTable,
    showFeedOptions,

    showSelectEventScreen,
    showSelectTeamOddScreen,
    showSelectCoinScreen,
    showBetAmmountScreen
};


// function show 
function drawEventTable(events) {

    /*
    let sportSet = new Set()
    let sportEventDict = {}
    events.forEach(evt => {
        sportSet.add(evt.sport)
        sportEventDict[evt.sport] = []
    });

    events.forEach(evt => {
        sportEventDict[evt.sport].push(evt)
    });

    console.log(sportEventDict)


    sportSet.map(sprtEvent => console.log(sprtEvent))
    sportEventDict.forEach(sprt => {
        const table = new Table({
            head: betTableHeader(),
            
            // ID, Date, Status, Participants, Choices
            colWidths: [8, 16, 8, 8, 16,16,16,16,8,8,8],
            // chars: { 'mid': '', 'left-mid': '', 'mid-mid': '', 'right-mid': '' }
        });


    })


    console.log(events)
   

    events.map((eventObject) => {

        table.push(
            betTableRow(eventObject)
        )
    });

    console.log(table.toString());
    console.log('\n');
    */
}

function showFeedOptions() {
    console.log("\n")

    const questions = [
        {
            type: 'select',
            name: 'option',
            message: 'Options:',
            choices: [
                { name: 'PLACE_BET', message: 'Place a Bet' },
                { name: 'BETTING_SLIP', message: 'My Betting Slip' },
                { name: 'FILTER', message: 'Filter/Search Bets' },
                { name: 'CLEAR_FILTER', message: 'Clear Filter' },
                { name: 'RETURN', message: 'Return to Menu' }
            ]
        }
    ];
    return enquirer.prompt(questions)
}



/* ============================================ */
/*             Betting Screen                   */

function showSelectEventScreen(events) {
    // console.clear();
    drawEventTable(events);

    const questions = [
        {
            type: 'input',
            name: 'eventID',
            message: 'Enter the event ID:',
        }
    ];

    return enquirer.prompt(questions);
}

function showSelectTeamOddScreen(event) {
    // console.clear();
    console.log(`Event ID: ${event.idEvent}`);
    console.log(`Date: ${event.initialDate}`);
    console.log(`${event.participants[0].name} vs ${event.participants[1].name}`);

    console.log("\nAvailable bets: ");

    let availableChoices = [];
    if (event.sport === 'Football') {
        availableChoices.push({name: event.choices.idChoice, message: `${event.participants[0].name} : ${event.choices[0].odd}`});
        availableChoices.push({name: event.choices[1].idChoice, message: `Tie : ${event.choices[1].odd}`});
        availableChoices.push({name: event.choices[2].idChoice, message: `${event.participants[1].name} : ${event.choices[2].odd}`});

    }else if (event.sport === 'Basketball') {
        availableChoices.push({name: event.choices[0].idChoice, message: `${event.participants[0].name} : ${event.choices[0].odd}`})

    } else if (event.sport === 'F1') {
        event.choices.map((choice, i) => {
            availableChoices.push({name: choice.idChoice, message: `${event.participants[i].name} : ${choice.odd}`});
        })
    }

    const questions = [
        {
            type: 'select',
            name: 'choiceID',
            message: 'Options:',
            choices: availableChoices,
        }
    ];
    return enquirer.prompt(questions)
}

function showSelectCoinScreen() {
    // console.clear();

    const questions = [
        {
            type: 'select',
            name: 'selectedCoin',
            message: 'Which coin should be used?',
            choices: [
                { name: 'EURO', message: 'Euro', value: 'Euro' },
                { name: 'USD', message: 'USD', value: 'USD' },
                { role: 'separator' },
                { name: 'BITCOIN', message: 'Bitcoin', value: 'Bitcoin' },
                { name: 'ETHEREUM', message: 'Ethereum', value: 'Ethereum' },
            ]
        }
    ];
    return enquirer.prompt(questions)
}

function showBetAmmountScreen() {
    // console.clear();

    const questions = [
        {
            type: 'numeral',
            name: 'enteredAmount',
            message: 'Enter the amount you want bet:',
        }
    ];

    return enquirer.prompt(questions);
}



/* ============================================ */
/*             Table drawing helpers            */

function betTableHeader() {
    return [
        { hAlign: 'center', content: 'Nrº' },
        { hAlign: 'center', content: 'Date' },
        { hAlign: 'center', content: 'Status' },
        { hAlign: 'center', content: 'Participants' },
        { hAlign: 'center', colSpan: 3, colWidths: [null, 8], content: 'Odds' }
    ]
}

function betTableRow(eventObj) {

    let rowContents = [  
        { hAlign: 'center', content: `${eventObj.idEvent}` },
        { hAlign: 'center', content: `${eventObj.initialDate}` },
        { hAlign: 'center', content: `${eventObj.eventStatus}` },
        { hAlign: 'center', content: `${eventObj.idCompetition}` },
        // { hAlign: 'center', content: `${eventObj.participants[0].name} vs ${eventObj.participants[1].name}` }
    ]
    console.log(eventObj.participants);
    console.log(eventObj.choices);




    if (eventObj.choices.length === 3)
    {
        // 1x2
        eventObj.participants.map((participant, i) => {
            rowContents.push(
                 { hAlign: 'center', content: `${eventObj.choices[i].idChoice}:${participant.name}:${eventObj.choices[i].odd}` }
            );
        })
    }
    const betConfig = rowBetConfig(eventobj);
    
    eventObj.participants.map(participant => {
        rowContents.push(
             { hAlign: 'center', content: `${participant.name}` }
        );
    })

    eventObj.choices.map(choice => {
        rowContents.push(
            { hAlign: 'center', content: `${choice.odd}` }
        )
    })


    return rowContents;
}

function draw1x2BetTable(event, eventOdds) {
    const tableHeader = [
        { hAlign: 'center', content: `${event.teamA} Odd` },
        { hAlign: 'center', content: `Tie Odd` },
        { hAlign: 'center', content: `${event.teamB} Odd` },
    ]

    const table = new Table({
        head: tableHeader,
        colWidths: [16, 16, 16]
        // chars: { 'mid': '', 'left-mid': '', 'mid-mid': '', 'right-mid': '' }
    });

    const tableRow = [
        { hAlign: 'center', content: `${eventOdds.odd1}` },
        { hAlign: 'center', content: `${eventOdds.oddX}` },
        { hAlign: 'center', content: `${eventOdds.odd2}` },
    ]

    table.push(tableRow);

    console.log(table.toString());
}