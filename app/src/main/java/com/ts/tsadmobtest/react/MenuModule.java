package com.ts.tsadmobtest.react;

import android.app.Activity;
import android.widget.Toast;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.ts.tsadmobtest.MainActivity;

/**
 * Created by test on 2017-05-27.
 */

public class MenuModule extends ReactContextBaseJavaModule {
    public MenuModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "NativeMenu";
    }

    @ReactMethod
    public void distoryAdview() {
        Toast.makeText(getReactApplicationContext(), "call distoryAdview native from ReactNative", Toast.LENGTH_SHORT).show();

        Activity activity = getCurrentActivity();
        if(activity != null && activity instanceof MainActivity) {
//            ((MainActivity) activity).openDrawerMenu();
        }
    }

    @ReactMethod
    public void adAction() {
        Toast.makeText(getReactApplicationContext(), "call adAction native from ReactNative", Toast.LENGTH_SHORT).show();

        Activity activity = getCurrentActivity();
        if(activity != null && activity instanceof MainActivity) {
//            ((MainActivity) activity).openDrawerMenu();
        }
    }
}
