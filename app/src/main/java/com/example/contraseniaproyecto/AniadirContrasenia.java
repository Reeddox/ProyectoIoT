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

    private DrawerLayout drawerLayout; //
    private EditText nombre, contrasenia, confirmarContrasenia; // Campos de entrada para el nombre, contraseña y confirmación de contraseña
    private boolean esModificacion = false; // Indica si se está modificando una contraseña existente
    private String contraseniaId; // ID de la contraseña a modificar
    private FirebaseAuth mAuth; // Instancia de Firebase Authentication
    private FirebaseFirestore db; // Instancia de Firebase Firestore
    private ImageView togglePassword, toggleConfirmPassword; // Íconos para mostrar/ocultar contraseña

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Llamada al método onCreate de la clase padre
        EdgeToEdge.enable(this); // Habilitar el modo de borde para la actividad
        setContentView(R.layout.activity_aniadir_contrasenia); // Establecer el diseño de la actividad

        mAuth = FirebaseAuth.getInstance(); // Inicializar la instancia de Firebase Authentication
        db = FirebaseFirestore.getInstance(); // Inicializar la instancia de Firebase Firestore

        drawerLayout = findViewById(R.id.mainGuardar); // Encontrar el DrawerLayout por su ID
        nombre = findViewById(R.id.etNombre); // Encontrar el EditText para el nombre por su ID
        contrasenia = findViewById(R.id.etContrasena); // Encontrar el EditText para la contraseña por su ID
        confirmarContrasenia = findViewById(R.id.etConfirmarContrasena); // Encontrar el EditText para la confirmación de contraseña por su ID
        togglePassword = findViewById(R.id.ivShowPassword); // Encontrar el ImageView para mostrar/ocultar contraseña por su ID
        toggleConfirmPassword = findViewById(R.id.ivShowPassword2); // Encontrar el ImageView para mostrar/ocultar confirmación de contraseña por su ID

        // Configurar los listeners para mostrar/ocultar contraseña
        togglePassword.setOnClickListener(v -> verContrasenia(contrasenia, togglePassword)); // Configurar el listener para el ImageView de contraseña
        toggleConfirmPassword.setOnClickListener(v -> verContrasenia(confirmarContrasenia, toggleConfirmPassword)); // Configurar el listener para el ImageView de confirmación de contraseña

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainGuardar), (v, insets) -> { // Configurar el listener para el DrawerLayout
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars()); // Obtener los márgenes del sistema
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom); // Establecer los márgenes del DrawerLayout
            return insets; // Devolver los insets
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

        esModificacion = getIntent().getBooleanExtra("MODIFICAR", false); // Verificar si se está modificando una contraseña existente
        if (esModificacion) { // Si se está modificando una contraseña existente
            contraseniaId = getIntent().getStringExtra("ID"); // Obtener el ID de la contraseña a modificar
            String nombreOriginal = getIntent().getStringExtra("NOMBRE"); // Obtener el nombre original de la contraseña
            String contraseniaOriginal = getIntent().getStringExtra("CONTRASENIA"); // Obtener la contraseña original de la contraseña
            nombre.setText(nombreOriginal); // Establecer el nombre original en el EditText
            contrasenia.setText(contraseniaOriginal); // Establecer la contraseña original en el EditText
            confirmarContrasenia.setText(contraseniaOriginal); // Establecer la contraseña original en el EditText de confirmación
        }

        findViewById(R.id.btnGuardar).setOnClickListener(v -> guardarContrasenia()); // Configurar el listener para el botón de guardar
    }

    private void verContrasenia(EditText editText, ImageView toggleIcon) { // Método para mostrar/ocultar contraseña
        if (editText.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) { // Si la contraseña está visible
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
        String nombreText = nombre.getText().toString(); // Obtener el texto del EditText para el nombre
        String contraseniaText = contrasenia.getText().toString(); // Obtener el texto del EditText para la contraseña
        String cContraseniaText = confirmarContrasenia.getText().toString(); // Obtener el texto del EditText para la confirmación de contraseña

        if (nombreText.isEmpty() || contraseniaText.isEmpty() || cContraseniaText.isEmpty()) { // Verificar si se ingresaron todos los campos
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show(); // Mostrar un mensaje de error
            return;
        }

        if (!contraseniaText.equals(cContraseniaText)) { // Verificar si las contraseñas coinciden
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show(); // Mostrar un mensaje de error
            return;
        }

        String userId = mAuth.getCurrentUser().getUid(); // Obtener el ID del usuario actual
        Map<String, Object> password = new HashMap<>();
        password.put("nombre", nombreText);
        password.put("contrasenia", contraseniaText);

        if (esModificacion) { // Si se está modificando una contraseña existente
            db.collection("ContraseñasUsuarios").document(userId).collection("ContraseñasGuardadas").document(contraseniaId)
                    .set(password)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(AniadirContrasenia.this, "Contraseña modificada", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(AniadirContrasenia.this, "Error al modificar contraseña", Toast.LENGTH_SHORT).show());
        } else {
            db.collection("ContraseñasUsuarios").document(userId).collection("ContraseñasGuardadas") // Guardar la contraseña en Firestore
                    .add(password) // Agregar la contraseña a la colección de contraseñas del usuario
                    .addOnSuccessListener(documentReference -> { // Mostrar un mensaje de éxito
                        Toast.makeText(AniadirContrasenia.this, "Contraseña guardada", Toast.LENGTH_SHORT).show(); // Mostrar un mensaje de éxito
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(AniadirContrasenia.this, "Error al guardar contraseña", Toast.LENGTH_SHORT).show()); // Mostrar un mensaje de error
        }
    }
}