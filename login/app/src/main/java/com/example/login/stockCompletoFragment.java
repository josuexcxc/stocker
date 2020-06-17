package com.example.login;


import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.login.Adaptadores.AdaptadorStock;
import com.example.login.Utilidades.Utilidades;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class stockCompletoFragment extends Fragment {
    private RecyclerView listaCompletaStock;
    private Button btn_back;
    private ArrayList<StockVo> listaStock;
    private AdaptadorStock adaptadorStock;
    public stockCompletoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_stock_completo, container, false);
        initComponents(root);
        final String consulta = getArguments().getString("consulta");
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        listaCompletaStock.setLayoutManager(new LinearLayoutManager(getContext()));
        listaStock = new ArrayList<StockVo>();
        adaptadorStock = new AdaptadorStock(listaStock);
        listaCompletaStock.setAdapter(adaptadorStock);

        getListado(consulta);

        return root;
    }

    private void back() {
        InventarioFragment2 inventarioFragment2 = new InventarioFragment2();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment,inventarioFragment2);
        transaction.commit();
    }

    private void getListado(String consulta) {
        switch (consulta){
            case "productos":
                getProductos();
                break;
            case "insumos":
                getInsumos();
                break;
        }
    }

    private void getInsumos() {
        String url = Utilidades.ip_server+"/stocker_web_services/listadoInsumos.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray = response.optJSONArray("json");
                JSONObject json = null;

                try {
                    for (int x=0 ; x<jsonArray.length() ; x++){
                        json = jsonArray.getJSONObject(x);
                        String nombre = json.optString("nombre");
                        String descripcion = json.optString("unidad")+" con "+json.optString("cantidad")+" piezas";
                        String stock = json.optString("stock");
                        StockVo objeto = new StockVo(nombre, descripcion, stock);
                        listaStock.add(objeto);
                        adaptadorStock.notifyItemInserted(listaStock.size()-1);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                final LoadDialogUnSuccess loadDialogUnSuccess = new LoadDialogUnSuccess(getContext(),"Algo falló\n Intenta más tarde");
                loadDialogUnSuccess.startLoad();
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                },2000);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonObjectRequest);
    }

    private void getProductos() {
        String url = Utilidades.ip_server +"/stocker_web_services/listadoProductos.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray = response.optJSONArray("json");
                JSONObject json = null;

                try {
                    for( int x =0 ; x<jsonArray.length() ; x++){
                        json = jsonArray.getJSONObject(x);
                        String nombre = json.optString("nombre");
                        String descripcion = json.optString("presentacion");
                        String stock = json.optString("stock");
                        StockVo objeto = new StockVo(nombre, descripcion, stock);
                        listaStock.add(objeto);
                        adaptadorStock.notifyItemInserted(listaStock.size()-1);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                final LoadDialogUnSuccess loadDialogUnSuccess = new LoadDialogUnSuccess(getContext(),"Algo falló\n Intenta más tarde");
                loadDialogUnSuccess.startLoad();
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                },2000);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonObjectRequest);
    }

    private void initComponents(View root) {
        listaCompletaStock = root.findViewById(R.id.listaCompletaStock);
        btn_back = root.findViewById(R.id.btn_back);
    }


}
