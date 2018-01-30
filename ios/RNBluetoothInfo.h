
// #if __has_include("RCTBridgeModule.h")
// #import "RCTBridgeModule.h"
// #else
// #import <React/RCTBridgeModule.h>
// #endif


#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>
#import <CoreBluetooth/CoreBluetooth.h>

@interface RNBluetoothInfo : RCTEventEmitter <RCTBridgeModule, CBCentralManagerDelegate>


//@interface RNBluetoothInfo : NSObject <RCTBridgeModule>

@end
  