package com.example.contraseniaproyecto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HistorialItem {
    private String fecha;
    private String accion;

    public HistorialItem(String accion) {
        this.fecha = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(new Date());
        this.accion = accion;
    }

    public String getFecha() {
        return fecha;
    }

    public String getAccion() {
        return accion;
    }
}
