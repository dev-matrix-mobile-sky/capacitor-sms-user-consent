package com.mesoco.smsretrieved;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.getcapacitor.BridgeActivity;
import com.getcapacitor.JSObject;
import com.getcapacitor.PluginCall;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AndroidSmsRetrieved {
    public static final int RESULT_OK = -1;
    String callbackId;
    BridgeActivity activity;
    ActivityResultLauncher<Intent> startActivityIntent;

    public AndroidSmsRetrieved(Context context) {
        activity = (BridgeActivity)context;
        startActivityIntent = activity
                .registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> onActivityResult(result));
    }

    public void registerSmsReceiver() {
        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        activity.registerReceiver(smsBroadcastReceiver, intentFilter);
    }

    public void unregisterSmsReceiver() {
        activity.unregisterReceiver(smsBroadcastReceiver);
    }

    public void startSmsUserConsent(PluginCall call) {
        call.setKeepAlive(true);
        callbackId = call.getCallbackId();
        activity.getBridge().saveCall(call);

        SmsRetrieverClient client = SmsRetriever.getClient(activity);
        //We can add sender phone number or leave it blank
        // I'm adding null here
        client.startSmsUserConsent(null);
    }

    BroadcastReceiver smsBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Objects.equals(intent.getAction(), SmsRetriever.SMS_RETRIEVED_ACTION)) {
                Bundle extras = intent.getExtras();
                Status smsRetrieverStatus = (Status) extras.get(SmsRetriever.EXTRA_STATUS);
                switch (smsRetrieverStatus.getStatusCode()) {
                    case CommonStatusCodes.SUCCESS:
                        Intent messageIntent = extras.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT);
                        startActivityIntent.launch(messageIntent);
                        break;
                    case CommonStatusCodes.TIMEOUT:
                        break;
                }
            }
        }
    };

    private void onActivityResult(ActivityResult result) {
        if ((result.getResultCode() == RESULT_OK) && (result.getData() != null)) {
            //That gives all message to us.
            // We need to get the code from inside with regex
            String message = result.getData().getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
            Pattern pattern = Pattern.compile("(|^)\\d{6}");
            Matcher matcher = pattern.matcher(message);
            if (matcher.find()) {
                JSObject ret = new JSObject();
                ret.put("otp", matcher.group(0));
                activity.getBridge().getSavedCall(callbackId).resolve(ret);
            }
        }
    }
}
