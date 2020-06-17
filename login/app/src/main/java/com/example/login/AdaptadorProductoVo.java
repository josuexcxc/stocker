package com.example.login;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.Objetos.ProductoVo;

import java.util.ArrayList;

public class AdaptadorProductoVo extends RecyclerView.Adapter<AdaptadorProductoVo.ViewHolderProducto> {
    ArrayList<ProductoVo> lista_productos;

    public AdaptadorProductoVo(ArrayList<ProductoVo> lista_productos) {
        this.lista_productos = lista_productos;
    }

    @NonNull
    @Override
    public ViewHolderProducto onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto,null,false);
        return new ViewHolderProducto(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderProducto holder, int position) {
        holder.tv1.setText(lista_productos.get(position).getNombre());
        holder.tv2.setText(lista_productos.get(position).getPresentacion());
        holder.tv4.setText(lista_productos.get(position).getCantidad());
    }

    @Override
    public int getItemCount() {
        return lista_productos.size();
    }

    public class ViewHolderProducto extends RecyclerView.ViewHolder {
        TextView tv1,tv2,tv4;
        public ViewHolderProducto(@NonNull View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(R.id.tv1);
            tv2 = itemView.findViewById(R.id.tv2);
            tv4 = itemView.findViewById(R.id.tv4);
        }
    }
}
