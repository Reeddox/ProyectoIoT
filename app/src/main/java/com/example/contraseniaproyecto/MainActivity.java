package com.example.contraseniaproyecto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText editTextUsuario, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contrasenialogin);

        // Inicializar Firebase Auth y Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        editTextUsuario = findViewById(R.id.Usuario);
        editTextPassword = findViewById(R.id.Contrasenia);
    }
    // Método para iniciar sesión
    public void login(View v) {
        final String usuario = editTextUsuario.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        if (usuario.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Autenticar usando el nombre de usuario como email
        mAuth.signInWithEmailAndPassword(usuario + "@dominio.com", password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Login exitoso
                            Intent i = new Intent(MainActivity.this, VentanaPrincipal.class);
                            startActivity(i);
                            finish();
                        } else {
                            // Si falla el login
                            Toast.makeText(MainActivity.this, "Error en las credenciales", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    // Método para ir a la pantalla de registro
    public void IrRegistro(View v) {
        Intent VueltaRegistro = new Intent(this, Registro.class);
        startActivity(VueltaRegistro);
    }
}