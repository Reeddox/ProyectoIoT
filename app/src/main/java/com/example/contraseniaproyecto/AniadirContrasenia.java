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

    private DrawerLayout drawerLayout; // Declaración del DrawerLayout
    private EditText nombre, contrasenia, confirmarContrasenia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_aniadir_contrasenia);

        // Manejo de los insets para una mejor experiencia de pantalla completa
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainGuardar), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar y configurar el menú lateral (Navigation Drawer)
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
                // Añade más condiciones según tus opciones

                drawerLayout.closeDrawers(); // Cierra el menú después de seleccionar
                return true;
            }
        });

        // Configurar el icono del menú para abrir el drawer
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

        // Encuentra la imagen del botón "+" y establece el listener para redirigir a AniadirContrasenia
        ImageView imageViewAdd = findViewById(R.id.imageView3); // Cambia el ID si es necesario
        imageViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirige a la actividad AniadirContrasenia
                Intent intentAniadirContrasenia = new Intent(AniadirContrasenia.this, VentanaPrincipal.class);
                startActivity(intentAniadirContrasenia);
            }
        });
        // Inicializar los campos
        nombre = findViewById(R.id.etNombre);
        contrasenia = findViewById(R.id.etContrasena);
        confirmarContrasenia = findViewById(R.id.etConfirmarContrasena);

        // Boton de guardar
        findViewById(R.id.btnGuardar).setOnClickListener(v -> {
            guardarContrasenia();
        });

    }
    private void guardarContrasenia(){
        String nombreText = nombre.getText().toString();
        String contraseniaText = contrasenia.getText().toString();
        String cContraseniaText = confirmarContrasenia.getText().toString();

        if (nombreText.isEmpty() || contraseniaText.isEmpty() || cContraseniaText.isEmpty()){
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!contraseniaText.equals(cContraseniaText)){
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        // Se agregan los datos
        AlmacenamientoContrasenia.contrasenias.add(new AlmacenamientoContrasenia.Contrasenia(nombreText, contraseniaText));

        Toast.makeText(this, "Contraseña Guardada", Toast.LENGTH_SHORT).show();
        finish();
    }
}