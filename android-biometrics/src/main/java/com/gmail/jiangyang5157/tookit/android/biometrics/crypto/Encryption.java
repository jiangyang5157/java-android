package com.gmail.jiangyang5157.tookit.android.biometrics.crypto;

import android.hardware.fingerprint.FingerprintManager;
import android.support.annotation.NonNull;
import android.util.Base64;

import com.gmail.jiangyang5157.tookit.android.biometrics.error.FingerprintChangedException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

/**
 * Created by yangjiang on 25/03/17.
 */
abstract class Encryption extends Crypto {

    Encryption(@NonNull String keyName) {
        super(keyName);
    }

    Encryption(@NonNull String keyName, @NonNull String provider) {
        super(keyName, provider);
    }

    @Override
    public FingerprintManager.CryptoObject createCryptoObject() throws FingerprintChangedException {
        Cipher cipher = providesDecryptCipher();
        return new FingerprintManager.CryptoObject(cipher);
    }

    /**
     * byte [] → Encrypt → byte [] → Base64 encode → String
     */
    public String encrypt(Cipher cipher, byte[] raw) {
        if (raw == null) {
            return null;
        }
        try {
            byte[] bytes = cipher.doFinal(raw);
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException("Failed to encrypt", e);
        }
    }

    public String encrypt(byte[] raw) {
        return encrypt(providesEncryptCipher(), raw);
    }

    /**
     * String → Base64 decode → byte [] → decrypt → byte []
     */
    public byte[] decrypt(Cipher cipher, String encrypted) {
        if (encrypted == null) {
            return null;
        }
        try {
            byte[] bytes = Base64.decode(encrypted, Base64.DEFAULT);
            return cipher.doFinal(bytes);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException("Failed to decrypt", e);
        }
    }

    public byte[] decrypt(String encrypted) throws FingerprintChangedException {
        return decrypt(providesDecryptCipher(), encrypted);
    }

    public abstract Cipher providesCipher();

    public abstract Cipher providesEncryptCipher();

    public abstract Cipher providesDecryptCipher() throws FingerprintChangedException;
}
