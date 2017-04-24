package com.gmail.jiangyang5157.tookit.android.biometrics.crypto;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.annotation.NonNull;

import com.gmail.jiangyang5157.tookit.android.biometrics.error.FingerprintChangedException;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateException;

/**
 * Created by yangjiang on 25/03/17.
 */
public class RsaSigning extends Signing {

    public RsaSigning(@NonNull String keyName) {
        super(keyName);
    }

    public RsaSigning(@NonNull String keyName, @NonNull String provider) {
        super(keyName, provider);
    }

    @Override
    public KeyPair createKeyPair() {
        KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(mKeyName, KeyProperties.PURPOSE_SIGN);
        builder.setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512);
        builder.setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PSS);
        builder.setKeySize(2048);
        /*
         It requires that the user authenticate with a registered fingerprint to authorize every use of the private key.
         It throw "android.security.KeyStoreException: Key user not authenticated" when private key be used without fingerprint auth.
         */
        builder.setUserAuthenticationRequired(true);
        /*
         Samsung KeyStoreException:
         http://stackoverflow.com/questions/36043912/error-after-fingerprint-touched-on-samsung-phones-android-security-keystoreexce
         */

        KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, mProvider);
            keyPairGenerator.initialize(builder.build());
        } catch (NoSuchAlgorithmException
                | NoSuchProviderException
                | InvalidAlgorithmParameterException e) {
            throw new RuntimeException("Failed to generate key pair", e);
        }
        return keyPairGenerator.generateKeyPair();
    }

    @Override
    public Signature providesSignature() {
        try {
            return Signature.getInstance("SHA256withRSA/PSS");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Use PrivateKey for signing
     */
    @Override
    public Signature providesSigningSignature() throws FingerprintChangedException {
        Signature signature = providesSignature();
        KeyStore keyStore = providesKeystore();
        try {
            keyStore.load(null);
            PrivateKey key = providesPrivateKey(keyStore);
            signature.initSign(key);
            return signature;
        } catch (KeyPermanentlyInvalidatedException e) {
            // Throw KeyPermanentlyInvalidatedException if any new Fingerprint setup since the keypair created
            // Won't throw KeyPermanentlyInvalidatedException if any Fingerprint has been deleted since the keypair created
            throw new FingerprintChangedException();
        } catch (InvalidKeyException
                | CertificateException
                | NoSuchAlgorithmException
                | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Signature providesVerifySignature() {
        Signature signature = providesSignature();
        KeyStore keyStore = providesKeystore();
        try {
            keyStore.load(null);
            PublicKey key = providesPublicKey(keyStore);
            signature.initVerify(key);
            return signature;
        } catch (InvalidKeyException
                | CertificateException
                | NoSuchAlgorithmException
                | IOException e) {
            throw new RuntimeException(e);
        }
    }
}