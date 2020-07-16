package com.example.androidfingerprint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public native String fingerPrint();

    static {
        System.loadLibrary("ndkfingerprint");
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView mParaLabel = (TextView) findViewById(R.id.text_scanner);
        ((Button) findViewById(R.id.first_button)).setText(fingerPrint());

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

            if (!fingerprintManager.isHardwareDetected()) {
                mParaLabel.setText("Fingerprint Scanner not detected in Device");
            } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                mParaLabel.setText("Permission not granted to use Fingerprint Scanner");
            } else if (keyguardManager.isKeyguardSecure()) {
                mParaLabel.setText("Add Lock to your Phone in Setting");
            } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                mParaLabel.setText("You should add 1 Fingerprint to use this Feature");
            } else {
                mParaLabel.setText("Place your Finger on Scanner to Access the App");

                FingerprintHandler fingerprintHandler = new FingerprintHandler(this);
                fingerprintHandler.startAuth(fingerprintManager, null);
            }
        }
    }
}