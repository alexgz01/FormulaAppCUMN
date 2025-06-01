package com.example.formulabasic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class DriverAdapter extends RecyclerView.Adapter<DriverViewHolder> {
    private ArrayList<Driver> datos;
    private View.OnClickListener onClickListener;

    public DriverAdapter(ArrayList<Driver> datos, View.OnClickListener onClickListener) {
        this.datos = datos;
        this.onClickListener = onClickListener;
    }

    @Override
    public DriverViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        view.setOnClickListener(onClickListener);
        return new DriverViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DriverViewHolder holder, int position) {
        Driver driver = datos.get(position);
        holder.nombre.setText(driver.givenName + " " + driver.familyName);
        holder.itemView.setTag(driver.driverId);
    }

    @Override
    public int getItemCount() { return datos.size(); }
}

class DriverViewHolder extends RecyclerView.ViewHolder {
    TextView nombre;
    public DriverViewHolder(View itemView) {
        super(itemView);
        nombre = itemView.findViewById(R.id.nombre);
    }
}
