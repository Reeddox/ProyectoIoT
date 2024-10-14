package com.example.contraseniaproyecto;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

public class AniadirContrasenia extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private EditText nombre, contrasenia, confirmarContrasenia;
    private boolean esModificacion = false;
    private String nombreOriginal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_aniadir_contrasenia);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainGuardar), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        drawerLayout = findViewById(R.id.mainGuardar);
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

        nombre = findViewById(R.id.etNombre);
        contrasenia = findViewById(R.id.etContrasena);
        confirmarContrasenia = findViewById(R.id.etConfirmarContrasena);

        // Verificar si se recibió una contraseña generada
        String contraseniaGenerada = getIntent().getStringExtra("CONTRASENIA_GENERADA");
        if (contraseniaGenerada != null && !contraseniaGenerada.isEmpty()) {
            contrasenia.setText(contraseniaGenerada);
            confirmarContrasenia.setText(contraseniaGenerada);
        }

        esModificacion = getIntent().getBooleanExtra("MODIFICAR", false);
        if (esModificacion) {
            nombreOriginal = getIntent().getStringExtra("NOMBRE");
            String contraseniaOriginal = getIntent().getStringExtra("CONTRASENIA");
            nombre.setText(nombreOriginal);
            contrasenia.setText(contraseniaOriginal);
            confirmarContrasenia.setText(contraseniaOriginal);
        }

        findViewById(R.id.btnGuardar).setOnClickListener(v -> guardarContrasenia());
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

        if (esModificacion) {
            for (AlmacenamientoContrasenia.Contrasenia c : AlmacenamientoContrasenia.contrasenias) {
                if (c.NombreContrasenia.equals(nombreOriginal)) {
                    c.NombreContrasenia = nombreText;
                    c.nContrasenia = contraseniaText;
                    break;
                }
            }
            setResult(RESULT_OK, new Intent().putExtra("ACCION", "Se modificó la contraseña de " + nombreOriginal));
        } else {
            AlmacenamientoContrasenia.contrasenias.add(new AlmacenamientoContrasenia.Contrasenia(nombreText, contraseniaText));
            setResult(RESULT_OK, new Intent().putExtra("ACCION", "Se añadió la contraseña de " + nombreText));
        }

        Toast.makeText(this, "Contraseña Guardada", Toast.LENGTH_SHORT).show();
        finish();
    }
}