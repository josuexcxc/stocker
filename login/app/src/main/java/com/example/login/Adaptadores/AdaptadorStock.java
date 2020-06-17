package com.example.login.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;
import com.example.login.StockVo;

import java.util.ArrayList;

public class AdaptadorStock extends RecyclerView.Adapter<AdaptadorStock.ViewHolderStockVo> {
    ArrayList<StockVo> listaStock;

    public AdaptadorStock(ArrayList<StockVo> listastock) {
        this.listaStock = listastock;
    }

    @NonNull
    @Override
    public ViewHolderStockVo onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stock,null,false);
        return new ViewHolderStockVo(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderStockVo holder, int position) {
        holder.tv_nombre.setText(listaStock.get(position).getNombre());
        holder.tv_descripcion.setText(listaStock.get(position).getDescripcion());
        holder.tv_stock.setText(listaStock.get(position).getStock());

    }

    @Override
    public int getItemCount() {
        return listaStock.size();
    }

    public class ViewHolderStockVo extends RecyclerView.ViewHolder {
        TextView tv_nombre,tv_descripcion,tv_stock;
        public ViewHolderStockVo(@NonNull View itemView) {
            super(itemView);
            tv_nombre = itemView.findViewById(R.id.li_tv_nombre);
            tv_descripcion = itemView.findViewById(R.id.li_tv_descripcion);
            tv_stock= itemView.findViewById(R.id.li_tv_stock);
        }
    }
}
