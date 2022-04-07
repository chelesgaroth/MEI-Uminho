import bookmakerService from '../../../service/bookmaker/bookmaker_service.js';

import cancelEventView from '../../../view/bookmaker/event_page/cancel_event_view.js';


import acknowledgePrompts from '../../../view/common/common_prompts.js';
import utils from '../../../view/common/utils.js';


export default {
    cancelEventScreen
}

async function cancelEventScreen(events) {

    const { eventID } = await cancelEventView.askSelectEvent();


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

    // Connect to backend and cancel event
    // eventService.cancelEvent(eventID)
    bookmakerService.cancelEvent(eventID)
        .then(resp => console.log(resp));

    // utils.clearScreen();
    await acknowledgePrompts.promptForAcknowledge(`✔️ Event Canceled ✔️`, "Press enter to continue...")

}