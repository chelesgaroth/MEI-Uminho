import acknowledgePrompts from "../../view/common/common_prompts.js";

import recordService from "../../service/user/record_service.js";
import recordView from '../../view/user/record_view.js';


export default {
    record,
}

let userRecord = {};

async function record() {

    try {
        const userRecord = await recordService.getUserRecord();
        console.log(userRecord);

        
        while (true) {
            // TODO: This needs to change to use backends data
            /*
            recordView.showRecordScreenHeader(userRecord);
            recordView.showRecordScreenBets(userRecord.bets);
            */

            const { option } = await recordView.showRecordScreenOptions();


            if (option === 'RETURN') 
                break;
        }
        

    } catch (error) {
        console.error(error)
        await acknowledgePrompts.promptForAcknowledge("👎 Failed to fetch user record 👎",`Press enter to return`);
    }


}
