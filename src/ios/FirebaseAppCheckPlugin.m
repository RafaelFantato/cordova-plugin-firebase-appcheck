#import "FirebaseAppCheckPlugin.h"

@import FirebaseCore;
@import FirebaseAppCheck;

@implementation FirebaseAppCheckPlugin

- (void)pluginInitialize {
    NSLog(@"Starting Firebase AppCheck plugin");
    if (![FIRApp defaultApp]) {
        [FIRApp configure];
    }
}

- (void)initialize:(CDVInvokedUrlCommand*)command {
    if (![FIRApp defaultApp]) {
        [FIRApp configure];
    }

    [FIRAppCheck setAppCheckProviderFactory:[[FIRDeviceCheckProviderFactory alloc] init]];

    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"AppCheck Initialized"];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)getToken:(CDVInvokedUrlCommand*)command {
    [[FIRAppCheck appCheck] tokenForcingRefresh:NO completion:^(FIRAppCheckToken *_Nullable token, NSError *_Nullable error) {
        CDVPluginResult* pluginResult = nil;
        if (error != nil) {
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:[error localizedDescription]];
        } else {
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:token.token];
        }
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)pingTest:(CDVInvokedUrlCommand*)command {
    NSString *response = @"Plugin ativo e executando!";
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:response];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

@end
