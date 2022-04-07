import enquirer from 'enquirer'


function showWalletScreen(wallet) {
    
    console.log("==================== 💱 My Wallet ====================");


    /*
        This part has to be automatized. (maybe if statements for each coin)
        This is just for testing purposes.
    */
   console.log("Current fund balance:");
    console.log(` ₿ - Bitcoin: ${ wallet.BTC != undefined ? wallet.BTC : 0.0}`);
    console.log(`💶 - Euros: ${wallet.EUR != undefined ? wallet.EUR : 0.0}`);
    console.log(`💵 - USD: ${wallet.USD != undefined ? wallet.USD : 0.0}`);
    
    console.log("\n")


    const questions = [
        {
          
            type: 'select',
            name: 'option',
            message: 'Available Actions: ',
            choices: [
                {name: 'DEPOSIT', message:'Deposit Funds'},
                {name: 'WITHDRAW',  message:'Withdraw Funds'},
                {name: 'RETURN',  message:'Return to Menu'}
            ]
        }
    ];
    return enquirer.prompt(questions)
}


function showWantToDepositScreen() {
    const question = {
        type: 'select',
        name: 'selectedOption',
        message: 'Do you wish to deposit the initial funds for your wallet?',
        choices: ['Yes', 'No']
    };

    return enquirer.prompt(question);
}



async function showDepositScreen() {
    console.clear()
    const coinPrompt = new enquirer.Select(
        {
            name: 'selectedCoin',
            message: 'Select deposit method/coin:',
            choices: [
                { name: 'EUR', message: 'Euro', value: 'EUR' },
                { name: 'USD', message: 'USD', value: 'USD' },
                { role: 'separator' },
                { name: 'BTC', message: 'Bitcoin', value: 'BTC' },
            ]
        });

    const selectedCoin = await coinPrompt.run();


    const amountPrompt = new enquirer.NumberPrompt({
        message: 'Insert the amount you wish to deposit:'
    });
    const amount = await amountPrompt.run();

    return {coin: selectedCoin, quantity: amount};
}

async function showWithdrawScreen() {
    console.clear()
    const coinPrompt = new enquirer.Select(
        {
            name: 'selectedCoin',
            message: 'Which coin is to be withdrawn?',
            choices: [
                { name: 'EUR', message: 'Euro', value: 'EUR' },
                { name: 'USD', message: 'USD', value: 'USD' },
                { role: 'separator' },
                { name: 'BTC', message: 'Bitcoin', value: 'BTC' },
            ]
        });

    const selectedCoin = await coinPrompt.run();

    const amountPrompt = new enquirer.NumberPrompt({
        message: 'Insert the amount you wish to withdraw:'
    });
    const amount = await amountPrompt.run();

    return {coin: selectedCoin, quantity: amount};
}


export default {
    showWalletScreen,
    showWantToDepositScreen,
    showDepositScreen,
    showWithdrawScreen,
};