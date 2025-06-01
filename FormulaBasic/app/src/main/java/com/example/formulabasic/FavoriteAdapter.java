package com.example.formulabasic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class FavoriteAdapter extends RecyclerView.Adapter<FavoriteViewHolder> {
    private ArrayList<FavoriteItem> datos;
    private View.OnClickListener onClickListener;

    public FavoriteAdapter(ArrayList<FavoriteItem> datos, View.OnClickListener onClickListener) {
        this.datos = datos;
        this.onClickListener = onClickListener;
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        view.setOnClickListener(onClickListener);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteViewHolder holder, int position) {
        FavoriteItem item = datos.get(position);
        holder.nombre.setText(item.itemName + " (" + item.itemType + ")");
        holder.itemView.setTag(item);
    }

    @Override
    public int getItemCount() { return datos.size(); }
}

class FavoriteViewHolder extends RecyclerView.ViewHolder {
    TextView nombre;
    public FavoriteViewHolder(View itemView) {
        super(itemView);
        nombre = itemView.findViewById(R.id.nombre);
    }
}