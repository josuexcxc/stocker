package com.example.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

public class LoadDialog {

    final Dialog dialog;
    TextView txt_mensaje;
    final String text;
    public LoadDialog(Context context, String msj) {
        dialog= new Dialog(context);
        text = msj;

    }
    public void startLoad(){
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.activity_load_bar);
        txt_mensaje= dialog.findViewById(R.id.tv_dialog);
        txt_mensaje.setText(text);
        dialog.show();
    }
    public void stopLoad(){
        dialog.dismiss();
    }
}
