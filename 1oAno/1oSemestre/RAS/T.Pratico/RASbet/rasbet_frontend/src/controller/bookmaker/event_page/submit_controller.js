import bookmakerService from '../../../service/bookmaker/bookmaker_service.js';
import submitView from '../../../view/bookmaker/event_page/submit_view.js'

import acknowledgePrompts from '../../../view/common/common_prompts.js';
import utils from '../../../view/common/utils.js';


export default {
    submitScreen,
}

async function submitScreen() {


    const { sport } = await submitView.askSport();

    const { competition } = await submitView.askCompetition();


    const dateTimes = await submitView.askDateTime();


    const {teams} = await submitView.askTeams();


    const betConfig = await submitView.askBet();


    // submit event to the backend

    try {
        bookmakerService.submitEvent(sport, competition, dateTimes, betConfig)
        // If success:
        // utils.clearScreen();
        await acknowledgePrompts.promptForAcknowledge(`✔️ Event Submitted ✔️`, "Press enter to continue...")
    } catch (error) {
        console.error(error);
    }

}