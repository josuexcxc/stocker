package com.example.login;


import android.app.DatePickerDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login.Objetos.ProductoVo;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.login.Utilidades.Utilidades;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;


public class OrdenProduccionFragment extends Fragment {


    public OrdenProduccionFragment() {
        // Required empty public constructor
    }

    Button btn_add, btn_date, btn_scan, btn_finalizar;
    TextView et_p1_1, tv_date, tv_p1_2, tv_p1_3;

    Calendar c;
    DatePickerDialog datePickerDialog;

    RecyclerView rlista;
    EditText et_id_producto, et_cantidad;
    RequestQueue requestQueue;
    JsonRequest jsonRequest, jsonRequestNewOrden, jsonRequestNewOrdenDetails;

    AdaptadorProductoVo adaptadorProducto;
    String id, descripcion, presentacion, cantidad, date_entrega, last_id;
    Context context;
    Spinner spinner_clientes;
    ArrayList<ClienteVo> listaClientes;
    ArrayList<String> listaClientesMostrar;

    //variables para la query sql
    private String _clave_cliente = "";
    private String _fecha_entrega = "";
    private String _clave_orden = "";
    //private int _cantidad = 0;
    private String _cantidad="";

    //listas
    ArrayList<ProductoVo> listaProductos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_orden_produccion, container, false);
        initComponents(root);
        listaClientes = new ArrayList<ClienteVo>();
        listaClientesMostrar = new ArrayList<>();
        listaProductos = new ArrayList<ProductoVo>();
        rlista.setLayoutManager(new LinearLayoutManager(getContext()));
        adaptadorProducto = new AdaptadorProductoVo(listaProductos);
        rlista.setAdapter(adaptadorProducto);
        consultarClientes();
        getCliente();

        // iniciar el escaneo del producto
        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _cantidad = et_cantidad.getText().toString();
                if(TextUtils.isEmpty(_cantidad)){
                    et_cantidad.setError("Necesita ingresar la cantidad");
                    et_cantidad.requestFocus();
                }else{
                    escanear();
                    et_cantidad.setText("");
                }

            }
        });


        // finalizar la orde de produccion, teniendo la lista de productos ya lista
        btn_finalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_clave_cliente.equals("") || tv_date.getText().toString().equals("--/--/--")){
                    final LoadDialogUnSuccess loadDialogUnSuccess= new LoadDialogUnSuccess(getContext(),"Hay campos vacíos\n Intente de nuevo");
                    loadDialogUnSuccess.startLoad();
                    Handler h = new Handler();
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadDialogUnSuccess.stopLoad();
                        }
                    },800);
                }else if(listaProductos.isEmpty()){
                    final LoadDialogUnSuccess loadDialogUnSuccess= new LoadDialogUnSuccess(getContext(),"Necesitas agregar\n productos a la lista");
                    loadDialogUnSuccess.startLoad();
                    Handler h = new Handler();
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadDialogUnSuccess.stopLoad();
                            //Toast.makeText(getContext(),_fecha_entrega,Toast.LENGTH_LONG).show();
                        }
                    },800);

                }else{
                    final LoadDialog loadDialog = new LoadDialog(getContext(),"Cargando...");
                    loadDialog.startLoad();
                    Handler h = new Handler();
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadDialog.stopLoad();
                            registrarOrdenProduccion();
                        }
                    },1500);
                }



            }//onclick
        });

        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionarFechaEntrega();
            }
        });

        return root;
    }

    private void registrarOrdenProduccion() {
        String url = Utilidades.ip_server+"/stocker_web_services/ordenProduccion.php?f="+_fecha_entrega+"&c="+_clave_cliente;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray = response.optJSONArray("json");
                JSONObject json = null;

                try {
                    json = jsonArray.getJSONObject(0);
                    _clave_orden = json.optString("clave_orden");
                    for( int x=0 ; x<listaProductos.size() ; x++){
                        registrarDetalleOP(_clave_orden,listaProductos.get(x).getClave_producto(),listaProductos.get(x).getCantidad());
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
                            rlista.setAdapter(adaptadorProducto);

                        }
                    },1500);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                final LoadDialogUnSuccess dialog = new LoadDialogUnSuccess(getContext(),"Algo falló\nIntentalo nuevamente");
                dialog.startLoad();
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.stopLoad();
                    }
                },1500);
            }
        })      ;
        RequestQueue requestQueue=Volley.newRequestQueue(getContext());
        requestQueue.add(jsonObjectRequest);

    }

    private void registrarDetalleOP(String clave_orden, String clave_producto, String cantidad) {
        String url = Utilidades.ip_server+"/stocker_web_services/detalleOrdenProduccion.php?clave_orden="+clave_orden+"&clave_producto="+clave_producto+"&cantidad="+cantidad;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonObjectRequest);
    }

    private void seleccionarFechaEntrega() {
        c = Calendar.getInstance();
        final int day = c.get(Calendar.DAY_OF_MONTH);
        int mont = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tv_date.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                _fecha_entrega = year+"-"+(month+1)+"-"+dayOfMonth;
            }
        } ,year, mont, day);
        datePickerDialog.show();
    }

    public void escanear(){
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(et_cantidad.getWindowToken(), 0);
        IntentIntegrator intentIntegrator = IntentIntegrator.forSupportFragment(OrdenProduccionFragment.this);
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
                String clave_producto=result.getContents().toString();
                consulta_producto(clave_producto);
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
                        Utilidades.ctotal = Integer.parseInt(listaProductos.get(Utilidades.position).getCantidad())+Integer.parseInt(_cantidad);
                        listaProductos.get(Utilidades.position).setCantidad(String.valueOf(Utilidades.ctotal));
                        adaptadorProducto.notifyItemChanged(Utilidades.position);
                    }else{
                        ProductoVo p = new ProductoVo();
                        p.setClave_producto(json.optString("clave_producto"));
                        p.setNombre(json.optString("nombre"));
                        p.setPresentacion(json.optString("presentacion"));
                        p.setCantidad(_cantidad);

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
                Utilidades.position=x;
                found=true;
                break;
            }
        }//fin del for
        return found;
    }

    private void getCliente() {
        spinner_clientes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    //Toast.makeText(getContext(),parent.getItemAtPosition(position).toString(),Toast.LENGTH_LONG).show();
                    //System.out.println(parent.getItemAtPosition(position).toString());
                    _clave_cliente = listaClientes.get(position-1).getClave_cliente();

                }else{
                    _clave_cliente="";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void consultarClientes() {
        String url = Utilidades.ip_server+"/stocker_web_services/consultaClientes.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray = response.optJSONArray("json");
                JSONObject jsonObject1 = null;

                try {
                    for (int x = 0; x < jsonArray.length(); x++) {
                        jsonObject1 = jsonArray.getJSONObject(x);
                        ClienteVo cliente = new ClienteVo(jsonObject1.optString("clave_cliente"), jsonObject1.optString("nombre"), jsonObject1.optString("app"));
                        listaClientes.add(cliente);
                    }
                    listaClientesSPinner();

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

    public void listaClientesSPinner() {

        listaClientesMostrar.add("Seleccione...");

        for (int x = 0; x < listaClientes.size(); x++) {
            listaClientesMostrar.add(listaClientes.get(x).getNombre() + " " + listaClientes.get(x).getApp());
        }
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,listaClientesMostrar);
        spinner_clientes.setAdapter(adapterSpinner);
    }

    public void initComponents(View root) {
        //btn_add = root.findViewById(R.id.btn_add);
        //et_p1_1 = root.findViewById(R.id.et_p1_2);
        tv_date = root.findViewById(R.id.tv_date);
        btn_date = root.findViewById(R.id.btn_date);
        et_cantidad = root.findViewById(R.id.et_cantidad);
        btn_scan = root.findViewById(R.id.scan_einsumo);
        rlista = root.findViewById(R.id.rlista);
        btn_finalizar = root.findViewById(R.id.btn_finalizar);
        spinner_clientes = root.findViewById(R.id.spinner_clientes);
    }

}
