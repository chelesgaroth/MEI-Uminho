import enquirer from 'enquirer'
import Table from 'cli-table3'
import utils from '../common/utils.js';


function showRecordScreenHeader(userRecord) {
    utils.clearScreen();
    console.log("==================== 📈 My Record ====================");

    console.log(`🟢  Total won bets: ${userRecord.winCount}`);
    console.log(`🔴 Total lost bets: ${userRecord.lossCount}`);
}

function showRecordScreenBets(bets) {
    console.log(bets)
    console.log("\n")

    const table = new Table({
        head: ['Date', 'Time', 'Teams', 'Bet Result', 'Amount Spent', 'Amount Won'],
        colWidths: [16, 16, 24, 16, 16, 16],
        chars: { 'mid': '', 'left-mid': '', 'mid-mid': '', 'right-mid': '' }
    });


    bets.map((bet) => {
        const winStatus = bet.won ? '🟢' : '🔴';

        table.push([
            `${bet.date}`,
            `${bet.startTime}`,
            `${bet.teamA} vs ${bet.teamB}`,
            `${winStatus}`,
            `${bet.amountSpent} ${bet.coin}s`,
            `${bet.amountWon} ${bet.coin}s`,
        ])
    });

    console.log(table.toString())

}

function showRecordScreenOptions() {
    console.log("\n")

    const questions = [
        {

            type: 'select',
            name: 'option',
            message: 'Options: ',
            choices: [
               // { name: 'FILTER', message: 'Filtered Search' },
                // { name: 'CLEAR_FILTER', message: 'Clear Filter' },
                { name: 'RETURN', message: 'Return to Menu' }
            ]
        }
    ];
    return enquirer.prompt(questions)
}



export default {
    showRecordScreenHeader,
    showRecordScreenBets,
    showRecordScreenOptions,
    
}