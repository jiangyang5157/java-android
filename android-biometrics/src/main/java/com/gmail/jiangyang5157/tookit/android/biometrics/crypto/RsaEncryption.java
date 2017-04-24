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
import java.security.cert.CertificateException;
import java.security.spec.MGF1ParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;

/**
 * Created by yangjiang on 24/03/17.
 * RSA key pair for encryption/decryption using RSA OAEP
 */
public class RsaEncryption extends Encryption {

    public RsaEncryption(@NonNull String keyName) {
        super(keyName);
    }

    public RsaEncryption(@NonNull String keyName, @NonNull String provider) {
        super(keyName, provider);
    }

    @Override
    public KeyPair createKeyPair() {
        KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(mKeyName, KeyProperties.PURPOSE_DECRYPT);
        builder.setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512);
        builder.setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_OAEP);
        builder.setKeySize(2048);
        /*
         It requires that the user authenticate with a registered fingerprint to authorize every use of the private key.
         It throw "android.security.KeyStoreException: Key user not authenticated" when private key be used without fingerprint auth.
         */
        builder.setUserAuthenticationRequired(true);

        KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, mProvider);
            keyPairGenerator.initialize(builder.build());
        } catch (InvalidAlgorithmParameterException
                | NoSuchAlgorithmException
                | NoSuchProviderException e) {
            throw new RuntimeException("Failed to generate key pair", e);
        }
        return keyPairGenerator.generateKeyPair();
    }

    @Override
    public Cipher providesCipher() {
        try {
            /*
             https://developer.android.com/reference/android/security/keystore/KeyGenParameterSpec.html
             https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#Cipher
             */
            return Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        } catch (NoSuchAlgorithmException
                | NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Use PublicKey for encrypt
     */
    @Override
    public Cipher providesEncryptCipher() {
        Cipher cipher = providesCipher();
        KeyStore keyStore = providesKeystore();
        try {
            keyStore.load(null);
            PublicKey unrestrictedPublicKey = providesUnrestrictedPublicKey(keyStore);
            OAEPParameterSpec spec = new OAEPParameterSpec(
                    "SHA-256", "MGF1",
                    MGF1ParameterSpec.SHA1,
                    PSource.PSpecified.DEFAULT);
            cipher.init(Cipher.ENCRYPT_MODE, unrestrictedPublicKey, spec);
            return cipher;
        } catch (InvalidKeyException
                | InvalidAlgorithmParameterException
                | CertificateException
                | NoSuchAlgorithmException
                | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Use PrivateKey for decrypt
     */
    @Override
    public Cipher providesDecryptCipher() throws FingerprintChangedException {
        Cipher cipher = providesCipher();
        KeyStore keyStore = providesKeystore();
        try {
            keyStore.load(null);
            PrivateKey key = providesPrivateKey(keyStore);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher;
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
}