package com.example.contraseniaproyecto;

import com.google.firebase.Timestamp;

public class HistorialItem {
    private String descripcion;
    private String tipo; // "modificacion" o "eliminacion"
    private Timestamp timestamp; // Fecha y hora

    public HistorialItem() {
        // Constructor vacío requerido para Firestore
    }

    public HistorialItem(String descripcion, String tipo, Timestamp timestamp) {
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.timestamp = timestamp;
    }

    // Getters y setters
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}