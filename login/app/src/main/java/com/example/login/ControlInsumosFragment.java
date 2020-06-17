package com.example.login;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.login.Adaptadores.AdaptadorInsumo;
import com.example.login.Objetos.InsumoVo;
import com.example.login.Utilidades.Utilidades;
import com.example.login.Utilidades.UtilidadesInsumo;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ControlInsumosFragment extends Fragment {

    String cantidadRecibida;
    EditText et_cantidad;
    Button btn_scan,btn_finalizar, btn_add_to_lista;
    RecyclerView lista_insumos;
    private RequestQueue requestQueue;
    private JsonRequest jsonRequest,jsonRequestRegistroEntrada,jsonRequestDetailEntrada;
    ArrayList<InsumoVo> listaInsumos;
    AdaptadorInsumo adaptadorInsumo;
    RadioButton rbe_insumo, rbs_insumo;

    private String clave_temporal="";
    private int btn_clicked =0;
    public ControlInsumosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root=inflater.inflate(R.layout.fragment_control_insumos, container, false);
        initComponents(root);
        lista_insumos.setLayoutManager(new LinearLayoutManager(getContext()));

        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_clicked = 1;
                cantidadRecibida=et_cantidad.getText().toString();
                if(TextUtils.isEmpty(cantidadRecibida)){
                    et_cantidad.setError("Ingresa la cantidad");
                    et_cantidad.requestFocus();
                }else {
                    escanear();
                    et_cantidad.setText("");

                }
            }
        });

        // ==============================añadir elementos a la lista =======

        btn_add_to_lista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultaInsumo(clave_temporal);
                clave_temporal ="";
            }
        });

        // ======================= registrar la transferencia ===============
        btn_finalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_clicked = 2;
                escanear();
            }
        });

        listaInsumos=new ArrayList<InsumoVo>();
        requestQueue = Volley.newRequestQueue(getContext());
        adaptadorInsumo = new AdaptadorInsumo(listaInsumos);
        lista_insumos.setAdapter(adaptadorInsumo);
        return root;
    }

    private void registrar(final String uentrega) {
        final LoadDialog loadDialog = new LoadDialog(getContext(),"Cargando...");
        loadDialog.startLoad();
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadDialog.stopLoad();
                if(listaInsumos.size()!=0) {
                    if (rbe_insumo.isChecked()) {
                        Utilidades.clave_movimiento = "1";
                    }
                    if (rbs_insumo.isChecked()) {
                        Utilidades.clave_movimiento = "2";
                    }

                    registrarEntrada(Utilidades.clave_movimiento,uentrega,Utilidades.token_id);

                }else{

                    final LoadDialogUnSuccess loadDialog = new LoadDialogUnSuccess(getContext(),"Campos vacíos");
                    loadDialog.startLoad();
                    Handler h = new Handler();
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadDialog.stopLoad();
                        }
                    },200);
                    //fin del loadDialog
                }
            }
        },5500);
    }

    public void initComponents(View root){
        et_cantidad = root.findViewById(R.id.et_cantidad);
        btn_scan = root.findViewById(R.id.scan_einsumo);
        lista_insumos = root.findViewById(R.id.listEntradaInsumos);
        btn_finalizar = root.findViewById(R.id.btn_finalizar);
        rbe_insumo = root.findViewById(R.id.rbe_insumo);
        rbs_insumo = root.findViewById(R.id.rbs_insumo);
        btn_add_to_lista = root.findViewById(R.id.btn_add_to_lista);

    }

    public void escanear(){
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(et_cantidad.getWindowToken(), 0);
        IntentIntegrator intentIntegrator = IntentIntegrator.forSupportFragment(ControlInsumosFragment.this);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intentIntegrator.setPrompt("Escanea el código...");
        intentIntegrator.setCameraId(0);
        intentIntegrator.setBeepEnabled(false);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setBarcodeImageEnabled(false);
        intentIntegrator.initiateScan();

        System.out.println("escanear");

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result != null){
            if(result.getContents() == null){
                Toast.makeText(getContext(),"Was canceled",Toast.LENGTH_SHORT).show();
            }else{
                clave_temporal = result.getContents().toString();
                if(btn_clicked == 2){
                    registrar(clave_temporal);
                    //Toast.makeText(getContext(),"Boton FINALIZAR",Toast.LENGTH_LONG).show();
                }else if(btn_clicked == 1){
                    //Toast.makeText(getContext(),"Boton SCAN",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void consultaInsumo(String id){
        String url = Utilidades.ip_server+"/stocker_web_services/consultaInsumo.php?clave_insumo="+id;
        url =url.replace(" ","%20");
        jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray = response.optJSONArray("json");
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 =jsonArray.getJSONObject(0);
                    if(buscarId(jsonObject1.optString("clave_insumo"))){
                        UtilidadesInsumo.total_insumos = Integer.parseInt(listaInsumos.get(UtilidadesInsumo.position).getCantidad_r())+Integer.parseInt(cantidadRecibida);
                        listaInsumos.get(UtilidadesInsumo.position).setCantidad_r(String.valueOf(UtilidadesInsumo.total_insumos));
                        adaptadorInsumo.notifyItemChanged(UtilidadesInsumo.position);
                        clave_temporal="";
                    }else {
                        InsumoVo i = new InsumoVo();
                        i.setClave_insumo(jsonObject1.optString("clave_insumo"));
                        i.setNombre(jsonObject1.optString("nombre"));
                        i.setUnida(jsonObject1.optString("unidad"));
                        i.setCantidad(jsonObject1.optString("cantidad"));
                        i.setCantidad_r(cantidadRecibida);
                        listaInsumos.add(i);
                        adaptadorInsumo.notifyItemInserted(listaInsumos.size()-1);
                        clave_temporal="";
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
        requestQueue.add(jsonRequest);
    }

    public boolean buscarId(String id){
        boolean found=false;
        for(int x =0;x<listaInsumos.size();x++){
            if(listaInsumos.get(x).getClave_insumo().equals(id)){
                UtilidadesInsumo.position=x;
                found=true;
                break;
            }
        }//fin del for
        return found;
    }

    public void registrarEntrada(String clave_mov, String uentrega, String urecibe){
        String url = Utilidades.ip_server+"/stocker_web_services/transferenciaInsumo.php?clave_movimiento="+clave_mov+"&uentrega="+uentrega+"&urecibe="+urecibe;
        url = url.replace(" ","%20");
        jsonRequestRegistroEntrada = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray = response.optJSONArray("json");
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = jsonArray.getJSONObject(0);
                    String clave_transferencia = jsonObject1.optString("clave_transferencia");
                    //Toast.makeText(getContext(),clave_transferencia,Toast.LENGTH_LONG).show();
                    for(int x=0;x<listaInsumos.size();x++){
                        detailsEntradaInsumos(clave_transferencia,listaInsumos.get(x).getClave_insumo(),listaInsumos.get(x).getCantidad_r(),Utilidades.clave_movimiento);
                    }
                    final LoadDialogSuccess loadDialog = new LoadDialogSuccess(getContext(),"Registro exitoso");
                    loadDialog.startLoad();
                    Handler h = new Handler();
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadDialog.stopLoad();
                            listaInsumos.clear();
                            Utilidades.ctotal=0;
                            Utilidades.position=0;
                            Utilidades.id="";
                            clave_temporal="";
                            lista_insumos.setAdapter(adaptadorInsumo);

                        }
                    },1500);
                    //fin del loadDialog
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                final LoadDialogUnSuccess dialog = new LoadDialogUnSuccess(getContext(),"Vuelve a escaner\nel código QR");
                dialog.startLoad();
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        clave_temporal="";
                        dialog.stopLoad();
                    }
                },1500);
            }
        });
        requestQueue.add(jsonRequestRegistroEntrada);

    }

    public void detailsEntradaInsumos(String clave_transferencia,String clave_insumo,String cantidad, String clave_movimiento){
        String url = Utilidades.ip_server+"/stocker_web_services/detalle_ti.php?clave_transferencia="+clave_transferencia+"&clave_insumo="+clave_insumo+"&cantidad="+cantidad+"&clave_movimiento="+clave_movimiento;
        url = url.replace(" ","%20");
        jsonRequestDetailEntrada =new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonRequestDetailEntrada);
    }

}

