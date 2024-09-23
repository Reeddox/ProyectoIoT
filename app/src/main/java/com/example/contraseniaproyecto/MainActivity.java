package com.example.contraseniaproyecto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contrasenialogin); // Referencia al archivo XML
    }

    public void login(View v){ // Metodo que se llama cuando el usuario interactua con el boton de inicio de sesion

        // Se obtiene el valor del EditText donde el usuario ingresa su nombre de usuario
        EditText Campo1 = this.findViewById(R.id.Usuario);
        String Usuario = Campo1.getText().toString();

        // Se obtiene el valor del EditText donde el usuario ingresa su contraseña
        EditText Campo2 = this.findViewById(R.id.Contrasenia);
        String Contrasenia = Campo2.getText().toString();

        // Verifica si las credenciales ingresadas son correctas
        if (Usuario.equals("admin") && Contrasenia.equals("admin")){

            // Si las credenciales son correctas, crea un intent para abrir 'VentanaPrincipal'
            Intent i = new Intent(this, VentanaPrincipal.class);

            // Inicia la nueva actividad 'VentanaPrincipal'
            startActivity(i);
        }else{

            // Si las credenciales son incorrectas, muesta un mensaje emergente (Toast) de error
            Toast.makeText(this, "Error en las credenciales", Toast.LENGTH_SHORT).show();
        }
    }

    // Metodo que se llama cuando el usuario hace clic en el boton para ir a la pantalla de registro
    public void IrRegistro(View v){

        // Crea un intent para abrir la actividad 'Registro'
        Intent VueltaRegistro = new Intent(this, Registro.class);
        startActivity(VueltaRegistro);  // Inicia la actividad 'Registro'
    }
}