package com.example.formulabasic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class CircuitAdapter extends RecyclerView.Adapter<CircuitViewHolder> {
    private ArrayList<Circuit> datos;
    private View.OnClickListener onClickListener;

    public CircuitAdapter(ArrayList<Circuit> datos, View.OnClickListener onClickListener) {
        this.datos = datos;
        this.onClickListener = onClickListener;
    }

    @Override
    public CircuitViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        view.setOnClickListener(onClickListener);
        return new CircuitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CircuitViewHolder holder, int position) {
        Circuit circuit = datos.get(position);
        holder.nombre.setText(circuit.circuitName);
        holder.itemView.setTag(circuit.circuitId);
    }

    @Override
    public int getItemCount() { return datos.size(); }
}

class CircuitViewHolder extends RecyclerView.ViewHolder {
    TextView nombre;
    public CircuitViewHolder(View itemView) {
        super(itemView);
        nombre = itemView.findViewById(R.id.nombre);
    }
}
