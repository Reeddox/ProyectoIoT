package com.example.contraseniaproyecto;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

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
                    // Acción para la opción 6
                    Intent intentLogout = new Intent(VentanaPrincipal.this, MainActivity.class);
                    startActivity(intentLogout);
                    finish();
                }

                // Añadir más condiciones

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

        mostrarContrasenias();
    }
    private void mostrarContrasenias(){
        LinearLayout layout = findViewById(R.id.contraseniaContainer); // El container donde se muestran las contraseñas
        layout.removeAllViews(); // Limpia el layout para evitar duplicados

        // Recorre cada contraseña almacenada en la lista
        for (AlmacenamientoContrasenia.Contrasenia contrasenia : AlmacenamientoContrasenia.contrasenias){

            // Infla (crea) la vista del item_contrasenia desde el archivo XML, pero no la agrega todavia al layout
            View itemView = getLayoutInflater().inflate(R.layout.item_contrasenia, layout, false);

            // Encuentra los TextView y el ImageView en el layout inflado por sus ID
            TextView textViewNombre = itemView.findViewById(R.id.textViewNombreContrasenia);
            TextView textViewContrasenia = itemView.findViewById(R.id.textViewContrasenia);
            ImageView imageViewEye = itemView.findViewById(R.id.imageViewEye);

            // Establece el nombre de la contraseña en el TextView correspondiente
            textViewNombre.setText(contrasenia.NombreContrasenia);

            // Establece la contraseña en el TextView correspondiente
            textViewContrasenia.setText(contrasenia.nContrasenia);

            // Oculta la contraseña inicialmente usando un metodo de transformacion
            textViewContrasenia.setTransformationMethod(PasswordTransformationMethod.getInstance()); // Oculta la contraseña inicialmente

            // Inicializa un array de un solo elemento para controlar si la contraseña es visible o no
            final boolean[] passwordVisible = {false};

            // Configure un listener para el ImageView que representa el icono de mostrar/ocultar contraseña
            imageViewEye.setOnClickListener(v -> {

                // Cambia el estado de visibilidad de la contraseña
                passwordVisible[0] = !passwordVisible[0];

                // Si la contraseña es visible, se muestra la contraseña y cambia el icono a "eye"
                if (passwordVisible[0]) {
                    textViewContrasenia.setTransformationMethod(null); // Muestra la contraseña
                    imageViewEye.setImageResource(R.drawable.eye);
                } else {
                // Si la contraseña no es visible, se oculta la contraseña y cambia el icono a "eyeoff"
                    textViewContrasenia.setTransformationMethod(PasswordTransformationMethod.getInstance()); // Oculta la contraseña
                    imageViewEye.setImageResource(R.drawable.eyeoff);
                }
            });
            // Agrega el item view (la vista inflada con los datos de la contraseña) al layout
            layout.addView(itemView);

        }
    }
    @Override
    protected void onResume(){
        super.onResume();
        mostrarContrasenias();
    }
}