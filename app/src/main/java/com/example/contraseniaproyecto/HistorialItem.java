package com.example.contraseniaproyecto;

import com.google.firebase.Timestamp;
import java.text.Normalizer;

public class HistorialItem {
    private String descripcion;
    private String tipo;
    private Timestamp timestamp;

    public static final String TIPO_MODIFICACION = "modificacion";
    public static final String TIPO_ELIMINACION = "eliminacion";

    public HistorialItem() {
        // Constructor vacío requerido para Firestore
    }

    public HistorialItem(String descripcion, String tipo, Timestamp timestamp) {
        this.descripcion = descripcion;
        setTipo(tipo);
        this.timestamp = timestamp;
    }

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