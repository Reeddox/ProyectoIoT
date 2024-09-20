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

    public void login(View v){

        EditText Campo1 = this.findViewById(R.id.Usuario);
        String Usuario = Campo1.getText().toString();

        EditText Campo2 = this.findViewById(R.id.Contrasenia);
        String Contrasenia = Campo2.getText().toString();

        if (Usuario.equals("c1") && Contrasenia.equals("123")){
            Intent i = new Intent(this, VentanaPrincipal.class);
            startActivity(i);
        }else{
            Toast.makeText(this, "Error en las credenciales", Toast.LENGTH_SHORT).show();
        }
    }

    public void IrRegistro(View v){
        Intent VueltaRegistro = new Intent(this, Registro.class);
        startActivity(VueltaRegistro);
    }
}