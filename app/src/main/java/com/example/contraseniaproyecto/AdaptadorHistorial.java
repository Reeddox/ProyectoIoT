package com.example.contraseniaproyecto;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AdaptadorHistorial extends RecyclerView.Adapter<AdaptadorHistorial.ViewHolder> {

    private List<ElementoHistorial> elementosHistorial;
    private OnItemClickListener oyente;

    public AdaptadorHistorial(List<ElementoHistorial> elementosHistorial) {
        this.elementosHistorial = elementosHistorial;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_historial, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ElementoHistorial item = elementosHistorial.get(position);
        holder.tvNombreContrasena.setText(item.getNombreContrasena());
        holder.tvAccion.setText(item.getAccion());
        holder.tvMarcaDeTiempo.setText(item.getMarcaDeTiempo());
    }

    @Override
    public int getItemCount() {
        return elementosHistorial.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreContrasena, tvAccion, tvMarcaDeTiempo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreContrasena = itemView.findViewById(R.id.tvNombreContrasena);
            tvAccion = itemView.findViewById(R.id.tvAccion);
            tvMarcaDeTiempo = itemView.findViewById(R.id.tvMarcaDeTiempo);

            itemView.setOnClickListener(v -> {
                if (oyente != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    oyente.onItemClick(elementosHistorial.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ElementoHistorial item);
    }

    public void setOnItemClickListener(OnItemClickListener oyente) {
        this.oyente = oyente;
    }
}
