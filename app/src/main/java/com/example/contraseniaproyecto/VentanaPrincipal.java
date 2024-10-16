package com.example.contraseniaproyecto;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class VentanaPrincipal extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private final List<AlmacenamientoContrasenia.Contrasenia> contrasenias = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ventana_principal);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

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
                // Implementar lógica de filtrado aquí
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
        menu.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_item2) {
                Intent intentGenerador = new Intent(VentanaPrincipal.this, Generador.class);
                startActivity(intentGenerador);
            } else if (id == R.id.nav_item3) {
                Intent intentHistorial = new Intent(VentanaPrincipal.this, HIstorial.class);
                startActivity(intentHistorial);
            } else if (id == R.id.nav_item6) {
                mAuth.signOut();
                Intent intentLogout = new Intent(VentanaPrincipal.this, MainActivity.class);
                startActivity(intentLogout);
                finish();
            }

            drawerLayout.closeDrawers();
            return true;
        });

        ImageView imageViewMenu = findViewById(R.id.imageView2);
        imageViewMenu.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        ImageView imageViewAdd = findViewById(R.id.imageView3);
        imageViewAdd.setOnClickListener(v -> {
            Intent intentAniadirContrasenia = new Intent(VentanaPrincipal.this, AniadirContrasenia.class);
            startActivity(intentAniadirContrasenia);
        });

        cargarContrasenias();
    }

    private void cargarContrasenias() {
        String userId = mAuth.getCurrentUser().getUid();
        db.collection("ContraseñasUsuarios").document(userId).collection("ContraseñasGuardadas")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        contrasenias.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String id = document.getId();
                            String nombre = document.getString("nombre");
                            String contrasenia = document.getString("contrasenia");
                            contrasenias.add(new AlmacenamientoContrasenia.Contrasenia(id, nombre, contrasenia));
                        }
                        mostrarContrasenias();
                    } else {
                        Toast.makeText(VentanaPrincipal.this, "Error al cargar contraseñas", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void mostrarContrasenias() {
        LinearLayout layout = findViewById(R.id.contraseniaContainer);
        layout.removeAllViews();

        for (AlmacenamientoContrasenia.Contrasenia contrasenia : contrasenias) {
            View itemView = getLayoutInflater().inflate(R.layout.item_contrasenia, layout, false);

            TextView textViewNombre = itemView.findViewById(R.id.textViewNombreContrasenia);
            TextView textViewContrasenia = itemView.findViewById(R.id.textViewContrasenia);
            ImageView imageViewEye = itemView.findViewById(R.id.imageViewEye);

            textViewNombre.setText(contrasenia.NombreContrasenia);
            textViewContrasenia.setText("********");
            textViewContrasenia.setTransformationMethod(PasswordTransformationMethod.getInstance());

            // Inicializamos una variable para controlar la visibilidad de la contraseña
            final boolean[] isPasswordVisible = {false};

            imageViewEye.setOnClickListener(v -> {
                if (isPasswordVisible[0]) {
                    // Si la contraseña es visible, la ocultamos
                    textViewContrasenia.setText("********");
                    textViewContrasenia.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isPasswordVisible[0] = false;  // Actualizamos la bandera
                } else {
                    // Si la contraseña está oculta, pedimos el PIN
                    solicitarPIN(contrasenia, textViewContrasenia, isPasswordVisible);
                }
            });

            itemView.setOnLongClickListener(v -> {
                mostrarOpcionesContrasenia(contrasenia);
                return true;
            });

            layout.addView(itemView);
        }
    }

    private void solicitarPIN(AlmacenamientoContrasenia.Contrasenia contrasenia, TextView textViewContrasenia, boolean[] isPasswordVisible) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ingrese su PIN");

        final EditText input = new EditText(this);
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String pin = input.getText().toString();
            verificarPIN(pin, contrasenia, textViewContrasenia, isPasswordVisible);
        });
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void verificarPIN(String pin, AlmacenamientoContrasenia.Contrasenia contrasenia, TextView textViewContrasenia, boolean[] isPasswordVisible) {
        String userId = mAuth.getCurrentUser().getUid();
        db.collection("usuarios").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    String storedPIN = documentSnapshot.getString("pin");
                    if (pin.equals(storedPIN)) {
                        mostrarContrasenia(contrasenia, textViewContrasenia, isPasswordVisible);
                    } else {
                        Toast.makeText(VentanaPrincipal.this, "PIN incorrecto", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(VentanaPrincipal.this, "Error al verificar PIN", Toast.LENGTH_SHORT).show());
    }

    private void mostrarContrasenia(AlmacenamientoContrasenia.Contrasenia contrasenia, TextView textViewContrasenia, boolean[] isPasswordVisible) {
        textViewContrasenia.setText(contrasenia.nContrasenia);
        textViewContrasenia.setTransformationMethod(null);
        isPasswordVisible[0] = true;  // Actualizamos la bandera para indicar que la contraseña está visible
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

        HIstorial.registrarEvento(
                "Se modificó la contraseña de " + contrasenia.NombreContrasenia,
                "modificación"
        );

        Intent intentModificar = new Intent(VentanaPrincipal.this, AniadirContrasenia.class);
        intentModificar.putExtra("MODIFICAR", true);
        intentModificar.putExtra("ID", contrasenia.id);
        intentModificar.putExtra("NOMBRE", contrasenia.NombreContrasenia);
        intentModificar.putExtra("CONTRASENIA", contrasenia.nContrasenia);
        startActivity(intentModificar);
    }

    private void eliminarContrasenia(AlmacenamientoContrasenia.Contrasenia contrasenia) {
        String userId = mAuth.getCurrentUser().getUid();
        db.collection("ContraseñasUsuarios").document(userId).collection("ContraseñasGuardadas").document(contrasenia.id)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    contrasenias.remove(contrasenia);
                    mostrarContrasenias();
                    Toast.makeText(VentanaPrincipal.this, "Contraseña eliminada", Toast.LENGTH_SHORT).show();

                    HIstorial.registrarEvento(
                            "Se eliminó la contraseña de " + contrasenia.NombreContrasenia,
                            "eliminación"
                    );
                })
                .addOnFailureListener(e -> Toast.makeText(VentanaPrincipal.this, "Error al eliminar contraseña", Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarContrasenias();
    }
}