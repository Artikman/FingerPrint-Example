package com.example.dex;

import android.os.AsyncTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

public class DownloadTask extends AsyncTask<Void, Object, Object> {

    private WeakReference<MainActivity> activityRef;
    String dataDir;

    public DownloadTask(MainActivity activity, String dataDir) {
        activityRef = new WeakReference<MainActivity>(activity);
        this.dataDir = dataDir;
    }

    @Override
    protected Object doInBackground(Void... voids) {
        try {
            File dir = new File(dataDir);
            dir.mkdirs();
            File dexFile = new File(dir, "classes.dex");
            if (dexFile.exists()) {
                dexFile.delete();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(dexFile);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return e;
        }
        return null;
    }

    @Override
    public void onPostExecute(Object obj) {
        MainActivity activity = activityRef.get();
        if (activity!=null) {
            if (obj == null) {
                activity.downloadReady();
            } else {
                activity.downloadError();
            }
        }
    }
}