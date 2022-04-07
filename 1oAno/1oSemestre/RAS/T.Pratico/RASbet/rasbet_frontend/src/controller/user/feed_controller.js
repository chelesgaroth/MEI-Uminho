import feedView from '../../view/user/feed_view.js';

import acknowledgePrompts from '../../view/common/common_prompts.js';
import utils from '../../view/common/utils.js';

import bettingSlipController from '../../controller/user/betting_slip_controller.js'
import event_service from '../../service/user/event_service.js';
import auth_service from '../../service/auth_service.js';


export default {
    feed
};


let events = []

async function feed() {

    
        const serverResponse = await event_service.getEventFeed();
       //  console.log(serverResponse)
        events = serverResponse.events;

        events.map(evt => {
            console.log(evt);
        })



        while (true) {
            // utils.clearScreen();
            console.log("===================================== Feed =====================================");

            
            // feedView.drawEventTable(events);
            
           
            const { option } = await feedView.showFeedOptions();

            if (option === 'PLACE_BET') {
                await placeBetScreen();
            }
            else if (option === 'BETTING_SLIP') {
                await bettingSlipScreen();
            }
            else
                break;
        }
    }



async function placeBetScreen() {
    /*
        Things needed to place a bet:
        1. Select a bet
        2. Select a team/odd
        3. Select the coin to use
        4. Enter an amount
        5. submit bet
    */



    // Ask the user for the number/id of the event he wants to bet on
    const { eventID } = await feedView.showSelectEventScreen(events);

    // Tried making a function that finds an event by its ID, but javascript sucks too much!
    //const selectedEvent = findEventByID(selectedEventNumber, filteredEvents);

    // Find the events chosen by the user (using its id)
    let selectedEvent = events.filter(evt => { return evt.idEvent === eventID})[0]
    
  
        
    // Ask user to choose an odd/team to bet on
    const { choiceID } = await feedView.showSelectTeamOddScreen(selectedEvent)
    console.log("Event: ")
    console.log(choiceID);


    let choice = {}
    for(let i = 0; i < selectedEvent.choices.length; i++) {
        if (selectedEvent.choices[i].idChoice === choiceID)  {
            choice = selectedEvent.choices[i];
            break;
        }
    }

    console.log(choice)
    
    // Ask user to select a coin
    const { selectedCoin } = await feedView.showSelectCoinScreen();

    // Ask user to enter an ammount to bet
    const { enteredAmount } = await feedView.showBetAmmountScreen();

    console.log("Choice odd: " + choice.odd)

    // Save bet to betting slip
    const bet = {
        eventID: eventID,
        choiceID: choiceID,
        odd: choice.odd,
        currencyUsed: selectedCoin,
        amount: enteredAmount,
        idUser: auth_service.getUserID()
    };

    

    bettingSlipController.addToBettingSlip(bet);

    await acknowledgePrompts.promptForAcknowledge("🙌 Bet Submitted to betting slip 🙌", "The submitted bet can be seen in your betting slip");

}

async function bettingSlipScreen() {
    await bettingSlipController.bettingSlipScreen()
}

async function filterScreen() {


    const { filterType, filterSearch } = await feedView.showFilterScreen();

    if (filterType === 'TEAM')
        currentFilter.team = filterSearch;
    else if (filterType === 'DATE')
        currentFilter.date = filterSearch;
    else if (filterType === 'SPORT')
        currentFilter.sport = filterSearch;

    const filteredBySport = Array.from(filteredEvents.filter((sportObj) => sportObj.sport.indexOf(currentFilter.sport) !== -1));



    let filteredByDate = []
    filteredBySport.map((sportObj) => {
        filteredByDate.push(
            {
                sport: sportObj.sport,
                events: sportObj.events.filter((evt) => evt.date.indexOf(currentFilter.date) !== -1)
            }
        );
    })

    // bet.date.indexOf(currentFilter.date) !== -1

    let filteredByTeam = []
    filteredByDate.map((sportObj) => {
        filteredByTeam.push(
            {
                sport: sportObj.sport,
                events: sportObj.events.filter(
                    (evt) =>
                        evt.teamA.toLowerCase().indexOf(currentFilter.team.toLowerCase()) !== -1
                        || evt.teamB.toLowerCase().indexOf(currentFilter.team.toLowerCase()) !== -1)
            }
        )
    });

    filteredEvents = Array.from(filteredByTeam)

}

