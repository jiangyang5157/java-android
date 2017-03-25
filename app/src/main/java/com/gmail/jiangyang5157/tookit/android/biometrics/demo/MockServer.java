package com.gmail.jiangyang5157.tookit.android.biometrics.demo;

import java.security.InvalidKeyException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

/**
 * @author Yang
 * @since 3/9/2017
 */
public class MockServer {

    public static final String KEY_NAME = "KEY_NAME";

    private static String token;

    private static PublicKey publicKey;

    public static boolean verifyToken(String s) {
        return token.equals(s);
    }

    public static boolean verifyHashedToken(Signature signature, byte[] signtureBytes) {
        String hashedToken = String.valueOf(token.hashCode());
        try {
            signature.initVerify(publicKey);
            signature.update(hashedToken.getBytes());
            return signature.verify(signtureBytes);
        } catch (InvalidKeyException | SignatureException e) {
            throw new RuntimeException(e);
        }
    }

    public static void regiest(String s) {
        token = s;
    }

    public static void regiest(String s, PublicKey pb) {
        token = s;
        publicKey = pb;
    }

    public static void deregiest() {
        token = null;
        publicKey = null;
    }

    public static String getToken() {
        return token;
    }
}
