package com.example.contraseniaproyecto;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class HistorialAdapter extends RecyclerView.Adapter<HistorialAdapter.ViewHolder> {
    private List<String> eventos;

    public HistorialAdapter(List<String> eventos) {
        this.eventos = eventos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_historial, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String evento = eventos.get(position);
        String[] partes = evento.split("\n", 2);
        if (partes.length == 2) {
            holder.tvFecha.setText(partes[0]);
            holder.tvAccion.setText(partes[1]);
        } else {
            holder.tvFecha.setText("");
            holder.tvAccion.setText(evento);
        }
    }

    @Override
    public int getItemCount() {
        return eventos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFecha;
        TextView tvAccion;

        public ViewHolder(View itemView) {
            super(itemView);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvAccion = itemView.findViewById(R.id.tvAccion);
        }
    }
}