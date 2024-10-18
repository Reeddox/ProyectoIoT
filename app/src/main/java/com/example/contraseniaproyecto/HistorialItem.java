package com.example.contraseniaproyecto;

import com.google.firebase.Timestamp;
import java.text.Normalizer;

public class HistorialItem {
    private String descripcion; // Descripción del evento
    private String tipo; // Tipo del evento
    private Timestamp timestamp; // Marca de tiempo del evento

    public static final String TIPO_MODIFICACION = "modificacion"; // Constantes para los tipos de eventos
    public static final String TIPO_ELIMINACION = "eliminacion"; // Constantes para los tipos de eventos

    public HistorialItem() {
        // Constructor vacío requerido para Firestore
    }

    public HistorialItem(String descripcion, String tipo, Timestamp timestamp) {
        this.descripcion = descripcion; // Inicializar los campos
        setTipo(tipo); // Configurar el tipo
        this.timestamp = timestamp; // Configurar el timestamp
    }

    // Métodos getter y setter para los campos
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
        if (tipo != null) {
            // Normalizar quitando tildes y convirtiendo a minúsculas
            String tipoNormalizado = Normalizer
                    .normalize(tipo.trim().toLowerCase(), Normalizer.Form.NFD)
                    .replaceAll("\\p{M}", "");

            // Verificar el tipo normalizado
            if (tipoNormalizado.equals("modificacion")) {
                this.tipo = TIPO_MODIFICACION;
            } else {
                this.tipo = TIPO_ELIMINACION;
            }
        } else {
            this.tipo = TIPO_ELIMINACION;
        }
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public boolean esModificacion() {
        return TIPO_MODIFICACION.equals(this.tipo);
    }
}