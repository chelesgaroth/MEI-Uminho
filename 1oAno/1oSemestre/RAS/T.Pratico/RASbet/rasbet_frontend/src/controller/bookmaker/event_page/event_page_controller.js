import eventPageView from '../../../view/bookmaker/event_page/event_page_view.js'


import bookmakerService from '../../../service/bookmaker/bookmaker_service.js';


import submitController from './submit_controller.js'
import filterController from './filter_controller.js'
import closeEventController from './close_event_controller.js';
import eventSuspensionController from './suspend_event_controller.js';
import cancelEventController from './cancel_event_controller.js'


export default {
    eventPage
}




async function eventPage() {

    let fetchedEvents = []

    try {
        fetchedEvents =  await bookmakerService.getBookmakerEvents()
        console.log(fetchedEvents);
    }
    catch (error) {
        console.error(error);
    }


    while (true) {
        // utils.clearScreen();
        console.log("===================================== My Events =====================================");

        if (fetchedEvents[0].length != 0) {
            const filteredEvents = fetchedEvents
            eventPageView.drawEventTable(filteredEvents);
        }

        const { option } = await eventPageView.showEventPageOptions();

        if (option === 'NEW_EVENT') {
            await submitController.submitScreen();
        }
        else if (option === 'CLOSE_EVENT') {
            await closeEventController.closeEventScreen(filteredEvents);
        }
        else if (option === 'SUSPEND_EVENT') {
            await eventSuspensionController.suspendEventScreen(filteredEvents);
        }
        else if (option === 'UNSUSPEND_EVENT') {
            await eventSuspensionController.unsuspendEventScreen(filteredEvents);
        }
        else if (option === 'CANCEL_EVENT') {
            await cancelEventController.cancelEventScreen(filteredEvents);
        }
        else if (option === 'FILTER') {
            await filterController.filterScreen(filteredEvents);
        }
        else if (option === 'CLEAR_FILTER') {
            filterController.clearFilter();
        }
        else if (option === 'RETURN') {
            break;

        }
        else {
            console.error("Invalid event page option!");
        }
    }




}