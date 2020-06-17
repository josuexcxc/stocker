package com.example.login.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.Evento;
import com.example.login.R;

import java.util.ArrayList;

public class AdaptadorEvento extends RecyclerView.Adapter<AdaptadorEvento.ViewHolderEvento> {
    ArrayList<Evento> listaEventos;

    public AdaptadorEvento(ArrayList<Evento> listaEventos) {
        this.listaEventos = listaEventos;
    }

    @NonNull
    @Override
    public AdaptadorEvento.ViewHolderEvento onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evento,null,false);
        return new AdaptadorEvento.ViewHolderEvento(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorEvento.ViewHolderEvento holder, int position) {
        holder.tv_title.setText(listaEventos.get(position).getTitulo());
        holder.tv_cliente.setText("Cliente: "+listaEventos.get(position).getCliente());
        holder.tv_fecha.setText(listaEventos.get(position).getFecha());

    }

    @Override
    public int getItemCount() {
        return listaEventos.size();
    }

    public class ViewHolderEvento extends RecyclerView.ViewHolder {
        TextView tv_title,tv_cliente,tv_fecha;
        public ViewHolderEvento(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_cliente = itemView.findViewById(R.id.tv_cliente);
            tv_fecha = itemView.findViewById(R.id.tv_fecha);
        }
    }
}
