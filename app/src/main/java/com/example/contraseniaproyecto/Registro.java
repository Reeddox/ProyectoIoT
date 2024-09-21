package com.example.contraseniaproyecto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Registro extends AppCompatActivity {

    private EditText editTextNombre, editTextApellido, editTextUsuario, editTextEmail,
            editTextContrasena, editTextConfirmarContrasena, editTextPIN;
    private CheckBox checkBoxTerminos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Referencia a todos los campos
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextApellido = findViewById(R.id.editTextApellido);
        editTextUsuario = findViewById(R.id.Usuario);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextContrasena = findViewById(R.id.editTextContrasena);
        editTextConfirmarContrasena = findViewById(R.id.editTextConfirmarContrasena);
        editTextPIN = findViewById(R.id.editTextPIN);
        checkBoxTerminos = findViewById(R.id.checkBoxTerminos);
    }

    // Método para validar los campos
    public void validarRegistro(View v) {
        // Validar si los campos están vacíos
        if (editTextNombre.getText().toString().trim().isEmpty() ||
                editTextApellido.getText().toString().trim().isEmpty() ||
                editTextUsuario.getText().toString().trim().isEmpty() ||
                editTextEmail.getText().toString().trim().isEmpty() ||
                editTextContrasena.getText().toString().trim().isEmpty() ||
                editTextConfirmarContrasena.getText().toString().trim().isEmpty() ||
                editTextPIN.getText().toString().trim().isEmpty()) {

            Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar que las contraseñas coincidan
        if (!editTextContrasena.getText().toString().equals(editTextConfirmarContrasena.getText().toString())) {
            Toast.makeText(this, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar si se han aceptado los términos
        if (!checkBoxTerminos.isChecked()) {
            Toast.makeText(this, "Debe aceptar los términos y condiciones.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Si todo está correcto, proceder con el registro
        Toast.makeText(this, "Registro exitoso.", Toast.LENGTH_SHORT).show();

        // Ejemplo de redireccionamiento a la pantalla de login tras el registro exitoso
        Intent VueltaLogin = new Intent(this, MainActivity.class);
        startActivity(VueltaLogin);
    }

    // Método para ir al login
    public void IrLogin(View v) {
        Intent VueltaLogin = new Intent(this, MainActivity.class);
        startActivity(VueltaLogin);
    }
}