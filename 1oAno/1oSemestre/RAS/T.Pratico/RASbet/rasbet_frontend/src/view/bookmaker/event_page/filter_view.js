import enquirer from 'enquirer'

export default {
    showFilterOptions,
}

function showFilterOptions() {
    const questions = [
        {
            type: 'select',
            name: 'filterType',
            message: 'Available Actions: ',
            choices: [
                { name: 'TEAM', message: '🤼 Filter by team name' },
                { name: 'DATE', message: '🗓️ Filter by date' },
                { name: 'SPORT', message:'🏐 Filter by sport' },
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