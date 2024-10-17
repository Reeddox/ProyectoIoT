package com.example.contraseniaproyecto;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AniadirContrasenia extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private EditText nombre, contrasenia, confirmarContrasenia;
    private boolean esModificacion = false;
    private String contraseniaId;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ImageView togglePassword, toggleConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_aniadir_contrasenia);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        drawerLayout = findViewById(R.id.mainGuardar);
        nombre = findViewById(R.id.etNombre);
        contrasenia = findViewById(R.id.etContrasena);
        confirmarContrasenia = findViewById(R.id.etConfirmarContrasena);
        togglePassword = findViewById(R.id.ivShowPassword);
        toggleConfirmPassword = findViewById(R.id.ivShowPassword2);

        // Configurar los listeners para mostrar/ocultar contraseña
        togglePassword.setOnClickListener(v -> verContrasenia(contrasenia, togglePassword));
        toggleConfirmPassword.setOnClickListener(v -> verContrasenia(confirmarContrasenia, toggleConfirmPassword));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainGuardar), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        NavigationView menu = findViewById(R.id.menuGuardar);
        menu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_item1) {
                    Intent intentPrincipal = new Intent(AniadirContrasenia.this, VentanaPrincipal.class);
                    startActivity(intentPrincipal);
                } else if (id == R.id.nav_item2) {
                    Intent intentGenerador = new Intent(AniadirContrasenia.this, Generador.class);
                    startActivity(intentGenerador);
                } else if (id == R.id.nav_item6) {
                    mAuth.signOut();
                    Intent intentLogout = new Intent(AniadirContrasenia.this, MainActivity.class);
                    startActivity(intentLogout);
                    finish();
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });

        ImageView imageViewMenu = findViewById(R.id.imageView2);
        imageViewMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        ImageView imageViewAdd = findViewById(R.id.imageView3);
        imageViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAniadirContrasenia = new Intent(AniadirContrasenia.this, VentanaPrincipal.class);
                startActivity(intentAniadirContrasenia);
            }
        });

        // Verificar si se recibió una contraseña generada
        String contraseniaGenerada = getIntent().getStringExtra("CONTRASENIA_GENERADA");
        if (contraseniaGenerada != null && !contraseniaGenerada.isEmpty()) {
            contrasenia.setText(contraseniaGenerada);
            confirmarContrasenia.setText(contraseniaGenerada);
        }

        esModificacion = getIntent().getBooleanExtra("MODIFICAR", false);
        if (esModificacion) {
            contraseniaId = getIntent().getStringExtra("ID");
            String nombreOriginal = getIntent().getStringExtra("NOMBRE");
            String contraseniaOriginal = getIntent().getStringExtra("CONTRASENIA");
            nombre.setText(nombreOriginal);
            contrasenia.setText(contraseniaOriginal);
            confirmarContrasenia.setText(contraseniaOriginal);
        }

        findViewById(R.id.btnGuardar).setOnClickListener(v -> guardarContrasenia());
    }

    private void verContrasenia(EditText editText, ImageView toggleIcon) {
        if (editText.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            // Cambiar a modo oculto
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            toggleIcon.setImageResource(R.drawable.eye);
        } else {
            // Cambiar a modo visible
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            toggleIcon.setImageResource(R.drawable.eyeoff);
        }
        editText.setSelection(editText.getText().length());
    }

    private void guardarContrasenia() {
        String nombreText = nombre.getText().toString();
        String contraseniaText = contrasenia.getText().toString();
        String cContraseniaText = confirmarContrasenia.getText().toString();

        if (nombreText.isEmpty() || contraseniaText.isEmpty() || cContraseniaText.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!contraseniaText.equals(cContraseniaText)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = mAuth.getCurrentUser().getUid();
        Map<String, Object> password = new HashMap<>();
        password.put("nombre", nombreText);
        password.put("contrasenia", contraseniaText);

        if (esModificacion) {
            db.collection("ContraseñasUsuarios").document(userId).collection("ContraseñasGuardadas").document(contraseniaId)
                    .set(password)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(AniadirContrasenia.this, "Contraseña modificada", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(AniadirContrasenia.this, "Error al modificar contraseña", Toast.LENGTH_SHORT).show());
        } else {
            db.collection("ContraseñasUsuarios").document(userId).collection("ContraseñasGuardadas")
                    .add(password)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(AniadirContrasenia.this, "Contraseña guardada", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(AniadirContrasenia.this, "Error al guardar contraseña", Toast.LENGTH_SHORT).show());
        }
    }
}