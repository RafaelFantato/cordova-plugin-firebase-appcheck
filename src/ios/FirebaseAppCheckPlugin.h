#import <Cordova/CDV.h>


@interface FirebaseAppCheckPlugin : CDVPlugin

- (void)pluginInitialize;
- (void)initialize:(CDVInvokedUrlCommand*)command;
- (void)getToken:(CDVInvokedUrlCommand*)command;
- (void)pingTest:(CDVInvokedUrlCommand*)command;

@end