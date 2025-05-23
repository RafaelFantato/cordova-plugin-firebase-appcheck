package com.valantic;

import android.util.Log;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;

import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.AppCheckToken;
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory;

public class FirebaseAppCheckPlugin extends CordovaPlugin {

    private static final String TAG = "AppCheck";

    @Override
    protected void pluginInitialize() {
        super.pluginInitialize();
        try {
            Log.d(TAG, "Inicializando FirebaseApp...");

            if (FirebaseApp.getApps(cordova.getContext()).isEmpty()) {
                FirebaseApp.initializeApp(cordova.getActivity().getApplicationContext());
                Log.d(TAG, "FirebaseApp inicializado.");
            } else {
                Log.d(TAG, "FirebaseApp já estava inicializado.");
            }

            FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
            PlayIntegrityAppCheckProviderFactory providerFactory = PlayIntegrityAppCheckProviderFactory.getInstance();
            firebaseAppCheck.installAppCheckProviderFactory(providerFactory);

            Log.d(TAG, "FirebaseAppCheck inicializado com Play Integrity.");
        } catch (Exception e) {
            Log.e(TAG, "Erro ao inicializar AppCheck: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        Log.d(TAG, "Executando ação: " + action);

        if ("initialize".equals(action)) {
            initializeAppCheck(callbackContext);
            return true;
        } else if ("getToken".equals(action)) {
            getToken(callbackContext);
            return true;
        } else if ("pingTest".equals(action)) {
            pingTest(callbackContext);
            return true;
        }
        return false;
    }

    private void initializeAppCheck(CallbackContext callbackContext) {
        callbackContext.success("AppCheck Initialized");
    }


    private void pingTest(CallbackContext callbackContext) {
        callbackContext.success("AppCheck Ping");
    }

    private void getToken(final CallbackContext callbackContext) {
        try {
            FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
            firebaseAppCheck.getAppCheckToken(false)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        AppCheckToken token = task.getResult();
                        callbackContext.success(token.getToken());
                        Log.d(TAG, "Token recebido com sucesso.");
                    } else {
                        String errorMsg = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                        Log.e(TAG, "Erro ao obter AppCheck token: " + errorMsg, task.getException());
                        callbackContext.error("Failed to get AppCheck token: " + errorMsg);
                    }
                });
        } catch (Exception e) {
            Log.e(TAG, "Exceção ao solicitar AppCheck token: " + e.getMessage(), e);
            callbackContext.error("Erro inesperado ao obter token: " + e.getMessage());
        }
    }
}