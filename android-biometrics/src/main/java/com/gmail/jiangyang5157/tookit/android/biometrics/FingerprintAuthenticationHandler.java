package com.gmail.jiangyang5157.tookit.android.biometrics;

import android.app.KeyguardManager;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;

import com.gmail.jiangyang5157.tookit.android.biometrics.error.BiometricsException;
import com.gmail.jiangyang5157.tookit.android.biometrics.error.FingerprintAuthenticationCancelledException;
import com.gmail.jiangyang5157.tookit.android.biometrics.error.FingerprintAuthenticationFailed;
import com.gmail.jiangyang5157.tookit.android.biometrics.error.NoEnrolledFingerprintException;
import com.gmail.jiangyang5157.tookit.android.biometrics.error.NoEnrolledScreenLockException;
import com.gmail.jiangyang5157.tookit.android.biometrics.error.OsVersionException;
import com.gmail.jiangyang5157.tookit.android.biometrics.error.SensorException;

/**
 * @author Yang
 * @since 3/9/2017
 */
public class FingerprintAuthenticationHandler extends FingerprintManager.AuthenticationCallback {

    interface AuthenticationCallback {
        void onAuthenticated(FingerprintManager.AuthenticationResult result);

        void onAuthentication(BiometricsException e);
    }

    private AuthenticationCallback mAuthenticationCallback;

    private CancellationSignal mCancellationSignal;

    private boolean mSelfCancelled = false;

    private KeyguardManager mKeyguardManager;
    private FingerprintManager mFingerprintManager;

    FingerprintAuthenticationHandler(Context context) {
        this(context, null);
    }

    FingerprintAuthenticationHandler(Context context, AuthenticationCallback authenticationCallback) {
        mKeyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        mFingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
        mAuthenticationCallback = authenticationCallback;
    }

    void checkDeviceSupport() throws OsVersionException, SensorException {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            throw new OsVersionException();
        }
        if (!mFingerprintManager.isHardwareDetected()) {
            throw new SensorException();
        }
    }

    void checkAuthenticationAvailable() throws NoEnrolledScreenLockException, NoEnrolledFingerprintException {
        if (!mKeyguardManager.isKeyguardSecure()) {
            throw new NoEnrolledScreenLockException();
        }
        if (!mFingerprintManager.hasEnrolledFingerprints()) {
            throw new NoEnrolledFingerprintException();
        }
    }

    void startListening(FingerprintManager.CryptoObject cryptoObject) {
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

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        if (mSelfCancelled) {
            if (mAuthenticationCallback != null) {
                mAuthenticationCallback.onAuthentication(new FingerprintAuthenticationCancelledException());
            }
        }
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        if (mAuthenticationCallback != null) {
            mAuthenticationCallback.onAuthentication(new FingerprintAuthenticationFailed(helpString.toString()));
        }
    }

    @Override
    public void onAuthenticationFailed() {
        if (mAuthenticationCallback != null) {
            mAuthenticationCallback.onAuthentication(new FingerprintAuthenticationFailed());
        }
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        if (mAuthenticationCallback != null) {
            mAuthenticationCallback.onAuthenticated(result);
        }
    }

    public AuthenticationCallback getAuthenticationCallback() {
        return mAuthenticationCallback;
    }

    public void setAuthenticationCallback(AuthenticationCallback authenticationCallback) {
        this.mAuthenticationCallback = authenticationCallback;
    }
}
