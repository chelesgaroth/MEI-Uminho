import acknowledgePrompts from '../../view/common/common_prompts.js';
import walletView from '../../view/user/wallet_view.js'
import utils from '../../view/common/utils.js';

import walletService from '../../service/user/wallet_service.js';

export default {
    wallet,
    askIfWantsToDeposit,
    depositScreen
};


async function wallet() {
    try {
        const wallet = await walletService.getWallet();
        console.log(wallet);

        const { option } = await walletView.showWalletScreen(wallet);

        if (option === 'DEPOSIT') {
            await depositScreen()
        }
        else if (option === 'WITHDRAW') {
            await withdrawScreen()
        }
        else
            return;


    } catch (error) {
        console.error(error);
        await acknowledgePrompts.promptForAcknowledge(`👎 Failed to fetch wallet. 👎`, "Press enter to try again...")
    }

}



async function askIfWantsToDeposit() {
    utils.clearScreen();
    const { selectedOption } = await walletView.showWantToDepositScreen();


    return selectedOption === 'Yes';
}



async function depositScreen() {
    const { coin, quantity } = await walletView.showDepositScreen();

    // Connect to backend and peform the actual deposit on this user's wallet
    try {
        const response = walletService.deposit(coin, quantity)
        console.log(response);

        // On sucessfull response:
        await acknowledgePrompts
            .promptForAcknowledge(
                "🙌 Deposit successfull 🙌",
                `     Deposited ${quantity} ${coin}`
            );
    } catch (error) {
        console.error(error);
    }

}

async function withdrawScreen() {
    const { coin, quantity } = await walletView.showWithdrawScreen();

    try {
        // Connect to backend and perform the withdrawall
        const response = await walletService.withdraw(coin, quantity)
        console.log(response);

        await acknowledgePrompts
            .promptForAcknowledge(
                "🙌 Withdraw successfull 🙌",
                `Withdrawed ${quantity} ${coin}`
            );
    } catch (error) {
        console.error(error);
        await acknowledgePrompts .promptForAcknowledge("👎 Withdraw failed 👎",`Press enter to return`);
    }

}

