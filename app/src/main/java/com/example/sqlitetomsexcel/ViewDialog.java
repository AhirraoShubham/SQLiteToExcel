package com.example.sqlitetomsexcel;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;

public class ViewDialog {
    private Activity activity;
    private Dialog dialog;

    public ViewDialog(Activity activity){
        this.activity=activity;
    }

    public void showDialog(){
        dialog=new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_show_progress);
        dialog.show();
    }

    //For Hide Dialog
    public void hideDialog(){
        dialog.dismiss();
    }
}
