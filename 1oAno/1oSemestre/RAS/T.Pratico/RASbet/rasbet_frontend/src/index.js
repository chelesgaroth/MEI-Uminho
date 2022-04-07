


import user_controller from './controller/user/user_controller.js'
import bookmaker_controller from './controller/bookmaker/bookmaker_controller.js'

// index.ts
import { Command } from 'commander';
const program = new Command();

program
  .option('-c, --client', 'start app in client mode')
  .option('-b, --bookmaker', 'start app in bookmaker mode')

program.parse(process.argv);

const options = program.opts();
if (options.client) 
    user_controller.userAppStart()

else if (options.bookmaker)
    bookmaker_controller.bookmakerAppStart()