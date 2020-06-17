package com.example.login.ui.code_qr;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.login.R;
import com.example.login.Utilidades.Utilidades;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Code_qrFragment extends Fragment {

    private Code_qrViewModel codeqrViewModel;

    private TextView tv_nombreUsuario, tv_tipoUsuario;
    private ImageView imageQr;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        codeqrViewModel = ViewModelProviders.of(this).get(Code_qrViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        getUsuarioInfo(Utilidades.token_id);
        initComponents(root);
        return root;
    }

    private void getUsuarioInfo(String token_id) {
        String url = Utilidades.ip_server+"/stocker_web_services/consultaUsuarioQR.php?clave_usuario="+token_id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray = response.optJSONArray("json");
                JSONObject json = null;

                try {
                    json = jsonArray.getJSONObject(0);
                    String nombre = json.optString("nombre")+" "+json.optString("app") +" "+json.optString("apm") ;
                    String tipo = "Nivel: "+json.optString("tipo");
                    tv_nombreUsuario.setText(nombre.toUpperCase());
                    tv_tipoUsuario.setText(tipo);
                    String clave_usuario = json.optString("clave_usuario");
                    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

                    try {
                        BitMatrix bitMatrix = multiFormatWriter.encode(clave_usuario, BarcodeFormat.QR_CODE, 300, 300);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        imageQr.setImageBitmap(bitmap);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonObjectRequest);
    }

    private void initComponents(View root) {
        tv_nombreUsuario = root.findViewById(R.id.tv_nombreusuario);
        tv_tipoUsuario = root.findViewById(R.id.tv_tipousuario);
        imageQr = root.findViewById(R.id.image_qr);
    }
}