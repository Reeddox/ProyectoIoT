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

        // Formatear el campo `timestamp` a una fecha legible
        if (item.getTimestamp() != null) {
            // Usa SimpleDateFormat para formatear el Timestamp a una fecha y hora legible
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            String fechaFormateada = sdf.format(item.getTimestamp().toDate());
            holder.tvFecha.setText(fechaFormateada);
        } else {
            holder.tvFecha.setText("Sin fecha"); // En caso de que no haya timestamp, para evitar crashes
        }

        holder.tvDescripcion.setText(item.getDescripcion());

        // Configurar el click listener para mostrar detalles en un diálogo
        holder.itemView.setOnClickListener(v -> mostrarDetalles(item));
    }

    private void mostrarDetalles(HistorialItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Detalles del registro");

        // Crear el mensaje con los detalles
        String mensaje = String.format("Acción: %s\n\nDetalle: %s",
                item.getTipo().equals("modificacion") ? "Modificación" : "Eliminación",
                item.getDescripcion());

        builder.setMessage(mensaje);
        builder.setPositiveButton("Cerrar", (dialog, which) -> dialog.dismiss());

        // Mostrar el diálogo
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
