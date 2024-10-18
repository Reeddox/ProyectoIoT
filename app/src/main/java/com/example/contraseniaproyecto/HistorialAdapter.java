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
    private List<HistorialItem> historialList; // Lista de items del historial
    private final Context context; // Contexto de la actividad

    public HistorialAdapter(Context context) { // Constructor
        this.context = context; // Guardar el contexto
        this.historialList = new ArrayList<>(); // Inicializar la lista
    }

    @NonNull    // Método para crear un nuevo ViewHolder
    @Override // Método para crear un nuevo ViewHolder
    public HistorialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { // Método para crear un nuevo ViewHolder
        View view = LayoutInflater.from(context).inflate(R.layout.item_historial, parent, false); // Inflar el layout del item
        return new HistorialViewHolder(view); // Crear y devolver un nuevo ViewHolder
    }

    @Override
    public void onBindViewHolder(@NonNull HistorialViewHolder holder, int position) { // Método para vincular los datos a un ViewHolder
        HistorialItem item = historialList.get(position); // Obtener el item en la posición actual

        if (item.getTimestamp() != null) { // Verificar si hay una fecha
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()); // Formatear la fecha
            String fechaFormateada = sdf.format(item.getTimestamp().toDate()); // Formatear la fecha
            holder.tvFecha.setText(fechaFormateada); // Mostrar la fecha
        } else {
            holder.tvFecha.setText("Sin fecha"); // Mostrar un mensaje si no hay fecha
        }

        holder.tvDescripcion.setText(item.getDescripcion()); // Mostrar la descripción
        holder.itemView.setOnClickListener(v -> mostrarDetalles(item)); // Configurar el listener para mostrar detalles
    }

    private void mostrarDetalles(HistorialItem item) { // Método para mostrar detalles del item
        AlertDialog.Builder builder = new AlertDialog.Builder(context); // Crear un diálogo de alerta
        builder.setTitle("Detalles del registro"); // Establece el título

        // Comparación directa con el string "modificacion"
        String tipoAccion = "modificacion".equals(item.getTipo()) ? "Modificación" : "Eliminación";

        String mensaje = String.format("Acción: %s\n\nDetalle: %s", // Formatear el mensaje
                tipoAccion, // Mostrar el tipo de acción
                item.getDescripcion()); // Mostrar la descripción

        builder.setMessage(mensaje); // Mostrar el mensaje
        builder.setPositiveButton("Cerrar", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create(); // Crear el diálogo
        dialog.show(); // Mostrar el diálogo
    }

    @Override
    public int getItemCount() { // Método para obtener el número de items
        return historialList.size(); // Devolver el tamaño de la lista
    }

    @SuppressLint("NotifyDataSetChanged") // Anotación para ignorar el aviso de cambio de datos
    public void actualizarLista(List<HistorialItem> nuevaLista) { // Método para actualizar la lista
        historialList = nuevaLista; // Actualizar la lista
        notifyDataSetChanged(); // Notificar al adaptador que los datos han cambiado
    }

    static class HistorialViewHolder extends RecyclerView.ViewHolder { // Clase para el ViewHolder
        TextView tvFecha, tvDescripcion; // Vistas para mostrar la fecha y la descripción

        public HistorialViewHolder(@NonNull View itemView) { // Constructor
            super(itemView); // Llamar al constructor de la clase padre
            tvFecha = itemView.findViewById(R.id.tvFecha); // Encontrar las vistas
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion); // Encontrar las vistas
        }
    }
}