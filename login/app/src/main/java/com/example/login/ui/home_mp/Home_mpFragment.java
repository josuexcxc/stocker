package com.example.login.ui.home_mp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.login.CalendarFragment;
import com.example.login.ControlInsumosFragment;
import com.example.login.ControlProductosFragment;
import com.example.login.InventarioFragment2;
import com.example.login.OrdenProduccionFragment;
import com.example.login.R;
import com.example.login.Utilidades.Utilidades;
import com.example.login.generarQRFragment;

public class Home_mpFragment extends Fragment {

    private Home_mpViewModel sendViewModel;
    private Button btn_qr,btn_orden_produccion,btn_ctlInsumos,btn_ctrlProductos,btn_Calendario,btn_Inventario;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sendViewModel =
                ViewModelProviders.of(this).get(Home_mpViewModel.class);
        View root = inflater.inflate(R.layout.fragment_homemp, container, false);

// ========================== init components=============================================

        btn_qr=root.findViewById(R.id.button4);
        btn_orden_produccion = root.findViewById(R.id.button_orden_produccion);
        btn_ctlInsumos = root.findViewById(R.id.btn_ctlInsumos);
        btn_ctrlProductos = root.findViewById(R.id.ctl_productos);
        btn_Calendario = root.findViewById(R.id.btn_calendar);
        btn_Inventario = root.findViewById(R.id.btn_inventario);
//========================================================================================
        if(Utilidades.tipo_usuario.equals("2")){
            btn_orden_produccion.setEnabled(false);
            btn_orden_produccion.setBackgroundResource(R.drawable.widgetdisable);

        }

        final ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Menú Principal");
        btn_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generarQRFragment fr1=new generarQRFragment();
                FragmentTransaction transaction =getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment,fr1);
                transaction.commit();
                actionBar.setTitle("Generar QR");
            }
        });

        btn_orden_produccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrdenProduccionFragment ordenProduccionFragment = new OrdenProduccionFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment,ordenProduccionFragment);
                transaction.commit();
                actionBar.setTitle("Orden de producción");
            }
        });
        btn_ctlInsumos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ControlInsumosFragment controlInsumosFragment = new ControlInsumosFragment();
                FragmentTransaction transaction= getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment,controlInsumosFragment);
                transaction.commit();
                actionBar.setTitle("Control de Insumos");
            }
        });

        btn_ctrlProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ControlProductosFragment controlProductosFragment = new ControlProductosFragment();
                FragmentTransaction transaction= getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment,controlProductosFragment);
                transaction.commit();
                actionBar.setTitle("Control de Productos");
            }
        });
        btn_Calendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarFragment calendarFragment = new CalendarFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment,calendarFragment);
                transaction.commit();
                actionBar.setTitle("Calendario");
            }
        });
        btn_Inventario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InventarioFragment2 inventarioFragment = new InventarioFragment2();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment,inventarioFragment);
                transaction.commit();
                actionBar.setTitle("Inventario");
            }
        });

        return root;
    }
}