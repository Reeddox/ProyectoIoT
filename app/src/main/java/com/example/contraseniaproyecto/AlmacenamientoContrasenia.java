package com.example.contraseniaproyecto;

import java.util.ArrayList;
import java.util.List;

public class AlmacenamientoContrasenia {
    // Clase para representar una contraseña
    public static class Contrasenia {
        public String id; // ID del documento en Firestore
        public String NombreContrasenia; // Se almacena el nombre de la contraseña
        public String nContrasenia; // Se almacena la contraseña en sí

        public Contrasenia() {
            // Constructor vacío necesario para Firestore
        }

        public Contrasenia(String id, String NombreContrasenia, String nContrasenia) {
            this.id = id; // Se asigna el ID del documento
            this.NombreContrasenia = NombreContrasenia; // Se asigna el nombre de la contraseña
            this.nContrasenia = nContrasenia; // Se asigna la contraseña en sí
        }
    }
}
