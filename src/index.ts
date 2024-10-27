import { registerPlugin } from '@capacitor/core';

import type { AndroidSmsRetrievedPlugin } from './definitions';

const AndroidSmsRetrieved = registerPlugin<AndroidSmsRetrievedPlugin>(
  'AndroidSmsRetrieved',
  {
    // web: () => import('./web').then(m => new m.AndroidSmsRetrievedWeb()),
  },
);

export * from './definitions';
export { AndroidSmsRetrieved };
