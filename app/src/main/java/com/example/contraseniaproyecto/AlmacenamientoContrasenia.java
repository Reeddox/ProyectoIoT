package com.example.contraseniaproyecto;

import java.util.ArrayList;
import java.util.List;

public class AlmacenamientoContrasenia {
    // Lista para almacenar los nombres y contraseñas
    public static List<AlmacenamientoContrasenia.Contrasenia> contrasenias = new ArrayList<>();

    // Clase para representar una contraseña
    public static class Contrasenia{
        public String NombreContrasenia;
        public String nContrasenia;

        public Contrasenia(String NombreContrasenia, String nContrasenia){
            this.NombreContrasenia = NombreContrasenia;
            this.nContrasenia = nContrasenia;
        }
    }
}
