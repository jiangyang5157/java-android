package com.gmail.jiangyang5157.tookit.android.biometrics.controller;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;

import com.gmail.jiangyang5157.tookit.android.biometrics.crypto.Crypto;
import com.gmail.jiangyang5157.tookit.android.biometrics.error.FingerprintChangedException;
import com.gmail.jiangyang5157.tookit.android.biometrics.error.NoEnrolledFingerprintException;
import com.gmail.jiangyang5157.tookit.android.biometrics.error.NoEnrolledScreenLockException;
import com.gmail.jiangyang5157.tookit.android.biometrics.error.NoPermissionGrantedException;
import com.gmail.jiangyang5157.tookit.android.biometrics.error.OsVersionException;
import com.gmail.jiangyang5157.tookit.android.biometrics.error.SensorException;

/**
 * Created by yangjiang on 24/04/17.
 */
public abstract class FingerprintAuthenticationDelegate implements FingerprintAuthentication {

    private FingerprintAuthenticationHandler mFingerprintAuthenticationHandler;

    private FingerprintManager.CryptoObject mCryptoObject = null;

    public FingerprintAuthenticationDelegate(Context context) {
        mFingerprintAuthenticationHandler = new FingerprintAuthenticationHandler(context, this);
    }

    @Override
    public void startAuth() {
        try {
            mFingerprintAuthenticationHandler.startListening(mCryptoObject);
        } catch (NoPermissionGrantedException e) {
            handleNoPermissionGrantedException();
        }
    }

    @Override
    public void stopAuth() {
        mFingerprintAuthenticationHandler.stopListening();
    }

    @Override
    public boolean prepare() {
        try {
            mFingerprintAuthenticationHandler.checkDeviceSupport();
            mFingerprintAuthenticationHandler.checkAuthenticationAvailable();
            mCryptoObject = providesCrypto().createCryptoObject();
        } catch (OsVersionException e) {
            handleOsVersionException();
            return false;
        } catch (SensorException e) {
            handleSensorException();
            return false;
        } catch (NoPermissionGrantedException e) {
            handleNoPermissionGrantedException();
        } catch (NoEnrolledScreenLockException e) {
            handleNoEnrolledFingerprintException();
            return false;
        } catch (NoEnrolledFingerprintException e) {
            handleNoEnrolledScreenLockException();
            return false;
        } catch (FingerprintChangedException e) {
            handleFingerprintChangedException();
            return false;
        }

        if (mCryptoObject == null) {
            return false;
        } else {
            return true;
        }
    }

    protected abstract Crypto providesCrypto();

    protected abstract void handleOsVersionException();

    protected abstract void handleSensorException();

    protected abstract void handleNoPermissionGrantedException();

    protected abstract void handleNoEnrolledFingerprintException();

    protected abstract void handleNoEnrolledScreenLockException();

    protected abstract void handleFingerprintChangedException();
}