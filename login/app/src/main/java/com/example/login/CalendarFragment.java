package com.example.login;


import android.app.ActionBar;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.login.Adaptadores.AdaptadorEvento;
import com.example.login.Utilidades.Utilidades;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment {

    private RecyclerView rlista;
    private ArrayList<Evento> listaEventos;
    private JsonObjectRequest jsonObjectRequest;
    RequestQueue requestQueue;
    AdaptadorEvento adaptadorEvento;
    CalendarView calendarView;

    public CalendarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_calendar, container, false);
        initComponents(root);
        rlista.setLayoutManager(new LinearLayoutManager(getContext()));
        listaEventos = new ArrayList<Evento>();
        adaptadorEvento= new AdaptadorEvento(listaEventos);
        requestQueue= Volley.newRequestQueue(getContext());
        rlista.setAdapter(adaptadorEvento);
        getFechas();
        return root;
    }

    public void initComponents(View root){
        rlista = root.findViewById(R.id.lista_eventos);
        calendarView = root.findViewById(R.id.calendarView);
    }


    public void getFechas(){
        String url = Utilidades.ip_server+"/stocker_web_services/entregaOrdenProduccion.php";
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray = response.optJSONArray("json");

                try {
                    for(int x=0;x<jsonArray.length();x++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(x);
                        Utilidades.titulo = "Entrega de orden de producción";
                        String clave_orden = jsonObject1.optString("clave_orden");
                        String cliente = jsonObject1.optString("nombre")+" "+jsonObject1.optString("app");
                        String fecha=jsonObject1.optString("fecha_entrega");
                        Evento e = new Evento(clave_orden,Utilidades.titulo,fecha,cliente);
                        listaEventos.add(e);
                        adaptadorEvento.notifyItemInserted(listaEventos.size()-1);
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
        requestQueue.add(jsonObjectRequest);
    }

}
