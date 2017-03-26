package com.gmail.jiangyang5157.tookit.android.biometrics.demo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.gmail.jiangyang5157.tookit.android.biometrics.FingerprintAuthenticationHandler;
import com.gmail.jiangyang5157.tookit.android.biometrics.crypto.Crypto;
import com.gmail.jiangyang5157.tookit.android.biometrics.crypto.RsaEncryption;
import com.gmail.jiangyang5157.tookit.android.biometrics.crypto.RsaSigning;
import com.gmail.jiangyang5157.tookit.android.biometrics.error.BiometricsException;
import com.gmail.jiangyang5157.tookit.android.biometrics.error.FingerprintChangedException;
import com.gmail.jiangyang5157.tookit.android.biometrics.error.NoEnrolledFingerprintException;
import com.gmail.jiangyang5157.tookit.android.biometrics.error.NoEnrolledScreenLockException;
import com.gmail.jiangyang5157.tookit.android.biometrics.error.OsVersionException;
import com.gmail.jiangyang5157.tookit.android.biometrics.error.SensorException;

import java.security.Signature;

import javax.crypto.Cipher;

/**
 * Created by yangjiang on 24/03/17.
 */
public class FingerprintAuthenticationPresenter
        implements FingerprintAuthenticationContract.Presenter, FingerprintAuthenticationHandler.AuthenticationCallback {

    private FingerprintAuthenticationContract.View mView;

    private FingerprintAuthenticationHandler mFingerprintAuthenticationHandler;

    private Crypto mCrypto;

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
                startAuth_Encryption();
//                startAuth_Signing();
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
        }
    }

    @Override
    public void stopAuth() {
        mFingerprintAuthenticationHandler.stopListening();
    }

    @Override
    public void onAuthenticated(FingerprintManager.AuthenticationResult result) {
        onAuthenticated_Encryption(result);
//        onAuthenticated_Signing(result);
    }

    @Override
    public void onAuthentication(BiometricsException e) {
        mView.showMessage(e.toString());
    }

    private static String temp;
    private void startAuth_Encryption() {
        // Register from after-login-more-touchid-register, and receive the token from server
        String onDeviceAuthToken = MockServer.register();
        Log.d("####", "startAuth_Encryption.onDeviceAuthToken=" + onDeviceAuthToken);
        // Create asymmetric-keypair
        mCrypto = new RsaEncryption(MockServer.KEY_NAME);
        mCrypto.createKeyPair();

        // Encrypt onDeviceAuthToken
        String encryptedOnDeviceAuthToken = ((RsaEncryption)mCrypto).encrypt(onDeviceAuthToken.getBytes());
        // Save the encrypted onDeviceAuthToken on device
        temp = encryptedOnDeviceAuthToken;
        Log.d("####", "startAuth_Option1.encryptedOnDeviceAuthToken=" + encryptedOnDeviceAuthToken);

        // Start Fingerprint Authentication
        FingerprintManager.CryptoObject cryptoObject = null;
        try {
            cryptoObject = mCrypto.createCryptoObject();
        }  catch (FingerprintChangedException e) {
            mView.showMessage("FingerprintChangedException: " + e.toString());
        }
        mFingerprintAuthenticationHandler.startListening(cryptoObject);
    }

    private void startAuth_Signing() {
        // Register from after-login-more-touchid-register, and receive the token from server
        String onDeviceAuthToken = MockServer.register();
        Log.d("####", "startAuth_Signing.onDeviceAuthToken=" + onDeviceAuthToken);
        // Create asymmetric-keypair
        mCrypto = new RsaSigning(MockServer.KEY_NAME);
        mCrypto.createKeyPair();

        // Send the PublicKey to server
        MockServer.uploadPublicKey(mCrypto.providesUnrestrictedPublicKey());
        // Save the hashed onDeviceAuthToken on device
        String hashedOnDeviceAuthToken = String.valueOf(onDeviceAuthToken.hashCode());
        temp = hashedOnDeviceAuthToken;

        // Start Fingerprint Authentication
        FingerprintManager.CryptoObject cryptoObject = null;
        try {
            cryptoObject = mCrypto.createCryptoObject();
        }  catch (FingerprintChangedException e) {
            mView.showMessage("FingerprintChangedException: " + e.toString());
        }
        mFingerprintAuthenticationHandler.startListening(cryptoObject);
    }

    private void onAuthenticated_Encryption(FingerprintManager.AuthenticationResult result){
        // Get the encrypted onDeviceAuthToken on device
        String encryptedOnDeviceAuthToken = temp;
        // Decrypt for onDeviceAuthToken
        Cipher authenticatedCipher = result.getCryptoObject().getCipher();
        byte[] onDeviceAuthTokenBytes = ((RsaEncryption)mCrypto).decrypt(authenticatedCipher, encryptedOnDeviceAuthToken);

        // Server verify if the onDeviceAuthToken is correct
        boolean serverVerify = MockServer.verifyToken(new String(onDeviceAuthTokenBytes));
        mView.showMessage(String.valueOf(serverVerify));
    }

    private void onAuthenticated_Signing(FingerprintManager.AuthenticationResult result){
        // Get the hashed onDeviceAuthToken on device
        String hashedOnDeviceAuthToken = temp;

        // Get authenticated Signature, which has been initialized for sign usage in the "Crypto.createCryptoObject()"
        Signature authenticatedSignature = result.getCryptoObject().getSignature();
        // Signature bytes from hashed onDeviceAuthToken
        byte[] signatureBytes = ((RsaSigning)mCrypto).sign(authenticatedSignature, hashedOnDeviceAuthToken.getBytes());

        // Get a new Signature instance, which will be initialized for verify usage
        Signature newSignatureInstance = ((RsaSigning)mCrypto).providesSignature();

        // Server verify the hashed onDeviceAuthToken signatureBytes
        boolean serverVerify = MockServer.verifyHashedToken(newSignatureInstance, signatureBytes);
        mView.showMessage(String.valueOf(serverVerify));
    }
}
