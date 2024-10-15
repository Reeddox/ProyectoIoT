package com.example.contraseniaproyecto;

public class ElementoHistorial {
    private String nombreContrasena;
    private String accion;
    private String marca_de_tiempo;

    public ElementoHistorial(String nombreContrasena, String accion, String marca_de_tiempo) {
        this.nombreContrasena = nombreContrasena;
        this.accion = accion;
        this.marca_de_tiempo = marca_de_tiempo;
    }

    // Getters
    public String getNombreContrasena() { return nombreContrasena; }
    public String getAccion() { return accion; }
    public String getMarcaDeTiempo() { return marca_de_tiempo; }
}