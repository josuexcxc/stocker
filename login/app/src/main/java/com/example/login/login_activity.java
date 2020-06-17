package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.login.Utilidades.Utilidades;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class login_activity extends AppCompatActivity {

    private EditText usuario;
    private EditText password;
    private Button btn_login;
    private Typeface sf;
    private CheckBox checkBox;
    Context context;
    private String user;
    private String passwd;

    // variables para volley
    RequestQueue requestQueue;
    JsonRequest jsonRequest;
    public String name,path_img,id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        context=this;
        String fuente1= "fonts/SFCompactDisplay-Light.otf";
        this.sf = Typeface.createFromAsset(getAssets(),fuente1);
        usuario = findViewById(R.id.editText);
        password = findViewById(R.id.editText2);
        btn_login = findViewById(R.id.button2);
        checkBox = findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        // asignacion de fuente a los widgets
        usuario.setTypeface(sf);
        password.setTypeface(sf);
        btn_login.setTypeface(sf);

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user = usuario.getText().toString().trim();
                passwd = password.getText().toString().trim();

                if(TextUtils.isEmpty(user)){
                    usuario.setError("Ingrese su usuario");
                    usuario.requestFocus();
                }else if (TextUtils.isEmpty(passwd)){
                    password.setError("Ingrese su contraseña");
                    password.requestFocus();
                }else{
                    final LoadDialog loadDialog = new LoadDialog(context,"Cargando...");
                    loadDialog.startLoad();
                    Handler h = new Handler();
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadDialog.stopLoad();
                            iniciar_sesion(user,passwd);
                        }
                    },1500);
                }



            }
        });
    }

    private void iniciar_sesion(String user, String passwd) {
        String url = Utilidades.ip_server+"/stocker_web_services/login.php?user="+user+"&passwd="+passwd;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray jsonArray = response.optJSONArray("json");
                JSONObject json = null;

                try {
                    json = jsonArray.getJSONObject(0);
                    Usuario u = new Usuario(json.optString("clave_usuario"), json.optString("usuario"), json.optString("passwd"), json.optString("imagen"), json.optString("clave_tipo"), json.optString("nombre"), json.optString("app"));
                    Intent start = new Intent(getApplicationContext(),SlideMenuActivity.class);
                    start.putExtra("clave_usuario",u.getClave_usuario());
                    start.putExtra("nombre",u.getNombre()+" "+u.getAppaterno());
                    start.putExtra("path_img",u.getImg_path());
                    Utilidades.token_id=json.optString("clave_usuario");
                    Utilidades.tipo_usuario = json.optString("clave_tipo");
                    startActivity(start);

                } catch (JSONException e) {

                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(),"Wrogn",Toast.LENGTH_LONG).show();
                final LoadDialogUnSuccess loadDialogUnSuccess= new LoadDialogUnSuccess(context,"Datos Incorrectos\n Intente de nuevo");
                loadDialogUnSuccess.startLoad();
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadDialogUnSuccess.stopLoad();
                    }
                },800);
            }
        });
        requestQueue.add(jsonObjectRequest);
    }


    @Override
    public void onBackPressed() {

    }
}
