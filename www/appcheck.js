var exec = require('cordova/exec');

var FirebaseAppCheck = {
    initialize: function (successCallback, errorCallback) {
        exec(
            successCallback,
            function (err) { 
                console.error("FirebaseAppCheck initialize failed:", err);
                if (errorCallback) errorCallback(err);
            },
            'FirebaseAppCheckPlugin', 
            'initialize', 
            []
        );
    },
    getToken: function (successCallback, errorCallback) {
        exec(
            function (token) {
                console.log("FirebaseAppCheck Token:", token);
                if (successCallback) successCallback(token);
            },
            function (err) { 
                console.error("FirebaseAppCheck getToken failed:", err);
                if (errorCallback) errorCallback(err);
            },
            'FirebaseAppCheckPlugin', 
            'getToken', 
            []
        );
    },
    pingTest: function (successCallback, errorCallback) {
        exec(
            function (data) {
                console.log("FirebaseAppCheck Data:", data);
                if (successCallback) successCallback(data);
            },
            function (err) { 
                console.error("FirebaseAppCheck getToken failed:", err);
                if (errorCallback) errorCallback(err);
            },
            'FirebaseAppCheckPlugin', 
            'pingTest', 
            []
        );
    }
};

module.exports = FirebaseAppCheck;
