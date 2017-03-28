package com.gmail.jiangyang5157.tookit.android.biometrics;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;

import com.gmail.jiangyang5157.tookit.android.biometrics.crypto.Crypto;
import com.gmail.jiangyang5157.tookit.android.biometrics.error.FingerprintChangedException;
import com.gmail.jiangyang5157.tookit.android.biometrics.error.NoEnrolledFingerprintException;
import com.gmail.jiangyang5157.tookit.android.biometrics.error.NoEnrolledScreenLockException;
import com.gmail.jiangyang5157.tookit.android.biometrics.error.OsVersionException;
import com.gmail.jiangyang5157.tookit.android.biometrics.error.SensorException;

/**
 * Created by yangjiang on 27/03/17.
 */
public abstract class FingerprintAuthenticationPresenter implements FingerprintAuthenticationHandler.AuthenticationCallback {

    private FingerprintAuthenticationHandler mFingerprintAuthenticationHandler;

    private FingerprintManager.CryptoObject mCryptoObject;

    public FingerprintAuthenticationPresenter(Context context) {
        mFingerprintAuthenticationHandler = new FingerprintAuthenticationHandler(context, this);
    }

    protected void prepare(Crypto crypto) throws OsVersionException, SensorException, NoEnrolledFingerprintException, NoEnrolledScreenLockException, FingerprintChangedException {
        mFingerprintAuthenticationHandler.checkDeviceSupport();
        mFingerprintAuthenticationHandler.checkAuthenticationAvailable();
        mCryptoObject = crypto.createCryptoObject();
    }

    protected void startAuth() {
        mFingerprintAuthenticationHandler.startListening(mCryptoObject);
    }


    protected void stopAuth() {
        mFingerprintAuthenticationHandler.stopListening();
    }

    @Override
    public void onAuthenticated(FingerprintManager.AuthenticationResult result) {
        onAuthenticated(result.getCryptoObject());
    }

    protected abstract void onAuthenticated(FingerprintManager.CryptoObject cryptoObject);
}
