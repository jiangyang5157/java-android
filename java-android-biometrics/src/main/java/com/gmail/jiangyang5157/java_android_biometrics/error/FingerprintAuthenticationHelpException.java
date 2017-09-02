package com.gmail.jiangyang5157.java_android_biometrics.error;

/**
 * Created by yangjiang on 24/04/17.
 */
public class FingerprintAuthenticationHelpException extends BiometricsException {

    private int helpMsgId;

    public FingerprintAuthenticationHelpException(int helpMsgId, String message) {
        super(message);
        this.helpMsgId = helpMsgId;
    }

    public int getHelpMsgId() {
        return helpMsgId;
    }
}
