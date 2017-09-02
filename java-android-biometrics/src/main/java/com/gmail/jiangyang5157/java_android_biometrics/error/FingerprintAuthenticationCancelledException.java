package com.gmail.jiangyang5157.java_android_biometrics.error;

/**
 * Created by yangjiang on 24/03/17.
 */
public class FingerprintAuthenticationCancelledException extends BiometricsException {

    private boolean selfCancelled;

    private int errMsgId;

    public FingerprintAuthenticationCancelledException(boolean selfCancelled, int errMsgId, String message) {
        super(message);
        this.selfCancelled = selfCancelled;
        this.errMsgId = errMsgId;
    }

    public boolean isSelfCancelled() {
        return selfCancelled;
    }

    public int getErrMsgId() {
        return errMsgId;
    }
}
