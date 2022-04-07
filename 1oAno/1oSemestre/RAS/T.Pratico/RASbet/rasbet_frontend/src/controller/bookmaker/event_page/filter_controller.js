
import filterView from '../../../view/bookmaker/event_page/filter_view.js'

export default {
    filterScreen,
    applyFilter,
    clearFilter
}

let currentFilter = {
    team: '',
    date: '',
    sport: '',
};

let filteredEvents = []


async function filterScreen(events) {

    filteredEvents = Array.from(events);

    const { filterType, filterSearch } = await filterView.showFilterOptions();

    if (filterType === 'TEAM')
        currentFilter.team = filterSearch;
    else if (filterType === 'DATE')
        currentFilter.date = filterSearch;
    else if (filterType === 'SPORT')
        currentFilter.sport = filterSearch;

}


function applyFilter(events) {
    filteredEvents = Array.from(events);

    const filteredBySport = Array.from(filteredEvents.filter((sportObj) => sportObj.sport.indexOf(currentFilter.sport) !== -1));

    let filteredByDate = []
    filteredBySport.map((sportObj) => {
        filteredByDate.push(
            {
                ...sportObj,
                events: sportObj.events.filter((evt) => evt.date.indexOf(currentFilter.date) !== -1)
            }
        );
    })


    // bet.date.indexOf(currentFilter.date) !== -1

    let filteredByTeam = []
    filteredByDate.map((sportObj) => {
        filteredByTeam.push(
            {
                ...sportObj,
                events: sportObj.events.filter(
                    (evt) =>
                        evt.teamA.toLowerCase().indexOf(currentFilter.team.toLowerCase()) !== -1
                        || evt.teamB.toLowerCase().indexOf(currentFilter.team.toLowerCase()) !== -1)
            }
        )
    });

    console.log("Filtered by team", filteredByTeam);



    return filteredByTeam;
}

  
function clearFilter () {
    currentFilter.date = '';
    currentFilter.team = '';
    currentFilter.sport = '';
}