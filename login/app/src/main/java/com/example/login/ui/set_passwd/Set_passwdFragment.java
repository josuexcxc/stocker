package com.example.login.ui.set_passwd;

import android.os.Bundle;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.login.LoadDialog;
import com.example.login.LoadDialogSuccess;
import com.example.login.LoadDialogUnSuccess;
import com.example.login.R;
import com.example.login.Utilidades.Utilidades;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Set_passwdFragment extends Fragment {

    private Set_passwdViewModel setpasswdViewModel;
    private EditText et_actual,et_nueva,et_repetirnueva;
    private CheckBox check;
    private Button btn_cambiar;
    private String pw_actual,pw_nueva,pw_repetir;
    String passwd;
    private TextView tv_error;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setpasswdViewModel = ViewModelProviders.of(this).get(Set_passwdViewModel.class);

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        initComponents(root);

        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    hideEdits();
                }else{
                    showEdits();
                }
            }
        });

        btn_cambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               pw_actual = et_actual.getText().toString().trim();
               pw_nueva =et_nueva.getText().toString().trim();
               pw_repetir = et_repetirnueva.getText().toString().trim();
               checkPasswdActual(Utilidades.token_id);
            }
        });
        return root;
    }

    public void checkPasswdActual(String clave_usuario){

        if(same_password()){
            updatePassword(clave_usuario,pw_actual,pw_nueva);

        }else{

            tv_error.setText("Las contraseñas no coinciden");
            et_nueva.requestFocus();
        }
    }

    public void updatePassword(String clave_usuario, String old_passwd, String new_passwd){

        String url = Utilidades.ip_server+"/stocker_web_services/updatePassword.php?clave_usuario="+clave_usuario+"&old_passwd="+old_passwd+"&new_passwd="+new_passwd;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("0")){
                    DialogUnSucces("Contraseña actual\n incorrecta");
                    et_actual.requestFocus();
                }else{
                    DialogSucces("Contraseña actualizada");
                    clearEdits();
                    et_actual.requestFocus();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    public void DialogSucces(String msj){
        final LoadDialogSuccess dialog = new LoadDialogSuccess(getContext(),msj);
        dialog.startLoad();
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.stopLoad();
            }
        },2000);
    }

    public void DialogUnSucces(String msj){
        final LoadDialogUnSuccess dialog = new LoadDialogUnSuccess(getContext(),msj);
        dialog.startLoad();
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.stopLoad();
            }
        },2000);
    }

    public void clearEdits(){
        et_repetirnueva.setText("");
        et_nueva.setText("");
        et_actual.setText("");
    }

    public boolean same_password(){
        if(pw_nueva.equals(pw_repetir)){
            tv_error.setText("");
            return true;
        }else{
            tv_error.setText("Las contraseñas no coinciden");
            return false;
        }
    }



    public void hideEdits(){
        et_actual.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        et_nueva.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        et_repetirnueva.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
    }
    public void showEdits(){
        et_actual.setTransformationMethod(PasswordTransformationMethod.getInstance());
        et_nueva.setTransformationMethod(PasswordTransformationMethod.getInstance());
        et_repetirnueva.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    private void initComponents(View root) {
        et_actual = root.findViewById(R.id.et_actual);
        et_nueva = root.findViewById(R.id.et_nueva);
        et_repetirnueva = root.findViewById(R.id.et_repetirnueva);
        check = root.findViewById(R.id.check);
        btn_cambiar = root.findViewById(R.id.btn_cambiar);
        tv_error = root.findViewById(R.id.tv_error);
    }
}