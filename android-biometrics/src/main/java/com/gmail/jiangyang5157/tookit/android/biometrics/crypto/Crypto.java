package com.gmail.jiangyang5157.tookit.android.biometrics.crypto;

import android.hardware.fingerprint.FingerprintManager;
import android.support.annotation.NonNull;

import com.gmail.jiangyang5157.tookit.android.biometrics.error.FingerprintChangedException;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by yangjiang on 25/03/17.
 */
public abstract class Crypto {

    static final String DEFAULT_PROVIDER = "AndroidKeyStore";

    final String mKeyName;

    final String mProvider;

    Crypto(@NonNull String keyName) {
        this(keyName, DEFAULT_PROVIDER);
    }

    Crypto(@NonNull String keyName, @NonNull String provider) {
        mKeyName = keyName;
        mProvider = provider;
    }

    KeyStore providesKeystore() {
        try {
            return KeyStore.getInstance(mProvider);
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        }
    }

    public PrivateKey providesPrivateKey(){
        KeyStore keyStore = providesKeystore();
        try {
            keyStore.load(null);
            return providesPrivateKey(keyStore);
        } catch (IOException | NoSuchAlgorithmException | CertificateException e) {
            throw new RuntimeException(e);
        }
    }

    PrivateKey providesPrivateKey(KeyStore keyStore) {
        try {
            return (PrivateKey) keyStore.getKey(mKeyName, null);
        } catch (KeyStoreException
                | NoSuchAlgorithmException
                | UnrecoverableKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public PublicKey providesPublicKey(){
        KeyStore keyStore = providesKeystore();
        try {
            keyStore.load(null);
            return providesPublicKey(keyStore);
        } catch (IOException | NoSuchAlgorithmException | CertificateException e) {
            throw new RuntimeException(e);
        }
    }

    PublicKey providesPublicKey(KeyStore keyStore) {
        try {
            return keyStore.getCertificate(mKeyName).getPublicKey();
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        }
    }

    public PublicKey providesUnrestrictedPublicKey(){
        KeyStore keyStore = providesKeystore();
        try {
            keyStore.load(null);
            return providesUnrestrictedPublicKey(keyStore);
        } catch (IOException | NoSuchAlgorithmException | CertificateException e) {
            throw new RuntimeException(e);
        }
    }

    PublicKey providesUnrestrictedPublicKey(KeyStore keyStore) {
        try {
            PublicKey publicKey = providesPublicKey(keyStore);
            /*
             A known bug in Android 6.0 (API Level 23) causes user authentication-related authorizations to be enforced even for public keys.
             To work around this issue extract the public key material to use outside of Android Keystore.
             */
            KeyFactory keyFactory = KeyFactory.getInstance(publicKey.getAlgorithm());
            return keyFactory.generatePublic(new X509EncodedKeySpec(publicKey.getEncoded()));
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract KeyPair createKeyPair();

    public abstract FingerprintManager.CryptoObject createCryptoObject() throws FingerprintChangedException;
}
