package com.gmail.jiangyang5157.tookit.android.biometrics.controller;

/**
 * Created by yangjiang on 24/04/17.
 */
public interface FingerprintAuthentication extends FingerprintAuthenticationHandler.AuthenticationCallback {

    boolean prepare();

    void startAuth();

    void stopAuth();
}