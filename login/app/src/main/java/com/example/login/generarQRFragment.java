package com.example.login;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * A simple {@link Fragment} subclass.
 */
public class generarQRFragment extends Fragment {


    public generarQRFragment() {
        // Required empty public constructor
    }
    private Button btn_generar_qr, btn_guardar_qr;
    private ImageView imageView_qr;
    private EditText editText_qr;
    OutputStream outputStream;

    private String name_qr="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_generar_qr, container, false);
        btn_generar_qr = root.findViewById(R.id.button9);
        imageView_qr = root.findViewById(R.id.image_qr);
        editText_qr = root.findViewById(R.id.editText3);
        btn_guardar_qr = root.findViewById(R.id.btn_guardarqr);
        btn_generar_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = editText_qr.getText().toString().trim();
                name_qr = id;
                if (TextUtils.isEmpty(id)){
                    editText_qr.setError("Ingrese el código");
                    editText_qr.requestFocus();
            }else{
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(editText_qr.getWindowToken(), 0);
                    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

                    try {
                        BitMatrix bitMatrix = multiFormatWriter.encode(id, BarcodeFormat.QR_CODE, 200, 200);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        imageView_qr.setImageBitmap(bitmap);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                    editText_qr.setText("");
                }
            }
        });

        btn_guardar_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarQR();
            }
        });

        return root;
    }

    private void guardarQR() {
        BitmapDrawable drawable = (BitmapDrawable) imageView_qr.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        //IMPLEMENTACION PARA GUARDAR EL QR EN EL TELEFONO AUTOMATICAMENTE
        File filePath = Environment.getExternalStorageDirectory();
        File dir = new File(filePath.getAbsolutePath() + "/Códigos QR/");
        dir.mkdir();
        File file = new File(dir, name_qr + ".jpg");
        try {
            outputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        final LoadDialogSuccess dialog = new LoadDialogSuccess(getContext(),"Código guardado\nen el telefono");
        dialog.startLoad();
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.stopLoad();
            }
        },2000);
        try {
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
