package com.valantic;

import android.util.Log;
import android.content.Context;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.AppCheckToken;
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory;

public class FirebaseAppCheckPlugin extends CordovaPlugin {

    private static final String TAG = "AppCheck";



    @Override
    protected void pluginInitialize() {
        try {
            Log.d(TAG, "pluginInitialize() iniciado.");
            
            Context context = this.cordova.getActivity().getApplicationContext();

            FirebaseOptions options = new FirebaseOptions.Builder()
                .setApplicationId("1:676134473685:android:21c04fd1d4ece0bf26b75c") // mobilesdk_app_id
                .setProjectId("unitedrentalsdev")
                .setApiKey("AIzaSyAdMyR4SErAIu36rAGGD6hAXvIzJTL0Vd0")
                .setGcmSenderId("676134473685") // project_number
                .setStorageBucket("unitedrentalsdev.firebasestorage.app") // optional
                .build();

            //FirebaseOptions options = FirebaseOptions.fromResource(context);

            if (options != null) {
                FirebaseApp.initializeApp(context, options);
                Log.d(TAG, "FirebaseApp inicializado com config explícita.");
            } else {
                Log.e(TAG, "FirebaseOptions.fromResource retornou null — verifique se o google-services.json está incluído.");
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro no pluginInitialize: " + e.getMessage(), e);
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
        try {
            FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
            PlayIntegrityAppCheckProviderFactory providerFactory = PlayIntegrityAppCheckProviderFactory.getInstance();
            firebaseAppCheck.installAppCheckProviderFactory(providerFactory);
            Log.d(TAG, "AppCheckProviderFactory instalado.");
            callbackContext.success("AppCheck Initialized");
        } catch (Exception e) {
            Log.e(TAG, "Erro ao inicializar AppCheck: " + e.getMessage(), e);
            callbackContext.error("Erro ao inicializar AppCheck: " + e.getMessage());
        }
    }



    private void pingTest(CallbackContext callbackContext) {
        callbackContext.success("AppCheck Ping");
    }

    private void getToken(final CallbackContext callbackContext) {
        try {
            Context context = cordova.getActivity() != null
                    ? cordova.getActivity().getApplicationContext()
                    : cordova.getContext();

            if (FirebaseApp.getApps(context).isEmpty()) {
                Log.w(TAG, "FirebaseApp não estava inicializado. Inicializando agora...");
                FirebaseApp.initializeApp(context);
            }

            FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
            firebaseAppCheck.getAppCheckToken(false)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        AppCheckToken token = task.getResult();
                        Log.d(TAG, "Token AppCheck obtido com sucesso.");
                        callbackContext.success(token.getToken());
                    } else {
                        String errorMsg = task.getException() != null
                                ? task.getException().getMessage()
                                : "Erro desconhecido";
                        Log.e(TAG, "Erro ao obter AppCheck token: " + errorMsg, task.getException());
                        callbackContext.error("Falha ao obter AppCheck token: " + errorMsg);
                    }
                });
        } catch (Exception e) {
            Log.e(TAG, "Exceção ao solicitar AppCheck token: " + e.getMessage(), e);
            callbackContext.error("Erro inesperado ao obter token: " + e.getMessage());
        }
    }

}