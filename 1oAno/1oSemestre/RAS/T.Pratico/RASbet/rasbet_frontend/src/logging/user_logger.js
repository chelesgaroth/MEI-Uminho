import Signale from 'signale'

const options = {
    disabled: false,
    interactive: false,
    logLevel: 'info',
    secrets: [],
    stream: process.stdout,
    types: {
      remind: {
        badge: '**',
        color: 'yellow',
        label: 'reminder',
        logLevel: 'info'
      },
      santa: {
        badge: '🎅',
        color: 'red',
        label: 'santa',
        logLevel: 'info'
      },
      prettyInfo: {
        badge: '🙌',
        color: 'blue',
        label: '',
        logLevel: 'info'
      }
    }
  };

const logger = new Signale.Signale(options);

const infoSuccess = (message) => {
    logger.prettyInfo(message);
}

export default {
    infoSuccess,
};