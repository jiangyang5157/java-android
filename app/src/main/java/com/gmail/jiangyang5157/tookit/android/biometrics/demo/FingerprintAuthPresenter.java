package com.gmail.jiangyang5157.tookit.android.biometrics.demo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.gmail.jiangyang5157.tookit.android.biometrics.FingerprintAuthenticationPresenter;
import com.gmail.jiangyang5157.tookit.android.biometrics.crypto.Crypto;
import com.gmail.jiangyang5157.tookit.android.biometrics.crypto.RsaEncryption;
import com.gmail.jiangyang5157.tookit.android.biometrics.crypto.RsaSigning;
import com.gmail.jiangyang5157.tookit.android.biometrics.error.BiometricsException;
import com.gmail.jiangyang5157.tookit.android.biometrics.error.FingerprintChangedException;
import com.gmail.jiangyang5157.tookit.android.biometrics.error.NoEnrolledFingerprintException;
import com.gmail.jiangyang5157.tookit.android.biometrics.error.NoEnrolledScreenLockException;
import com.gmail.jiangyang5157.tookit.android.biometrics.error.OsVersionException;
import com.gmail.jiangyang5157.tookit.android.biometrics.error.SensorException;

import java.security.KeyPair;
import java.security.Signature;

import javax.crypto.Cipher;

/**
 * Created by yangjiang on 24/03/17.
 */
public class FingerprintAuthPresenter extends FingerprintAuthenticationPresenter implements FingerprintAuthContract.Presenter {

    public static final String KEY_NAME = "KEY_NAME";

    private FingerprintAuthContract.View mView;

    public FingerprintAuthPresenter(@NonNull FingerprintAuthContract.View view) {
        super(view.getContext());
        mView = view;

        register_Encryption();
//        register_Signing();
    }

    @Override
    public void onAuthentication(BiometricsException e) {
        mView.showMessage(e.toString());
    }

    @Override
    public void onAuthenticated(FingerprintManager.CryptoObject cryptoObject) {
        onAuthenticated_Encryption(cryptoObject);
//        onAuthenticated_Signing(cryptoObject);
    }

    @Override
    public boolean initialize(Crypto crypto) {
        try {
            prepare(crypto);
            if (ContextCompat.checkSelfPermission(mView.getContext(), Manifest.permission.USE_FINGERPRINT) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                mView.showMessage("USE_FINGERPRINT Permission not granted");
                return false;
            }
        } catch (OsVersionException e) {
            mView.showMessage("OsVersionException: " + e.toString());
            return false;
        } catch (SensorException e) {
            mView.showMessage("SensorException: " + e.toString());
            return false;
        } catch (NoEnrolledScreenLockException e) {
            mView.showMessage("NoEnrolledScreenLockException: " + e.toString());
            mView.showSecuritySettings();
            return false;
        } catch (NoEnrolledFingerprintException e) {
            mView.showMessage("NoEnrolledFingerprintException: " + e.toString());
            mView.showSecuritySettings();
            return false;
        } catch (FingerprintChangedException e) {
            mView.showMessage("FingerprintChangedException: " + e.toString());
            return false;
        }
    }

    @Override
    public void startAuth() {
        super.startAuth();
    }

    @Override
    public void stopAuth() {
        super.stopAuth();
    }

    private static String temp;

    private void register_Encryption() {
        RsaEncryption crypto = new RsaEncryption(KEY_NAME);

        // Register from after-login-more-touchid-register, and receive the token from server
        String onDeviceAuthToken = MockServer.register();
        Log.d("####", "register_Encryption.onDeviceAuthToken=" + onDeviceAuthToken);
        // Create asymmetric-keypair
        crypto.createKeyPair();

        // Encrypt onDeviceAuthToken
        String encryptedOnDeviceAuthToken = crypto.encrypt(onDeviceAuthToken.getBytes());
        Log.d("####", "register_Encryption.encryptedOnDeviceAuthToken=" + encryptedOnDeviceAuthToken);
        // Save the encrypted onDeviceAuthToken on device
        temp = encryptedOnDeviceAuthToken;
    }

    private void register_Signing() {
        RsaSigning crypto = new RsaSigning(KEY_NAME);

        // Register from after-login-more-touchid-register, and receive the token from server
        String onDeviceAuthToken = MockServer.register();
        Log.d("####", "register_Signing.onDeviceAuthToken=" + onDeviceAuthToken);
        // Create asymmetric-keypair
        KeyPair keyPair = crypto.createKeyPair();

        // Send the PublicKey to server
        MockServer.uploadPublicKey(keyPair.getPublic());
        // Save the hashed onDeviceAuthToken on device
        String hashedOnDeviceAuthToken = String.valueOf(onDeviceAuthToken.hashCode());
        temp = hashedOnDeviceAuthToken;
    }

    private void onAuthenticated_Encryption(FingerprintManager.CryptoObject cryptoObject) {
        RsaEncryption crypto = new RsaEncryption(KEY_NAME);

        // Get the encrypted onDeviceAuthToken on device
        String encryptedOnDeviceAuthToken = temp;
        // Decrypt for onDeviceAuthToken
        Cipher authenticatedCipher = cryptoObject.getCipher();
        byte[] onDeviceAuthTokenBytes = crypto.decrypt(authenticatedCipher, encryptedOnDeviceAuthToken);

        // Server verify if the onDeviceAuthToken is correct
        boolean serverVerify = MockServer.verifyToken(new String(onDeviceAuthTokenBytes));
        mView.showMessage(String.valueOf(serverVerify));
    }

    private void onAuthenticated_Signing(FingerprintManager.CryptoObject cryptoObject) {
        RsaSigning crypto = new RsaSigning(KEY_NAME);

        // Get the hashed onDeviceAuthToken on device
        String hashedOnDeviceAuthToken = temp;

        // Get authenticated Signature, which has been initialized for sign usage in the "Crypto.createCryptoObject()"
        Signature authenticatedSignature = cryptoObject.getSignature();
        // Signature bytes from hashed onDeviceAuthToken
        byte[] signatureBytes = crypto.sign(authenticatedSignature, hashedOnDeviceAuthToken.getBytes());

        // Get a new Signature instance, which will be initialized for verify usage
        Signature newSignatureInstance = crypto.providesSignature();

        // Server verify the hashed onDeviceAuthToken signatureBytes
        boolean serverVerify = MockServer.verifyHashedToken(newSignatureInstance, signatureBytes);
        mView.showMessage(String.valueOf(serverVerify));
    }
}
