import { Component, OnDestroy, OnInit } from '@angular/core';
import { IonicModule, Platform } from '@ionic/angular';
import { FormsModule } from '@angular/forms';
import { AndroidSmsRetrieved } from 'ion-sms-retrieved';
import { App } from '@capacitor/app';
import { Subscription, interval } from 'rxjs';

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
  standalone: true,
  imports: [IonicModule, FormsModule],
})
export class HomePage implements OnInit, OnDestroy {
  oneTimeCode: string;
  counter = 10;
  tick = 1000;
  enableResendBtn = false;
  intervalSubscription: Subscription | undefined;

  constructor(private platform: Platform,) {
    this.oneTimeCode = "";
  }

  ngOnInit() {
    this.startTimer();
    this.changeRegisterSmsPlugin(true);
    App.addListener('appStateChange', async ({ isActive }) => {
      console.log('App state changed. Is active?', isActive);
      this.changeRegisterSmsPlugin(isActive);
    });
    this.startSmsUserConsent();
  }

  ngOnDestroy() {
    this.intervalSubscription?.unsubscribe();
  }

  async changeRegisterSmsPlugin(isActive: boolean) {
    if (this.platform.is("android")) {
      if (isActive) {
        AndroidSmsRetrieved.registerSmsReceiver();
      }
      else {
        AndroidSmsRetrieved.unregisterSmsReceiver();
      }
    }
  }

  startSmsUserConsent() {
    if (this.platform.is("android")) {
      AndroidSmsRetrieved.startSmsUserConsent().then(({ otp }) => {
        this.oneTimeCode = otp;
      });
    }
  }

  resend() {
    this.counter = 10;
    this.enableResendBtn = false;
    this.startSmsUserConsent();
  }

  startTimer() {
    this.intervalSubscription = interval(this.tick).subscribe(() =>
      this.updateTimer()
    );
  }

  updateTimer() {
    if (this.counter > 0) {
      this.counter--;
      this.enableResendBtn = false;
    } else if (this.counter == 0) {
      this.enableResendBtn = true;
    } else {
      this.intervalSubscription?.unsubscribe();
    }
  }
}
