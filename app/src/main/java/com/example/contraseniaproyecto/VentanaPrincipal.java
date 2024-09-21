package com.example.contraseniaproyecto;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.view.GravityCompat;

import com.google.android.material.navigation.NavigationView;

public class VentanaPrincipal extends AppCompatActivity {

    private DrawerLayout drawerLayout; // Declara el DrawerLayout

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ventana_principal);

        // Inicializa el DrawerLayout
        drawerLayout = findViewById(R.id.main); // Asegúrate de que el ID sea correcto

        // Configurar el Spinner
        Spinner spinner = findViewById(R.id.spinner_filter);

        // Crear un ArrayAdapter usando el array de opciones y un layout predeterminado
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.filtro_opciones, android.R.layout.simple_spinner_item);

        // Especificar el layout a usar cuando la lista de opciones se muestra
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Aplicar el adaptador al spinner
        spinner.setAdapter(adapter);

        // Configurar el listener para manejar la selección
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                // Aquí puedes manejar la opción seleccionada
                // Por ejemplo: Toast.makeText(VentanaPrincipal.this, selectedItem, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada
            }
        });

        // Manejo de los insets para el sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /* Menu Lateral */
        NavigationView menu = findViewById(R.id.menu);
        menu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Recuperar la opción del menú
                // Recuperar la opción del menú
                int id = item.getItemId();
                if (id ==R.id.nav_item2){
                    Intent intentGenerador = new Intent(VentanaPrincipal.this, Generador.class);
                    startActivity(intentGenerador);
                } else if (id == R.id.nav_item6) {
                    // Acción para la opción 1
                    Intent intentLogout = new Intent(VentanaPrincipal.this, MainActivity.class);
                    startActivity(intentLogout);
                    finish();
                }

                // Añade más condiciones según tus opciones

                drawerLayout.closeDrawers(); // Cierra el menú después de seleccionar
                return true;
            }
        });

        // Encuentra la imagen y establece el listener para abrir el drawer
        ImageView imageViewMenu = findViewById(R.id.imageView2); // Asegúrate de usar el ID correcto
        imageViewMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abre el Drawer
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
                Intent intentAniadirContrasenia = new Intent(VentanaPrincipal.this, AniadirContrasenia.class);
                startActivity(intentAniadirContrasenia);
            }
        });

    }
}