import suspendEventView from '../../../view/bookmaker/event_page/suspend_event_view.js'

import acknowledgePrompts from '../../../view/common/common_prompts.js';
import utils from '../../../view/common/utils.js';

import bookmakerService from '../../../service/bookmaker/bookmaker_service.js';

export default {
    suspendEventScreen,
    unsuspendEventScreen
}


async function suspendEventScreen(events) {


    const { eventID } = await suspendEventView.askForEventID();

    let allEvents = []
    const event = events.forEach(element => {
        element.events.forEach(evt => allEvents.push(evt))
    }); // (eventID, selectedOptions);


    let selectedEvent = allEvents.find(evt => evt.id === parseInt(eventID));
    if (selectedEvent == undefined) {
        await acknowledgePrompts.promptForAcknowledge(`❌ Invalid Event ID ❌`, "Please try again...")
        return;
    }

    const selections = await suspendEventView.askForBets(selectedEvent, "Select the bets you wish to suspend");

    // Tell backend to suspend the bets on this event
    bookmakerService.suspendBets(eventID, selections)
        .then(resp => console.log(resp))

    // On suspend success:
    //utils.clearScreen();
    await acknowledgePrompts.promptForAcknowledge(`✔️ Event Bets Suspended ✔️`, "Press enter to continue...")

}


async function unsuspendEventScreen(events) {
    const { eventID } = await suspendEventView.askForEventID();

    let allEvents = []
    const event = events.forEach(element => {
        element.events.forEach(evt => allEvents.push(evt))
    }); // (eventID, selectedOptions);


    let selectedEvent = allEvents.find(evt => evt.id === parseInt(eventID));
    if (selectedEvent == undefined) {
        utils.clearScreen();
        await acknowledgePrompts.promptForAcknowledge(`❌ Invalid Event ID ❌`, "Please try again...")
        return;
    }

    const selections = await suspendEventView.askForBets(selectedEvent, "Select the bets you wish to unsuspend");

    // Tell backend to suspend the bets on this event
    bookmakerService.unsuspendBets(eventID, selections)
        .then(resp => console.log(resp))

    // On suspend success:
    // utils.clearScreen();
    await acknowledgePrompts.promptForAcknowledge(`✔️ Event Bets Unsuspended ✔️`, "Press enter to continue...")
}