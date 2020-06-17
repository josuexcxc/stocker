package com.example.login.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.Insumo;
import com.example.login.Objetos.InsumoVo;
import com.example.login.R;

import java.util.ArrayList;

public class AdaptadorInsumo extends RecyclerView.Adapter<AdaptadorInsumo.ViewHolderInsumoVo> {
    ArrayList<InsumoVo> lista_insumos;

    public AdaptadorInsumo(ArrayList<InsumoVo> lista_insumos) {
        this.lista_insumos = lista_insumos;
    }

    @NonNull
    @Override
    public ViewHolderInsumoVo onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_insumo,null,false);
        return new ViewHolderInsumoVo(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderInsumoVo holder, int position) {
        holder.tv1.setText(lista_insumos.get(position).getNombre());
        holder.tv2.setText(lista_insumos.get(position).getUnida());
        holder.tv4.setText(lista_insumos.get(position).getCantidad_r());
        holder.tvcunidad.setText(lista_insumos.get(position).getCantidad());
    }

    @Override
    public int getItemCount() {
        return lista_insumos.size();
    }

    public class ViewHolderInsumoVo extends RecyclerView.ViewHolder {
        TextView tv1,tv2,tv4,tvcunidad;
        public ViewHolderInsumoVo(@NonNull View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(R.id.tv1);
            tv2 = itemView.findViewById(R.id.tv2);
            tv4 = itemView.findViewById(R.id.tv4);
            tvcunidad = itemView.findViewById(R.id.tvcunidad);

        }
    }

}

