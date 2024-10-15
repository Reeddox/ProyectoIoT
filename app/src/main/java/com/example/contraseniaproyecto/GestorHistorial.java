package com.example.contraseniaproyecto;

import android.content.Context;
import android.content.SharedPreferences;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GestorHistorial {
    private static final String NOMBRE_PREF = "PreferenciaHistorialContrasenas";
    private static final String CLAVE_HISTORIAL = "HistorialContrasenas";
    private SharedPreferences preferenciasCompartidas;

    public GestorHistorial(Context contexto) {
        preferenciasCompartidas = contexto.getSharedPreferences(NOMBRE_PREF, Context.MODE_PRIVATE);
    }

    public void registrarAccionContrasena(String nombreContrasena, String accion) {
        List<String> historial = obtenerHistorialComoLista();
        String marcaDeTiempo = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(new Date());
        String nuevoElemento = nombreContrasena + "|" + accion + "|" + marcaDeTiempo;
        historial.add(0, nuevoElemento); // Agregar al inicio de la lista
        guardarHistorial(historial);
    }

    public List<ElementoHistorial> obtenerHistorial() {
        List<String> historialComoLista = obtenerHistorialComoLista();
        List<ElementoHistorial> historial = new ArrayList<>();
        for (String elemento : historialComoLista) {
            String[] partes = elemento.split("\\|");
            if (partes.length == 3) {
                historial.add(new ElementoHistorial(partes[0], partes[1], partes[2]));
            }
        }
        return historial;
    }

    private List<String> obtenerHistorialComoLista() {
        String historialComoString = preferenciasCompartidas.getString(CLAVE_HISTORIAL, "");
        String[] elementos = historialComoString.split("\\n");
        List<String> historial = new ArrayList<>();
        for (String elemento : elementos) {
            if (!elemento.isEmpty()) {
                historial.add(elemento);
            }
        }
        return historial;
    }

    private void guardarHistorial(List<String> historial) {
        StringBuilder sb = new StringBuilder();
        for (String elemento : historial) {
            sb.append(elemento).append("\n");
        }
        preferenciasCompartidas.edit().putString(CLAVE_HISTORIAL, sb.toString()).apply();
    }
}
