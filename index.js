
import { 
  NativeAppEventEmitter,
  NativeModules,
  NativeEventEmitter,
  Platform
} from 'react-native';

const { RNBluetoothInfo } = NativeModules;
const BluetoothInfoEventEmitter = new NativeEventEmitter(RNBluetoothInfo);
const DEVICE_CONNECTIVITY_EVENT = 'bluetoothDidUpdateState';

type ChangeEventName = $Enum<{
  change: string,
}>;

const _subscriptions = new Map();

const BluetoothInfo = {
  /**
   * Adds an event handler. Supported events:
   *
   * - `change`: Fires when the network status changes. The argument to the event
   *   handler is an object with key:
   *   - `type`: A `ConnectionType` (listed above)
   */
  addEventListener(
    eventName: ChangeEventName,
    handler: Function
  ): {remove: () => void} {
    let listener;
    if (eventName === 'change') {
      listener = BluetoothInfoEventEmitter.addListener(
        DEVICE_CONNECTIVITY_EVENT,
        (appStateData) => {
          handler({
            type: appStateData,
          });
        }
      );
    } else {
      console.warn('Trying to subscribe to unknown event: "' + eventName + '"');
      return {
        remove: () => {}
      };
    }

    _subscriptions.set(handler, listener);
    return {
      remove: () => BluetoothInfo.removeEventListener(eventName, handler)
    };
  },

  /**
   * Removes the listener for network status changes.
   */
  removeEventListener(
    eventName: ChangeEventName,
    handler: Function
  ): void {
    const listener = _subscriptions.get(handler);
    if (!listener) {
      return;
    }
    listener.remove();
    _subscriptions.delete(handler);
  },

  /**
   * Returns a promise that resolves to an object with `type key
   */
  getCurrentState(): Promise<any> {
    return RNBluetoothInfo.getCurrentState().then(resp => {
      return {
        type: resp,
      };
    });
  }
};

export default BluetoothInfo;
//export default RNBluetoothInfo;
