import enquirer from 'enquirer'
import Table from 'cli-table3'
import SportIcon from '../../common/sport_icon.js'

export default {
    drawEventTable,
    showEventPageOptions
}

function drawEventTable(events) {
    events.map((obj) => {

        
            const table = new Table({
                head: betTableHeader(),
                colWidths: [8, 16, 16, 24, 16],
                // chars: { 'mid': '', 'left-mid': '', 'mid-mid': '', 'right-mid': '' }
            });

            const sportIcon = SportIcon.getSportIcon(obj.sportID);
            const sportName = obj.sport //`//`${obj.sport.charAt(0).toUpperCase()}${obj.sport.substring(1, obj.sport.length)}`
            console.log(`==> ${sportIcon} ${sportName}`)


            const eventList = obj.events;
            eventList.map((evnt) => {
                table.push(betTableRow(evnt))
            })

            console.log(table.toString());
            console.log('\n');

    })

}

function showEventPageOptions() {
    console.log("\n")

    const questions = [
        {
            type: 'select',
            name: 'option',
            message: 'Options:',
            choices: [
                { name: 'NEW_EVENT', message: '➕ Submit a new Event' },
                { name: 'CLOSE_EVENT', message: '❌ Close an Event' },
                { name: 'SUSPEND_EVENT', message: "🔒 Suspend an Event's bet" },
                { name: 'UNSUSPEND_EVENT', message: "🔓 Unsuspend an Event's bet" },
                { name: 'CANCEL_EVENT', message: "🛑 Cancel an Event" },
                { name: 'FILTER', message: '🔎 Filter/Search Events' },
                { name: 'CLEAR_FILTER', message: '↪️ Clear Filter' },
                { name: 'RETURN', message: '⬅️ Return to Menu' }
            ]
        }
    ];
    return enquirer.prompt(questions)
}


function showFilterScreen() {
    const questions = [
        {
            type: 'select',
            name: 'filterType',
            message: 'Available Actions: ',
            choices: [
                { name: 'TEAM', message: 'Filter by team name' },
                { name: 'DATE', message: 'Filter by date' },
                { name: 'SPORT', message: 'Filter by sport' },
            ]
        },

        {
            type: 'input',
            name: 'filterSearch',
            message: 'Enter you search:',
        }
    ];

    return enquirer.prompt(questions)
}


/* ============================================ */
/*             Table drawing helpers            */

function betTableHeader() {
    return [
        { hAlign: 'center', content: 'Nrº' },
        { hAlign: 'center', content: 'Date' },
        { hAlign: 'center', content: 'Start Time' },
        { hAlign: 'center', content: 'Teams' },
        { hAlign: 'center', colSpan: 3, colWidths: [null, 8], content: 'Odds' }
    ]
}

function betTableRow(evnt) {
    return [
        { hAlign: 'center', content: `42` },
        { hAlign: 'center', content: `${evnt.date}` },
        { hAlign: 'center', content: `${evnt.startTime}` },
        { hAlign: 'center', content: `${evnt.teamA} vs ${evnt.teamB}` },

        { hAlign: 'center', content: `${evnt.bets._1x2.odd1}` },
        { hAlign: 'center', content: `${evnt.bets._1x2.oddX}` },
        { hAlign: 'center', content: `${evnt.bets._1x2.odd2}` }

    ]

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