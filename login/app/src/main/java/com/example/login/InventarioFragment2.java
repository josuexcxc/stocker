package com.example.login;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.login.Utilidades.Utilidades;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.provider.PieChartDataProvider;
import lecho.lib.hellocharts.view.PieChartView;


/**
 * A simple {@link Fragment} subclass.
 */
public class InventarioFragment2 extends Fragment {
    
    private PieChartView grafica2;
    private TextView tv_listado;
    public Typeface font1;
    private  TextView inv_tv_nombre, inv_tv_descripcion, inv_tv_stock, inv_tv_ocupado, inv_tv_disponible;
    private RadioButton rbc_producto, rbc_insumo;
    private Button btn_scanProducto;

    private String clave_temporal ="";


    // ===================== metodo ONCREATE =================
    public InventarioFragment2() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_inventario_fragment2, container, false);
        initComponents(root);

        tv_listado.setText(Html.fromHtml("<u>Ver listado completo</u>"));
        //grafica2.setChartRotationEnabled(false);
        tv_listado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stockCompletoFragment stockCompletoFragment= new stockCompletoFragment();
                Bundle args = new Bundle();
                if(rbc_producto.isChecked()){
                    args.putString("consulta","productos");
                }
                else if(rbc_insumo.isChecked()) {
                    args.putString("consulta","insumos");
                }
                stockCompletoFragment.setArguments(args);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment,stockCompletoFragment);
                transaction.commit();
            }
        });

        btn_scanProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escanear();
            }
        });
        return root;
    }

    // ========================= MÉTODOS =====================================

    public void escanear(){
        //InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        //inputMethodManager.hideSoftInputFromWindow(et_cantidad.getWindowToken(), 0);
        IntentIntegrator intentIntegrator = IntentIntegrator.forSupportFragment(InventarioFragment2.this);
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
                getStock(clave_temporal);

            }
        }

    }

    private void getStock(String clave) {
        if(rbc_producto.isChecked()){

            getStockProducto(clave);

        }else if(rbc_insumo.isChecked()){

            getStockInsumo(clave);

        }
    }

    private void getStockProducto(String clave_producto) {
        String url = Utilidades.ip_server+"/stocker_web_services/consultaProducto.php?clave_producto="+clave_producto;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray = response.optJSONArray("json");
                JSONObject jsonObject1 = null;

                try {
                    jsonObject1 = jsonArray.getJSONObject(0);
                    String nombre = jsonObject1.optString("nombre");
                    String presentacion = jsonObject1.optString("presentacion");
                    String stock = jsonObject1.optString("stock");
                    String stock_max = jsonObject1.optString("stock_max");

                    inv_tv_nombre.setText(nombre);
                    inv_tv_descripcion.setText(presentacion);
                    inv_tv_stock.setText(stock);

                    crearGrafica(Integer.parseInt(stock),Integer.parseInt(stock_max));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                final LoadDialogUnSuccess dialogo = new LoadDialogUnSuccess(getContext(),"Producto no registrado");
                dialogo.startLoad();
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialogo.stopLoad();
                    }
                },2000);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonObjectRequest);

    }

    private void getStockInsumo(String clave_insumo) {

        String url = Utilidades.ip_server+"/stocker_web_services/consultaInsumo.php?clave_insumo="+clave_insumo;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray = response.optJSONArray("json");
                JSONObject jsonObject1 = null;

                try {

                    jsonObject1 = jsonArray.getJSONObject(0);
                    String nombre = jsonObject1.optString("nombre");
                    String description = jsonObject1.optString("unidad")+" con "+ jsonObject1.optString("cantidad")+" piezas";
                    String stock = jsonObject1.optString("stock");
                    String stock_max = jsonObject1.optString("stock_max");

                    inv_tv_nombre.setText(nombre);
                    inv_tv_descripcion.setText(description);
                    inv_tv_stock.setText(stock);

                    crearGrafica(Integer.parseInt(stock),Integer.parseInt(stock_max));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                final LoadDialogUnSuccess dialogo = new LoadDialogUnSuccess(getContext(),"Insumo no registrado");
                dialogo.startLoad();
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialogo.stopLoad();
                    }
                },2000);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonObjectRequest);
    }

    private void crearGrafica(int stock, int storage) {
        inv_tv_ocupado.setText(String.valueOf(stock));
        inv_tv_disponible.setText(String.valueOf(storage-stock));
        List pieData = new ArrayList<>();
        pieData.add(new SliceValue(stock, Color.parseColor("#e4bf00")).setLabel(""));
        pieData.add(new SliceValue((storage-stock), Color.parseColor("#EEEEEE")).setLabel(""));
        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true).setValueLabelTextSize(14);
        int porcentaje = (stock*100)/storage;
        String porc =String.valueOf(porcentaje)+"%";
        String fuente1= "fonts/poeone.ttf";
        font1 = Typeface.createFromAsset(getActivity().getAssets(),fuente1);
        pieChartData.setHasCenterCircle(true).setCenterText1(porc).setCenterText1FontSize(30).setCenterText1Color(Color.parseColor("#3a3a3a")).setCenterText1Typeface(font1);
        grafica2.startDataAnimation();
        grafica2.setPieChartData(pieChartData);

    }

    private void initComponents(View root) {
        grafica2 = root.findViewById(R.id.grafica2);
        tv_listado = root.findViewById(R.id.tv_listado);
        inv_tv_nombre = root.findViewById(R.id.inv_tv_nombre);
        inv_tv_descripcion = root.findViewById(R.id.inv_tv_descripcion);
        inv_tv_stock = root.findViewById(R.id.inv_tv_stock);
        inv_tv_ocupado = root.findViewById(R.id.inv_tv_ocupado);
        inv_tv_disponible = root.findViewById(R.id.inv_tv_disponible);
        rbc_insumo = root.findViewById(R.id.rbc_insumo);
        rbc_producto = root.findViewById(R.id.rbc_producto);
        btn_scanProducto = root.findViewById(R.id.btn_scanProducto);
    }



}
