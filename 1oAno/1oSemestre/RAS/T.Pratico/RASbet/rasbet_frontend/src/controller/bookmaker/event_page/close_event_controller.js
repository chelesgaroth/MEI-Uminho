import bookmakerService from '../../../service/bookmaker/bookmaker_service.js';

import closeEventView from '../../../view/bookmaker/event_page/close_event_view.js';

import acknowledgePrompts from '../../../view/common/common_prompts.js';
import utils from '../../../view/common/utils.js';


export default {
    closeEventScreen
}

async function closeEventScreen(events) {

    const { eventID } = await closeEventView.askSelectEvent();


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


    const { eventResult } = await closeEventView.askEventResult(selectedEvent);
    const { confirmed } = await closeEventView.askConfirmationCloseEvent(eventID);

    if (confirmed === 'YES') {

        // Make a POST (or DELETE request)
        // eventService.closeEvent(eventID, eventResult);
        bookmakerService.closeEvent(eventID)
            .then(resp => console.log(resp));

        // On close success:
        // utils.clearScreen();
        await acknowledgePrompts.promptForAcknowledge(`✔️ Event Deleted ✔️`, "Press enter to continue...")
    }

}