
#import "RNBluetoothInfo.h"

// @implementation RNBluetoothInfo

// - (dispatch_queue_t)methodQueue
// {
//     return dispatch_get_main_queue();
// }
// RCT_EXPORT_MODULE()

// @end


@interface RNBluetoothInfo ()
@property (nonatomic, strong) CBCentralManager *bluetoothManager;
@end

@implementation RNBluetoothInfo

RCT_EXPORT_MODULE();

- (CBCentralManager *)bluetoothManager
{
  if (!_bluetoothManager) {
    _bluetoothManager = [[CBCentralManager alloc] initWithDelegate:self queue:dispatch_get_main_queue() options:@{CBCentralManagerOptionShowPowerAlertKey: @(NO)}];
  }
  
  return _bluetoothManager;
}

- (void)centralManagerDidUpdateState:(CBCentralManager *)central
{
  [self sendEventWithName:@"bluetoothDidUpdateState" body:@{@"connectionState": [self getConnectionState]}];
}

- (void)startObserving
{
  //[self sendEventWithName:@"bluetoothDidUpdateState" body:@{@"connectionState": [self getConnectionState]}];
}

- (void)stopObserving
{
  //_bluetoothManager = nil;
}


- (NSString *)getConnectionState
{
  NSString *bluetoothState = nil;
  switch(self.bluetoothManager.state)
  {
    case CBCentralManagerStateResetting: bluetoothState = @"resetting"; break;
    case CBCentralManagerStateUnsupported: bluetoothState = @"unsupported"; break;
    case CBCentralManagerStateUnauthorized: bluetoothState = @"unauthorized"; break;
    case CBCentralManagerStatePoweredOff: bluetoothState = @"off"; break;
    case CBCentralManagerStatePoweredOn: bluetoothState = @"on"; break;
    default: bluetoothState = @"unknown"; break;
  }
  NSLog(@"bluetoothState %@", bluetoothState);
  return bluetoothState;
}

- (NSArray<NSString *> *)supportedEvents
{
  return @[@"bluetoothDidUpdateState"];
}

#pragma mark - public method

RCT_EXPORT_METHOD(getCurrentState:(RCTPromiseResolveBlock)resolve
                  reject:(RCTPromiseRejectBlock)reject)
{
  if (!self.bluetoothManager) {
    NSError * error;
    reject(@"no_bluetooth_init", @"Bluetooth manager cannot be initialized", error);
  } else {
    resolve(@{@"connectionState": [self getConnectionState]});
  }

}

@end
  