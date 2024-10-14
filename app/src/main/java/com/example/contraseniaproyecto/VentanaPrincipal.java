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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.view.GravityCompat;

import com.google.android.material.navigation.NavigationView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class VentanaPrincipal extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ventana_principal);

        drawerLayout = findViewById(R.id.main);

        Spinner spinner = findViewById(R.id.spinner_filter);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.filtro_opciones, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        NavigationView menu = findViewById(R.id.menu);
        menu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_item2) {
                    Intent intentGenerador = new Intent(VentanaPrincipal.this, Generador.class);
                    startActivity(intentGenerador);
                } else if (id == R.id.nav_item3) {
                    Intent intentHistorial = new Intent(VentanaPrincipal.this, Historial.class);
                    startActivity(intentHistorial);
                } else if (id == R.id.nav_item6) {
                    Intent intentLogout = new Intent(VentanaPrincipal.this, MainActivity.class);
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
                Intent intentAniadirContrasenia = new Intent(VentanaPrincipal.this, AniadirContrasenia.class);
                startActivity(intentAniadirContrasenia);
            }
        });

        mostrarContrasenias();
    }

    private void mostrarContrasenias() {
        LinearLayout layout = findViewById(R.id.contraseniaContainer);
        layout.removeAllViews();

        for (AlmacenamientoContrasenia.Contrasenia contrasenia : AlmacenamientoContrasenia.contrasenias) {
            View itemView = getLayoutInflater().inflate(R.layout.item_contrasenia, layout, false);

            TextView textViewNombre = itemView.findViewById(R.id.textViewNombreContrasenia);
            TextView textViewContrasenia = itemView.findViewById(R.id.textViewContrasenia);
            ImageView imageViewEye = itemView.findViewById(R.id.imageViewEye);

            textViewNombre.setText(contrasenia.NombreContrasenia);
            textViewContrasenia.setText(contrasenia.nContrasenia);
            textViewContrasenia.setTransformationMethod(PasswordTransformationMethod.getInstance());

            final boolean[] passwordVisible = {false};

            imageViewEye.setOnClickListener(v -> {
                passwordVisible[0] = !passwordVisible[0];
                if (passwordVisible[0]) {
                    textViewContrasenia.setTransformationMethod(null);
                    imageViewEye.setImageResource(R.drawable.eye);
                } else {
                    textViewContrasenia.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imageViewEye.setImageResource(R.drawable.eyeoff);
                }
            });

            itemView.setOnLongClickListener(v -> {
                mostrarOpcionesContrasenia(contrasenia);
                return true;
            });

            layout.addView(itemView);
        }
    }

    private void mostrarOpcionesContrasenia(AlmacenamientoContrasenia.Contrasenia contrasenia) {
        new AlertDialog.Builder(this)
                .setTitle("Opciones")
                .setItems(new CharSequence[]{"Modificar", "Eliminar"}, (dialog, which) -> {
                    if (which == 0) {
                        modificarContrasenia(contrasenia);
                    } else {
                        eliminarContrasenia(contrasenia);
                    }
                })
                .show();
    }

    private void modificarContrasenia(AlmacenamientoContrasenia.Contrasenia contrasenia) {
        Intent intentModificar = new Intent(VentanaPrincipal.this, AniadirContrasenia.class);
        intentModificar.putExtra("MODIFICAR", true);
        intentModificar.putExtra("NOMBRE", contrasenia.NombreContrasenia);
        intentModificar.putExtra("CONTRASENIA", contrasenia.nContrasenia);
        startActivity(intentModificar);
    }

    private void eliminarContrasenia(AlmacenamientoContrasenia.Contrasenia contrasenia) {
        AlmacenamientoContrasenia.contrasenias.remove(contrasenia);
        mostrarContrasenias();

        // Agregar evento al historial sin redirigir
        String accion = "Se eliminó la contraseña de " + contrasenia.NombreContrasenia;
        agregarEventoHistorialLocal(accion);
    }

    private void agregarEventoHistorialLocal(String accion) {
        String fecha = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(new Date());
        String evento = fecha + "\n" + accion;

        // Guardar el evento en el archivo local
        try {
            FileOutputStream fos = openFileOutput("historial.txt", MODE_APPEND);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            osw.write(evento + "\n");
            osw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mostrarContrasenias();
    }
}