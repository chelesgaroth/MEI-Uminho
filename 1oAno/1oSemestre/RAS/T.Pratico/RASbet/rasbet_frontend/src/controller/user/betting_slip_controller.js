
import bettingSlipView from '../../view/user/betting_slip_view.js';
import acknowledgePrompts from '../../view/common/common_prompts.js';
import utils from '../../view/common/utils.js';
import bettingService from '../../service/user/betting_service.js';

export default {
    bettingSlipScreen,
    addToBettingSlip
}

let bettingSlip = [
    {
        date: '2021-07-15',
        eventID: 'C2',
        idEvent: 'E0',
        amount: 32.50,
        bettedOdd: 0.45,
        bettedTeam: "FCP",
        coin: "EUR",
        idUser: 'brunoadns',
        choiceID: 'C2'
    },
];

async function bettingSlipScreen() {

    if (bettingSlip.length > 0) {
        bettingSlipView.showBets(bettingSlip);
        console.log(bettingSlip)

        const { selectedOption } = await bettingSlipView.showOptions();

        if (selectedOption === 'SUBMIT') {
            await submitBettingSlip()
        }
        else if (selectedOption === 'REMOVE_BET') {
            await screenRemoveBet();
        }
        else if (selectedOption === 'CLEAR') {
            clearBettingSlip();
        }
        else if (selectedOption === 'RETURN') {
        }

    } else {
        utils.clearScreen();
        await acknowledgePrompts.promptForAcknowledge("😥 Betting Slip is Empty 😥", " ");
    }
}


function addToBettingSlip(bet) {
    bettingSlip.push(bet);
}

async function submitBettingSlip() {
    // Connect o backend and POST request the betting slip
    // Something like:
    // bettingService.submitBettingSlip(bettingSlip);

    try {
        const response = await bettingService.submitBettingSlip(bettingSlip)
        console.log(response);

        clearBettingSlip();
        await acknowledgePrompts.promptForAcknowledge('🙌 Bets successfully submitted 🙌', " ");
    } catch (error) {
        console.error(error);
    }

}

async function screenRemoveBet() {
    const { selectedBetNumber } = await bettingSlipView.showRemoveBet(bettingSlip.length);

    bettingSlip = bettingSlip.filter((_, index) => index != selectedBetNumber - 1);
}

function clearBettingSlip() {
    // Yes, this is the same as doing bettingSlip.clear() or bettinSlip = []
    bettingSlip.length = 0;
}
