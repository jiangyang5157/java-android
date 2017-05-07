package com.gmail.jiangyang5157.tookit.android.biometrics.demo;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gmail.jiangyang5157.tookit.R;
import com.gmail.jiangyang5157.tookit.android.biometrics.crypto.RsaEncryption;

public class DemoActivity extends AppCompatActivity implements FingerprintAuthContract.View {

    private TextView tvMessage;

    private FingerprintAuthPresenter mFingerprintAuthenticationPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.biometrics_demo_activity);
        tvMessage = (TextView) findViewById(R.id.tv_message);
        Button btnStart = (Button) findViewById(R.id.btn_start);
        Button btnStop = (Button) findViewById(R.id.btn_stop);
        btnStart.setOnClickListener(btnStartOnClickListener);
        btnStop.setOnClickListener(btnStopOnClickListener);

        mFingerprintAuthenticationPresenter = new FingerprintAuthPresenter(this);
    }

    private View.OnClickListener btnStartOnClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mFingerprintAuthenticationPresenter.prepare()) {
                mFingerprintAuthenticationPresenter.startAuth();
            }
        }
    };

    private View.OnClickListener btnStopOnClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            mFingerprintAuthenticationPresenter.stopAuth();
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        mFingerprintAuthenticationPresenter.stopAuth();
    }

    public static final int REQUEST_CODE_SECURITY_SETTINGS = 100;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SECURITY_SETTINGS) {
        }
    }

    @Override
    public void showMessage(String s) {
        tvMessage.setText(s);
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }
}
