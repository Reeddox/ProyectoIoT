package com.example.contraseniaproyecto;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
    private EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ventana_principal);

        // Inicializar Firebase Auth y Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Configurar el DrawerLayout
        drawerLayout = findViewById(R.id.main);

        // Configurar el padding del DrawerLayout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupNavigationMenu();  // Configurar el menú de navegación
        setupImageViewListeners(); // Configurar los listeners de los ImageView
        setupSearchFunctionality(); // Configurar la funcionalidad de búsqueda
        cargarContrasenias(); // Cargar las contraseñas desde Firestore
    }

    // Método para configurar el menú de navegación
    private void setupNavigationMenu() {
        NavigationView menu = findViewById(R.id.menu);
        menu.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_item2) {
                startActivity(new Intent(VentanaPrincipal.this, Generador.class));
            } else if (id == R.id.nav_item3) {
                startActivity(new Intent(VentanaPrincipal.this, HIstorial.class));
            } else if (id == R.id.nav_item6) {
                mAuth.signOut();
                startActivity(new Intent(VentanaPrincipal.this, MainActivity.class));
                finish();
            }
            drawerLayout.closeDrawers();
            return true;
        });
    }

    // Método para configurar los listeners de los ImageView
    private void setupImageViewListeners() {
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
            startActivity(new Intent(VentanaPrincipal.this, AniadirContrasenia.class));
        });
    }

    // Método para configurar la funcionalidad de búsqueda
    private void setupSearchFunctionality() {
        searchEditText = findViewById(R.id.textView);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                filtrarContrasenias(s.toString());
            }
        });
    }

    // Método para cargar las contraseñas desde Firestore
    private void cargarContrasenias() {
        String userId = mAuth.getCurrentUser().getUid();
        db.collection("ContraseñasUsuarios").document(userId).collection("ContraseñasGuardadas") // Cambiar a la colección correcta
                .get() // Obtener todas las contraseñas del usuario
                .addOnCompleteListener(task -> { // Manejar la respuesta
                    if (task.isSuccessful()) { // Si la operación fue exitosa
                        contrasenias.clear(); // Limpiar la lista
                        for (QueryDocumentSnapshot document : task.getResult()) { // Recorrer los documentos
                            String id = document.getId(); // Obtener el ID del documento
                            String nombre = document.getString("nombre"); // Obtener el nombre de la contraseña
                            String contrasenia = document.getString("contrasenia"); // Obtener la contraseña
                            contrasenias.add(new AlmacenamientoContrasenia.Contrasenia(id, nombre, contrasenia)); // Agregar la contraseña a la lista
                        }
                        mostrarContrasenias(contrasenias); // Mostrar las contraseñas
                    } else {
                        Toast.makeText(VentanaPrincipal.this, "Error al cargar contraseñas", Toast.LENGTH_SHORT).show(); // Mostrar un mensaje de error
                    }
                });
    }

    // Método para filtrar las contraseñas según el texto de búsqueda
    private void filtrarContrasenias(String query) { // Recibe el texto de búsqueda
        List<AlmacenamientoContrasenia.Contrasenia> contraseniasFiltered = new ArrayList<>(); // Lista para almacenar las contraseñas filtradas
        for (AlmacenamientoContrasenia.Contrasenia contrasenia : contrasenias) { // Recorrer las contraseñas
            if (contrasenia.NombreContrasenia.toLowerCase().contains(query.toLowerCase())) { // Si el nombre de la contraseña contiene el texto de búsqueda
                contraseniasFiltered.add(contrasenia); // Agregar la contraseña a la lista filtrada
            }
        }
        mostrarContrasenias(contraseniasFiltered); // Mostrar las contraseñas filtradas
    }

    // Método para mostrar las contraseñas
    private void mostrarContrasenias(List<AlmacenamientoContrasenia.Contrasenia> contraseniasToShow) {
        LinearLayout layout = findViewById(R.id.contraseniaContainer);
        layout.removeAllViews();

        for (AlmacenamientoContrasenia.Contrasenia contrasenia : contraseniasToShow) { // Recorrer las contraseñas
            View itemView = getLayoutInflater().inflate(R.layout.item_contrasenia, layout, false); // Inflar la vista del item

            // Configurar los elementos de la vista
            TextView textViewNombre = itemView.findViewById(R.id.textViewNombreContrasenia); // Obtener el TextView del nombre de la contraseña
            TextView textViewContrasenia = itemView.findViewById(R.id.textViewContrasenia); // Obtener el TextView de la contraseña
            ImageView imageViewEye = itemView.findViewById(R.id.imageViewEye); // Obtener el ImageView de la contraseña

            // Configurar los datos
            textViewNombre.setText(contrasenia.NombreContrasenia); // Configurar el nombre de la contraseña
            textViewContrasenia.setText("********"); // Configurar la contraseña
            textViewContrasenia.setTransformationMethod(PasswordTransformationMethod.getInstance()); // Configurar la contraseña como oculta

            final boolean[] isPasswordVisible = {false}; // Estado de la contraseña

            imageViewEye.setOnClickListener(v -> { // Configurar el listener del ImageView de la contraseña
                if (isPasswordVisible[0]) { // Si la contraseña está visible
                    textViewContrasenia.setText("********"); // Configurar la contraseña como oculta
                    textViewContrasenia.setTransformationMethod(PasswordTransformationMethod.getInstance()); // Configurar la contraseña como oculta
                    isPasswordVisible[0] = false; // Cambiar el estado de la contraseña
                } else {
                    solicitarPIN(contrasenia, textViewContrasenia, isPasswordVisible); // Solicitar el PIN
                }
            });

            itemView.setOnLongClickListener(v -> { // Configurar el listener de la vista
                mostrarOpcionesContrasenia(contrasenia); // Mostrar las opciones de la contraseña
                return true; // Indicar que el evento ha sido manejado
            });

            layout.addView(itemView); // Agregar la vista al layout
        }
    }

    // Método para solicitar el PIN
    private void solicitarPIN(AlmacenamientoContrasenia.Contrasenia contrasenia, TextView textViewContrasenia, boolean[] isPasswordVisible) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this); // Construir el diálogo
        builder.setTitle("Ingrese su PIN"); // Configurar el título

        final EditText input = new EditText(this); // Crear el EditText para el PIN
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD); // Configurar el tipo de entrada
        builder.setView(input); // Configurar el EditText

        builder.setPositiveButton("OK", (dialog, which) -> { // Configurar el listener del botón OK
            String pin = input.getText().toString(); // Obtener el PIN ingresado
            verificarPIN(pin, contrasenia, textViewContrasenia, isPasswordVisible); // Verificar el PIN
        });
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel()); // Configurar el listener del botón Cancelar

        builder.show(); // Mostrar el diálogo
    }

    // Método para verificar el PIN
    private void verificarPIN(String pin, AlmacenamientoContrasenia.Contrasenia contrasenia, TextView textViewContrasenia, boolean[] isPasswordVisible) {
        String userId = mAuth.getCurrentUser().getUid(); // Obtener el ID del usuario
        db.collection("usuarios").document(userId).get() // Obtener los datos del usuario
                .addOnSuccessListener(documentSnapshot -> {
                    String storedPIN = documentSnapshot.getString("pin"); // Obtener el PIN almacenado
                    if (pin.equals(storedPIN)) { // Si el PIN es correcto
                        mostrarContrasenia(contrasenia, textViewContrasenia, isPasswordVisible); // Mostrar la contraseña
                    } else { // Si el PIN es incorrecto
                        Toast.makeText(VentanaPrincipal.this, "PIN incorrecto", Toast.LENGTH_SHORT).show(); // Mostrar un mensaje de error
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(VentanaPrincipal.this, "Error al verificar PIN", Toast.LENGTH_SHORT).show()); // Mostrar un mensaje de error
    }

    // Método para mostrar la contraseña
    private void mostrarContrasenia(AlmacenamientoContrasenia.Contrasenia contrasenia, TextView textViewContrasenia, boolean[] isPasswordVisible) {
        textViewContrasenia.setText(contrasenia.nContrasenia); // Configurar la contraseña
        textViewContrasenia.setTransformationMethod(null);  // Mostrar la contraseña
        isPasswordVisible[0] = true; // Cambiar el estado de la contraseña
    }

    // Método para mostrar las opciones de la contraseña
    private void mostrarOpcionesContrasenia(AlmacenamientoContrasenia.Contrasenia contrasenia) {
        new AlertDialog.Builder(this) // Construir el diálogo
                .setTitle("Opciones") // Configurar el título
                .setItems(new CharSequence[]{"Modificar", "Eliminar"}, (dialog, which) -> { // Configurar los items
                    if (which == 0) { // Si se seleccionó modificar
                        modificarContrasenia(contrasenia); // Modificar la contraseña
                    } else { // Si se seleccionó eliminar
                        eliminarContrasenia(contrasenia); // Eliminar la contraseña
                    }
                })
                .show(); // Mostrar el diálogo
    }

    // Método para modificar la contraseña
    private void modificarContrasenia(AlmacenamientoContrasenia.Contrasenia contrasenia) {// Recibe la contraseña a modificar
        HIstorial.registrarModificacion( // Registrar la modificación
                "Se modificó la contraseña de " + contrasenia.NombreContrasenia // Con el nombre de la contraseña
        );

        // Iniciar la actividad de modificación
        Intent intentModificar = new Intent(VentanaPrincipal.this, AniadirContrasenia.class); // Intent para la modificación
        intentModificar.putExtra("MODIFICAR", true); // Configurar el modo de modificación
        intentModificar.putExtra("ID", contrasenia.id); // Configurar el ID de la contraseña
        intentModificar.putExtra("NOMBRE", contrasenia.NombreContrasenia); //Configurar el nombre de la contraseña
        intentModificar.putExtra("CONTRASENIA", contrasenia.nContrasenia); // Configurar la contraseña
        startActivity(intentModificar); // Iniciar la actividad de modificación
    }

    // Método para eliminar la contraseña
    private void eliminarContrasenia(AlmacenamientoContrasenia.Contrasenia contrasenia) { // Recibe la contraseña a eliminar
        String userId = mAuth.getCurrentUser().getUid(); // Obtener el ID del usuario
        db.collection("ContraseñasUsuarios") // Cambiar a la colección correcta
                .document(userId) // Cambiar a la colección correcta
                .collection("ContraseñasGuardadas") // Cambiar a la colección correcta
                .document(contrasenia.id) // Cambiar a la colección correcta
                .delete() // Eliminar la contraseña
                .addOnSuccessListener(aVoid -> { // Manejar la respuesta
                    contrasenias.remove(contrasenia); // Eliminar la contraseña de la lista
                    mostrarContrasenias(contrasenias); // Mostrar las contraseñas
                    Toast.makeText(VentanaPrincipal.this, "Contraseña eliminada", Toast.LENGTH_SHORT).show(); // Mostrar un mensaje de éxito

                    HIstorial.registrarEliminacion( // Registrar la eliminación
                            "Se eliminó la contraseña de " + contrasenia.NombreContrasenia // Con el nombre de la contraseña
                    );
                })
                .addOnFailureListener(e -> // Manejar el error
                        Toast.makeText(VentanaPrincipal.this, "Error al eliminar contraseña", Toast.LENGTH_SHORT).show() //Mostrar un mensaje de error
                );
    }

    //Método para volver a cargar las contraseñas
    @Override
    protected void onResume() {
        super.onResume();
        cargarContrasenias();
    }
}