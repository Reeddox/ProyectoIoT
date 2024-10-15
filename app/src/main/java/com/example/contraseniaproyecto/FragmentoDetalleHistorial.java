package com.example.contraseniaproyecto;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class FragmentoDetalleHistorial extends Fragment {

    private static final String ARG_NOMBRE_CONTRASENA = "nombre_contrasena";
    private static final String ARG_ACCION = "accion";
    private static final String ARG_MARCA_DE_TIEMPO = "marca_de_tiempo";

    public static FragmentoDetalleHistorial newInstance(ElementoHistorial item) {
        FragmentoDetalleHistorial fragmento = new FragmentoDetalleHistorial();
        Bundle args = new Bundle();
        args.putString(ARG_NOMBRE_CONTRASENA, item.getNombreContrasena());
        args.putString(ARG_ACCION, item.getAccion());
        args.putString(ARG_MARCA_DE_TIEMPO, item.getMarcaDeTiempo());
        fragmento.setArguments(args);
        return fragmento;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_detalle_historial, container, false);

        TextView tvNombreContrasena = view.findViewById(R.id.tvDetalleNombreContrasena);
        TextView tvAccion = view.findViewById(R.id.tvDetalleAccion);
        TextView tvMarcaDeTiempo = view.findViewById(R.id.tvDetalleMarcaDeTiempo);

        if (getArguments() != null) {
            tvNombreContrasena.setText(getArguments().getString(ARG_NOMBRE_CONTRASENA));
            tvAccion.setText(getArguments().getString(ARG_ACCION));
            tvMarcaDeTiempo.setText(getArguments().getString(ARG_MARCA_DE_TIEMPO));
        }

        return view;
    }
}
