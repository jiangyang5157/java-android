package com.gmail.jiangyang5157.tookit.android.biometrics.crypto;

import android.hardware.fingerprint.FingerprintManager;
import android.support.annotation.NonNull;

import com.gmail.jiangyang5157.tookit.android.biometrics.error.FingerprintChangedException;

import java.security.Signature;
import java.security.SignatureException;

/**
 * Created by yangjiang on 25/03/17.
 */
abstract class Signing extends Crypto {

    Signing(@NonNull String keyName) {
        super(keyName);
    }

    Signing(@NonNull String keyName, @NonNull String provider) {
        super(keyName, provider);
    }

    @Override
    public FingerprintManager.CryptoObject createCryptoObject() throws FingerprintChangedException {
        Signature signature = providesSigningSignature();
        return new FingerprintManager.CryptoObject(signature);
    }

    public byte[] sign(Signature signature, byte[] raw) {
        if (raw == null) {
            return null;
        }
        try {
            signature.update(raw);
            return signature.sign();
        } catch (SignatureException e) {
            throw new RuntimeException("Failed to do signing", e);
        }
    }

    public byte[] sign(byte[] raw) throws FingerprintChangedException {
        return sign(providesSigningSignature(), raw);
    }

    public boolean verify(Signature signature, byte[] signtureBytes, byte[] raw) {
        if (raw == null) {
            return false;
        }
        try {
            signature.update(raw);
            return signature.verify(signtureBytes);
        } catch (SignatureException e) {
            throw new RuntimeException("Failed to do verify", e);
        }
    }

    public boolean verify(byte[] signtureBytes, byte[] raw) {
        return verify(providesVerifySignature(), signtureBytes, raw);
    }

    public abstract Signature providesSignature();

    public abstract Signature providesSigningSignature() throws FingerprintChangedException;

    public abstract Signature providesVerifySignature();
}