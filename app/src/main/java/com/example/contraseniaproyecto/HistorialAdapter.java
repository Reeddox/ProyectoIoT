package com.example.contraseniaproyecto;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class HistorialAdapter extends RecyclerView.Adapter<HistorialAdapter.HistorialViewHolder> {
    private List<HistorialItem> historialList;
    private final Context context;

    public HistorialAdapter(Context context) {
        this.context = context;
        this.historialList = new ArrayList<>();
    }

    @NonNull
    @Override
    public HistorialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_historial, parent, false);
        return new HistorialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistorialViewHolder holder, int position) {
        HistorialItem item = historialList.get(position);

        if (item.getTimestamp() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            String fechaFormateada = sdf.format(item.getTimestamp().toDate());
            holder.tvFecha.setText(fechaFormateada);
        } else {
            holder.tvFecha.setText("Sin fecha");
        }

        holder.tvDescripcion.setText(item.getDescripcion());
        holder.itemView.setOnClickListener(v -> mostrarDetalles(item));
    }

    private void mostrarDetalles(HistorialItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Detalles del registro");

        // Corregido: Comparación directa con el string "modificacion"
        String tipoAccion = "modificacion".equals(item.getTipo()) ? "Modificación" : "Eliminación";

        String mensaje = String.format("Acción: %s\n\nDetalle: %s",
                tipoAccion,
                item.getDescripcion());

        builder.setMessage(mensaje);
        builder.setPositiveButton("Cerrar", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return historialList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void actualizarLista(List<HistorialItem> nuevaLista) {
        historialList = nuevaLista;
        notifyDataSetChanged();
    }

    static class HistorialViewHolder extends RecyclerView.ViewHolder {
        TextView tvFecha, tvDescripcion;

        public HistorialViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
        }
    }
}