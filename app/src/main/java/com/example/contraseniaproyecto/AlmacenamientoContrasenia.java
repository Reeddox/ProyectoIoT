package com.example.contraseniaproyecto;

import java.util.ArrayList;
import java.util.List;

public class AlmacenamientoContrasenia {
    // Clase para representar una contraseña
    public static class Contrasenia {
        public String id; // ID del documento en Firestore
        public String NombreContrasenia;
        public String nContrasenia;

        public Contrasenia() {
            // Constructor vacío necesario para Firestore
        }

        public Contrasenia(String id, String NombreContrasenia, String nContrasenia) {
            this.id = id;
            this.NombreContrasenia = NombreContrasenia;
            this.nContrasenia = nContrasenia;
        }
    }
}
