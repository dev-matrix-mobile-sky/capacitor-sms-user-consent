package com.mesoco.smsretrieved;

import android.util.Log;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "AndroidSmsRetrieved")
public class AndroidSmsRetrievedPlugin extends Plugin {

    AndroidSmsRetrieved implementation;

    @Override
    public void load() {
        super.load();
        try {
            implementation = new AndroidSmsRetrieved(getContext());
        } catch (Exception ex) {
            Log.d("load", ex.getMessage());
        }
    }

    @PluginMethod
    public void unregisterSmsReceiver(PluginCall call) {
        try {
            implementation.unregisterSmsReceiver();
        } catch (Exception ex) {
            Log.d("registerSmsReceiver", ex.getMessage());
        }
    }

    @PluginMethod
    public void registerSmsReceiver(PluginCall call) {
        try {
            implementation.registerSmsReceiver();
        } catch (Exception ex) {
            Log.d("registerSmsReceiver", ex.getMessage());
        }
    }

    @PluginMethod
    public void startSmsUserConsent(PluginCall call) {
        implementation.startSmsUserConsent(call);
    }
}
