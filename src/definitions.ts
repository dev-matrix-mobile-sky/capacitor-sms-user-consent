import { registerPlugin } from '@capacitor/core';

export interface AndroidSmsRetrievedPlugin {
  registerSmsReceiver(): any;
  unregisterSmsReceiver(): any;
  startSmsUserConsent(): Promise<{ otp: string }>;
}

const AndroidSmsRetrieved = registerPlugin<AndroidSmsRetrievedPlugin>('AndroidSmsRetrieved');

export default AndroidSmsRetrieved;