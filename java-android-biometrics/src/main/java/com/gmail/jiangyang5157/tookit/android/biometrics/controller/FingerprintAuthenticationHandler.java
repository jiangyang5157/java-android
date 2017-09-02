package com.gmail.jiangyang5157.tookit.android.biometrics.controller;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.gmail.jiangyang5157.tookit.android.biometrics.error.FingerprintAuthenticationCancelledException;
import com.gmail.jiangyang5157.tookit.android.biometrics.error.FingerprintAuthenticationFailedException;
import com.gmail.jiangyang5157.tookit.android.biometrics.error.FingerprintAuthenticationHelpException;
import com.gmail.jiangyang5157.tookit.android.biometrics.error.NoEnrolledFingerprintException;
import com.gmail.jiangyang5157.tookit.android.biometrics.error.NoEnrolledScreenLockException;
import com.gmail.jiangyang5157.tookit.android.biometrics.error.NoPermissionGrantedException;
import com.gmail.jiangyang5157.tookit.android.biometrics.error.OsVersionException;
import com.gmail.jiangyang5157.tookit.android.biometrics.error.SensorException;

/**
 * Created by yangjiang on 24/04/17.
 */
class FingerprintAuthenticationHandler extends FingerprintManager.AuthenticationCallback {

    interface AuthenticationCallback {
        void onAuthenticated(FingerprintManager.AuthenticationResult result);

        void onAuthenticationException(FingerprintAuthenticationCancelledException e);

        void onAuthenticationException(FingerprintAuthenticationHelpException e);

        void onAuthenticationException(FingerprintAuthenticationFailedException e);
    }

    private AuthenticationCallback mAuthenticationCallback;

    private CancellationSignal mCancellationSignal;

    private boolean mSelfCancelled = false;

    private Context mContext;

    private KeyguardManager mKeyguardManager;
    private FingerprintManager mFingerprintManager;

    public FingerprintAuthenticationHandler(Context context) {
        this(context, null);
    }

    FingerprintAuthenticationHandler(@NonNull Context context, AuthenticationCallback authenticationCallback) {
        mContext = context;
        mKeyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        mFingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
        mAuthenticationCallback = authenticationCallback;
    }

    void checkDeviceSupport() throws OsVersionException, SensorException, NoPermissionGrantedException {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            throw new NoPermissionGrantedException();
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            throw new OsVersionException();
        }
        if (!mFingerprintManager.isHardwareDetected()) {
            throw new SensorException();
        }
    }

    void checkAuthenticationAvailable() throws NoPermissionGrantedException, NoEnrolledScreenLockException, NoEnrolledFingerprintException {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            throw new NoPermissionGrantedException();
        }
        if (!mKeyguardManager.isKeyguardSecure()) {
            throw new NoEnrolledScreenLockException();
        }
        if (!mFingerprintManager.hasEnrolledFingerprints()) {
            throw new NoEnrolledFingerprintException();
        }
    }

    void startListening(FingerprintManager.CryptoObject cryptoObject) throws NoPermissionGrantedException {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            throw new NoPermissionGrantedException();
        }

        mSelfCancelled = false;
        mCancellationSignal = new CancellationSignal();
        mFingerprintManager.authenticate(cryptoObject, mCancellationSignal, 0, this, null);
    }

    void stopListening() {
        if (mCancellationSignal == null) {
            return;
        }
        mSelfCancelled = true;
        mCancellationSignal.cancel();
        mCancellationSignal = null;
    }

    /**
     * Called when an unrecoverable error has been encountered and the operation is complete.
     * No further callbacks will be made on this object.
     * @param errMsgId An integer identifying the error message
     * @param errString A human-readable error string that can be shown in UI
     */
    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        if (mAuthenticationCallback != null) {
            mAuthenticationCallback.onAuthenticationException(
                    new FingerprintAuthenticationCancelledException(
                            mSelfCancelled, errMsgId, errString.toString()));
        }
    }

    /**
     * Called when a recoverable error has been encountered during authentication.
     * The help string is provided to give the user guidance for what went wrong, such as "Sensor dirty, please clean it."
     * @param helpMsgId An integer identifying the error message
     * @param helpString A human-readable string that can be shown in UI
     */
    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        if (mAuthenticationCallback != null) {
            mAuthenticationCallback.onAuthenticationException(
                    new FingerprintAuthenticationHelpException(helpMsgId, helpString.toString()));
        }
    }

    /**
     * Called when a fingerprint is valid but not recognized.
     */
    @Override
    public void onAuthenticationFailed() {
        if (mAuthenticationCallback != null) {
            mAuthenticationCallback.onAuthenticationException(
                    new FingerprintAuthenticationFailedException());
        }
    }

    /**
     * Called when a fingerprint is recognized.
     * @param result  An object containing authentication-related data
     */
    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        if (mAuthenticationCallback != null) {
            mAuthenticationCallback.onAuthenticated(result);
        }
    }

    AuthenticationCallback getAuthenticationCallback() {
        return mAuthenticationCallback;
    }

    void setAuthenticationCallback(AuthenticationCallback authenticationCallback) {
        this.mAuthenticationCallback = authenticationCallback;
    }
}