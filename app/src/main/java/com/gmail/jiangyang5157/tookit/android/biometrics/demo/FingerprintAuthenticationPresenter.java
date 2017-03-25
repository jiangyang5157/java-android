package com.gmail.jiangyang5157.tookit.android.biometrics.demo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.gmail.jiangyang5157.tookit.android.biometrics.FingerprintAuthenticationHandler;
import com.gmail.jiangyang5157.tookit.android.biometrics.crypto.RsaEncryption;
import com.gmail.jiangyang5157.tookit.android.biometrics.crypto.RsaSigning;
import com.gmail.jiangyang5157.tookit.android.biometrics.error.BiometricsException;
import com.gmail.jiangyang5157.tookit.android.biometrics.error.FingerprintChangedException;
import com.gmail.jiangyang5157.tookit.android.biometrics.error.NoEnrolledFingerprintException;
import com.gmail.jiangyang5157.tookit.android.biometrics.error.NoEnrolledScreenLockException;
import com.gmail.jiangyang5157.tookit.android.biometrics.error.OsVersionException;
import com.gmail.jiangyang5157.tookit.android.biometrics.error.SensorException;

/**
 * Created by yangjiang on 24/03/17.
 */
public class FingerprintAuthenticationPresenter
        implements FingerprintAuthenticationContract.Presenter, FingerprintAuthenticationHandler.AuthenticationCallback {

    private FingerprintAuthenticationContract.View mView;

    private FingerprintAuthenticationHandler mFingerprintAuthenticationHandler;

    private RsaEncryption mRsaEncryption;
    private String mSecureDataByRsaEncryption;
    private RsaSigning mRsaSigning;

    public FingerprintAuthenticationPresenter(@NonNull FingerprintAuthenticationContract.View view) {
        mView = view;
        mFingerprintAuthenticationHandler = new FingerprintAuthenticationHandler(view.getContext(), this);
    }

    @Override
    public void startAuth() {
        try {
            mFingerprintAuthenticationHandler.checkDeviceSupport();
            mFingerprintAuthenticationHandler.checkAuthenticationAvailable();
            if (ContextCompat.checkSelfPermission(mView.getContext(),
                    Manifest.permission.USE_FINGERPRINT) == PackageManager.PERMISSION_GRANTED) {
                mView.showMessage("startAuth");
//                mRsaEncryption = new RsaEncryption(MockServer.KEY_NAME);
//                mRsaEncryption.createKeyPair();
//                MockServer.regiest("asdasdasdasd");
//                mSecureDataByRsaEncryption = mRsaEncryption.encrypt(MockServer.getToken().getBytes());
//                mView.showMessage("mSecureDataByRsaEncryption=" + mSecureDataByRsaEncryption);
//                mFingerprintAuthenticationHandler.startListening(mRsaEncryption.createCryptoObject());
                mRsaSigning = new RsaSigning(MockServer.KEY_NAME);
                mRsaSigning.createKeyPair();
                MockServer.regiest("asdasdasdasd", mRsaSigning.providesUnrestrictedPublicKey());
                mFingerprintAuthenticationHandler.startListening(mRsaSigning.createCryptoObject());
            }
        } catch (OsVersionException e) {
            mView.showMessage("OsVersionException: " + e.toString());
        } catch (SensorException e) {
            mView.showMessage("SensorException: " + e.toString());
        } catch (NoEnrolledScreenLockException e) {
            mView.showMessage("NoEnrolledScreenLockException: " + e.toString());
            mView.showSecuritySettings();
        } catch (NoEnrolledFingerprintException e) {
            mView.showMessage("NoEnrolledFingerprintException: " + e.toString());
            mView.showSecuritySettings();
        } catch (FingerprintChangedException e) {
            mView.showMessage("FingerprintChangedException: " + e.toString());
        }
    }

    @Override
    public void stopAuth() {
        mFingerprintAuthenticationHandler.stopListening();
    }

    @Override
    public void onAuthenticated(FingerprintManager.AuthenticationResult result) {
//        mView.showMessage(String.valueOf(MockServer.verifyToken(new String(
//                mRsaEncryption.decrypt(result.getCryptoObject().getCipher(), mSecureDataByRsaEncryption)))));
        String hashedToken = String.valueOf(MockServer.getToken().hashCode());
        byte[] secureDataByRsaSigning = mRsaSigning.sign(result.getCryptoObject().getSignature(), hashedToken.getBytes());
        mView.showMessage("Signing verify: " + String.valueOf(mRsaSigning.verify(
                mRsaSigning.providesSignature(),
                secureDataByRsaSigning,
                mRsaSigning.providesUnrestrictedPublicKey(),
                hashedToken.getBytes()) + "\n"
                + "MockServer verify:" + String.valueOf(MockServer.verifyHashedToken(
                mRsaSigning.providesSignature(),
                secureDataByRsaSigning))));
    }

    @Override
    public void onAuthentication(BiometricsException e) {
        mView.showMessage(e.toString());
    }
}
