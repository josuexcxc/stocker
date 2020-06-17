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
import com.android.volley.toolbox.Volley;
import com.example.login.Objetos.ProductoVo;
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
public class ControlProductosFragment extends Fragment {

    private Button btn_finalizar,btn_scanProducto, btn_add_to_lista;
    private EditText et_productos;
    private RecyclerView rlista;
    private String cantidadRecibida;
    private RequestQueue requestQueue;
    private JsonObjectRequest requestConsultaProducto,jsonRequestRegistroEntrada,jsonRequestDetailEntrada;
    private ArrayList<ProductoVo> listaProductos;
    private AdaptadorProductoVo adaptadorProducto;
    private RadioButton rbe_producto, rbs_producto;

    private ArrayList<String> tipos_movimiento;

    private String clave_temporal = "";
    private int btn_clicked = 0;

    public ControlProductosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_control_productos, container, false);
        initComponents(root);



        rlista.setLayoutManager(new LinearLayoutManager(getContext()));
        btn_scanProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_clicked = 1;
                cantidadRecibida = et_productos.getText().toString();
                if(TextUtils.isEmpty(cantidadRecibida)){
                    et_productos.setError("Ingresa la cantidad");
                    et_productos.requestFocus();
                }
                else{
                    escanear();
                    et_productos.setText("");
                }
            }
        });

        // ================= btn para adicionar otro elemnto a la lista de productos
        btn_add_to_lista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consulta_producto(clave_temporal);
                clave_temporal ="";
            }
        });

        // ====================== Boton para registrar todos los elemento escaneados

        btn_finalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_clicked = 2;
                escanear();


            }
        });

        listaProductos = new ArrayList<ProductoVo>();
        adaptadorProducto = new AdaptadorProductoVo(listaProductos);
        requestQueue= Volley.newRequestQueue(getContext());
        rlista.setAdapter(adaptadorProducto);
        return root;
    }

    private void registrar(final String uentrega) {
        //inicio del loadDialog
        final LoadDialog loadDialog = new LoadDialog(getContext(),"Cargando...");
        loadDialog.startLoad();
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadDialog.stopLoad();
                if(listaProductos.size()!=0) {
                    if (rbe_producto.isChecked()) {
                        Utilidades.clave_movimiento = "1";
                    }
                    if (rbs_producto.isChecked()) {
                        Utilidades.clave_movimiento = "2";
                    }
                    registrarTransferenciaProducto(Utilidades.clave_movimiento,uentrega,Utilidades.token_id);

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
        btn_finalizar = root.findViewById(R.id.btn_finalizar);
        btn_scanProducto = root.findViewById(R.id.btn_scanProducto);
        et_productos = root.findViewById(R.id.et_cantidad);
        rlista = root.findViewById(R.id.listEntradaProductos);
        rbe_producto = root.findViewById(R.id.rbe_producto);
        rbs_producto = root.findViewById(R.id.rbs_producto);
        btn_add_to_lista = root.findViewById(R.id.btn_add_to_lista);
    }

    public void getTiposMovimiento(){
        String url = "http://192.168.0.13/tequilera_web_services/getTiposMovimiento.php";
        JsonObjectRequest jsonObjectRequest =new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray = response.optJSONArray("movimiento");
                JSONObject jsonObject1 = null;

                try {
                    for(int x=0 ; x<jsonArray.length() ; x++){
                        jsonObject1=jsonArray.getJSONObject(x);
                        tipos_movimiento.add(jsonObject1.optString("descripcion"));

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
        RequestQueue requestQueue_tm= Volley.newRequestQueue(getContext());
        requestQueue_tm.add(jsonObjectRequest);
    }

    public void escanear(){
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(et_productos.getWindowToken(), 0);
        IntentIntegrator intentIntegrator = IntentIntegrator.forSupportFragment(ControlProductosFragment.this);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intentIntegrator.setPrompt("Escanea el código...");
        intentIntegrator.setCameraId(0);
        intentIntegrator.setBeepEnabled(false);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setBarcodeImageEnabled(false);
        intentIntegrator.initiateScan();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result != null){
            if(result.getContents() == null){

                Toast.makeText(getContext(),"Was canceled",Toast.LENGTH_SHORT).show();

            }else{

                clave_temporal = result.getContents().toString();

                if (btn_clicked == 1){

                }else if(btn_clicked == 2){
                    registrar(clave_temporal);
                }

            }
        }
    }

    private void consulta_producto(String clave_producto) {
        String url = Utilidades.ip_server+"/stocker_web_services/consultaProducto.php?clave_producto="+clave_producto;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray = response.optJSONArray("json");
                JSONObject json = null;

                try {
                    json = jsonArray.getJSONObject(0);
                    boolean res = buscarId(json.optString("clave_producto"));
                    if(res){
                        Utilidades.ctotal = Integer.parseInt(listaProductos.get(Utilidades.position).getCantidad())+Integer.parseInt(cantidadRecibida);
                        listaProductos.get(Utilidades.position).setCantidad(String.valueOf(Utilidades.ctotal));
                        adaptadorProducto.notifyItemChanged(Utilidades.position);
                    }else{
                        ProductoVo p = new ProductoVo();
                        p.setClave_producto(json.optString("clave_producto"));
                        p.setNombre(json.optString("nombre"));
                        p.setPresentacion(json.optString("presentacion"));
                        p.setCantidad(cantidadRecibida);
                        listaProductos.add(p);
                        adaptadorProducto.notifyItemInserted(listaProductos.size()-1);

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

    public boolean buscarId(String id){
        boolean found=false;
        for(int x =0;x<listaProductos.size();x++){
            if(listaProductos.get(x).getClave_producto().equals(id)){
                UtilidadesInsumo.position=x;
                found=true;
                break;
            }
        }//fin del for
        return found;
    }

    public void registrarTransferenciaProducto(String clave_mov, String uentrega, String urecibe){
        String url = Utilidades.ip_server+"/stocker_web_services/transferenciaProducto.php?clave_movimiento="+clave_mov+"&uentrega="+uentrega+"&urecibe="+urecibe;
        url = url.replace(" ","%20");
        jsonRequestRegistroEntrada = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray = response.optJSONArray("json");
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = jsonArray.getJSONObject(0);
                    String clave_transferencia = jsonObject1.optString("clave_transferencia");
                    for(int x=0;x<listaProductos.size();x++){
                        detalleTransferenciaProducto(clave_transferencia,listaProductos.get(x).getClave_producto(),listaProductos.get(x).getCantidad(),Utilidades.clave_movimiento);
                    }
                    final LoadDialogSuccess loadDialog = new LoadDialogSuccess(getContext(),"Registro exitoso");
                    loadDialog.startLoad();
                    Handler h = new Handler();
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadDialog.stopLoad();

                            listaProductos.clear();
                            Utilidades.ctotal=0;
                            Utilidades.position=0;
                            Utilidades.id="";
                            clave_temporal="";
                            btn_clicked=0;
                            rlista.setAdapter(adaptadorProducto);

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

    public void detalleTransferenciaProducto(String clave_transferencia, String clave_producto, String cantidad, String clave_movimiento){
        String url = Utilidades.ip_server+"/stocker_web_services/detalle_tp.php?clave_transferencia="+clave_transferencia+"&clave_producto="+clave_producto+"&cantidad="+cantidad+"&clave_movimiento="+clave_movimiento;
        url = url.replace(" ","%20");
        jsonRequestDetailEntrada =new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("E"+error);
            }
        });
        requestQueue.add(jsonRequestDetailEntrada);
    }


}

