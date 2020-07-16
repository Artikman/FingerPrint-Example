package com.example.dex;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;

import dalvik.system.DexClassLoader;

public class MainActivity extends AppCompatActivity {

    FrameLayout frameLayout;
    String dataDir;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Thread.UncaughtExceptionHandler oldHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                e.printStackTrace();
                oldHandler.uncaughtException(t, e);
            }
        });

        dataDir = getApplicationInfo().dataDir;
        frameLayout = (FrameLayout) findViewById(R.id.main_frame);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Загружаем класс");
        progressDialog.show();


        DownloadTask downloadTask = new DownloadTask(this, dataDir);
        downloadTask.execute(null, null, null);

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String className = intent.getStringExtra("className");
                Bundle args = intent.getBundleExtra("args");
                showActivity(className, args);
            }
        };
        IntentFilter filter = new IntentFilter("com.example.dex.ShowActivity");
        registerReceiver(receiver, filter);
    }

    @Override
    public void onBackPressed() {
        System.exit(0);
    }

    public void downloadReady() {
        Toast.makeText(this, "Класс загружен", Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
        showActivity("com.example.androidfingerprint.MainActivity", null);
    }

    public void downloadError() {
        Toast.makeText(this, "Ошибка загрузки", Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
    }

    public void showActivity(String className, Bundle arguments) {
        File dexFile = new File(dataDir, "classes.dex");
        Log.e("Main activity", "Loading from dex: " + dexFile.getAbsolutePath());
        File codeCacheDir = new File(getCacheDir() + File.separator + "codeCache");
        codeCacheDir.mkdirs();
        DexClassLoader dexClassLoader = new DexClassLoader(
                dexFile.getAbsolutePath(), codeCacheDir.getAbsolutePath(), null, getClassLoader());
        try {
            Class clazz = dexClassLoader.loadClass(className);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}