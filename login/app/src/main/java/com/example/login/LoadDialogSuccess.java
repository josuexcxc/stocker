package com.example.login;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.TextView;

public class LoadDialogSuccess {

    final Dialog dialog;
    TextView txt_mensaje;
    final String text;
    public LoadDialogSuccess(Context context, String msj) {
        dialog= new Dialog(context);
        text = msj;

    }
    public void startLoad(){
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.activity_success);
        txt_mensaje= dialog.findViewById(R.id.tv_dialog);
        txt_mensaje.setText(text);
        dialog.show();
    }
    public void stopLoad(){
        dialog.dismiss();
    }
}
