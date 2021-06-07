package com.example.localdiskieapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class LoadingBar {
   private Activity activity;
   private AlertDialog dialog;

    public LoadingBar(Activity activity) {
        this.activity = activity;
    }
    void startLoading(){
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        LayoutInflater inflater=activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.progressbar_layout,null));
        dialog=builder.create();
        dialog.show();
    }
    void stopLoading()
    {
        dialog.dismiss();
    }
}
