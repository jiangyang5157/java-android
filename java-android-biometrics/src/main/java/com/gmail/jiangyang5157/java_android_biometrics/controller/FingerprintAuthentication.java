package com.gmail.jiangyang5157.java_android_biometrics.controller;

/**
 * Created by yangjiang on 24/04/17.
 */
public interface FingerprintAuthentication extends FingerprintAuthenticationHandler.AuthenticationCallback {

    boolean prepare();

    void startAuth();

    void stopAuth();
}