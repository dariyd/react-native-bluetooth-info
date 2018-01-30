
package com.reactlibrary;

import android.support.annotation.Nullable;
import android.app.Activity;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.bridge.Promise;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;

import android.bluetooth.BluetoothAdapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.util.Log;

import com.facebook.react.bridge.*;
import com.facebook.react.modules.core.RCTNativeAppEventEmitter;
import org.json.JSONException;
import java.util.HashMap;
import java.util.Map;

public class RNBluetoothInfoModule extends ReactContextBaseJavaModule implements ActivityEventListener {

  public static final String REACT_CLASS = "RNBluetoothInfo";
  public static final String LOG_TAG = "logs";
  private ReactApplicationContext reactContext;

  private BluetoothAdapter bluetoothAdapter;
  private Context context;

  //private final ReactApplicationContext reactContext;

  // public RNBluetoothInfoModule(ReactApplicationContext reactContext) {
  //   super(reactContext);
  //   this.reactContext = reactContext;
  // }

  public RNBluetoothInfoModule(ReactApplicationContext reactContext) {
        super(reactContext);
        context = reactContext;

        this.reactContext = reactContext;
        reactContext.addActivityEventListener(this);
        registerBluetoothStateReceiver();
        Log.d(LOG_TAG, "BluetoothStateModule created");
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    private BluetoothAdapter getBluetoothAdapter() {

        if (bluetoothAdapter == null) {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        return bluetoothAdapter;
    }


    @ReactMethod
    public void getCurrentState(Promise promise) {
        try {

            BluetoothAdapter adapter = getBluetoothAdapter();
            String state = "unknown";
            if (adapter != null) {
                switch (adapter.getState()) {
                    case BluetoothAdapter.STATE_ON:
                        state = "on";
                        break;
                    case BluetoothAdapter.STATE_OFF:
                        state = "off";
                }
            }

            WritableMap map = Arguments.createMap();
            map.putString("connectionState", state);

            Log.d(LOG_TAG, "connectionState:" + state);

            promise.resolve(map);
        } catch (Exception e) {
            promise.reject("NO_BLUETOOTH", e);
        }
    }

  private void registerBluetoothStateReceiver() {
      Log.d(LOG_TAG, "registerBluetoothStateReceiver");
      if (getBluetoothAdapter() == null) {
          Log.d(LOG_TAG, "NO_BLUETOOTH");
          return;
      }

      IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
      context.registerReceiver(mReceiver, filter);
  }

  private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
          Log.d(LOG_TAG, "onReceive");
          final String action = intent.getAction();

          if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
              final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                      BluetoothAdapter.ERROR);
              String stringState = "";

              switch (state) {
                  case BluetoothAdapter.STATE_OFF:
                      stringState = "off";
                      break;
                  case BluetoothAdapter.STATE_TURNING_OFF:
                      stringState = "turning_off";
                      break;
                  case BluetoothAdapter.STATE_ON:
                      stringState = "on";
                      break;
                  case BluetoothAdapter.STATE_TURNING_ON:
                      stringState = "turning_on";
                      break;
              }

              WritableMap map = Arguments.createMap();
              map.putString("connectionState", stringState);

              Log.d(LOG_TAG, "connectionState: " + stringState);
              emitDeviceEvent("bluetoothDidUpdateState", map);

          }
      }
  };


  private void emitDeviceEvent(String eventName, @Nullable WritableMap eventData) {
      // A method for emitting from the native side to JS
      // https://facebook.github.io/react-native/docs/native-modules-android.html#sending-events-to-javascript

      if (reactContext.hasActiveCatalystInstance()) {
          Log.d(LOG_TAG, "Sending event: " + eventName);
          reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, eventData);
          Log.d(LOG_TAG, "emitDeviceEvent: " + eventName);
      }
  }

  @Override
  public void onNewIntent(Intent intent) {

  }

  @Override
  public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {

  }

  // @Override
  // public String getName() {
  //   return "RNBluetoothInfo";
  // }
}