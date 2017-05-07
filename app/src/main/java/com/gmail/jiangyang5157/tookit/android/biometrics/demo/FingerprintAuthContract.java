package com.gmail.jiangyang5157.tookit.android.biometrics.demo;

import android.content.Context;

import com.gmail.jiangyang5157.tookit.android.biometrics.crypto.Crypto;

/**
 * Created by yangjiang on 24/03/17.
 */
public class FingerprintAuthContract {

    public interface Presenter {
    }

    public interface View {
        void showMessage(String s);

        Context getContext();
    }
}
