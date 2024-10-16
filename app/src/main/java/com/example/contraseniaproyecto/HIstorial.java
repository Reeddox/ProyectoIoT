package com.example.contraseniaproyecto;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class HIstorial extends AppCompatActivity {
    private static final String TAG = "Historial";
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FirebaseAuth mAuth;

    // Constantes para los tipos de eventos
    public static final String TIPO_MODIFICACION = "modificacion";
    public static final String TIPO_ELIMINACION = "eliminacion";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        // Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Inicializar views
        drawerLayout = findViewById(R.id.main);
        navigationView = findViewById(R.id.historialNav);

        // Configurar navigation drawer
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_item1) {
                Intent intentPrincipal = new Intent(HIstorial.this, VentanaPrincipal.class);
                startActivity(intentPrincipal);
            } else if (id == R.id.nav_item2) {
                Intent intentGenerador = new Intent(HIstorial.this, Generador.class);
                startActivity(intentGenerador);
            } else if (id == R.id.nav_item3) {
                // Ya estamos en Historial, no necesitamos hacer nada
                drawerLayout.closeDrawers();
                return true;
            } else if (id == R.id.nav_item6) {
                mAuth.signOut();
                Intent intentLogout = new Intent(HIstorial.this, MainActivity.class);
                startActivity(intentLogout);
                finish();
            }

            drawerLayout.closeDrawers();
            return true;
        });

        // Configurar el botón del menú
        ImageView imageViewMenu = findViewById(R.id.imageView2);
        imageViewMenu.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        // Cargar el fragment del historial
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HistorialFragmento())
                    .commit();
        }
    }

    // Método principal para registrar eventos
    public static void registrarEvento(String descripcion, String tipo) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Normalizar el tipo
        String tipoNormalizado;
        if (tipo != null) {
            tipo = tipo.trim().toLowerCase();
            if (tipo.equals(TIPO_MODIFICACION) ||
                    tipo.equals("modificación")) {
                tipoNormalizado = TIPO_MODIFICACION;
            } else {
                tipoNormalizado = TIPO_ELIMINACION;
            }
        } else {
            tipoNormalizado = TIPO_ELIMINACION;
        }

        // Logs para depuración
        Log.d(TAG, "Registrando evento:");
        Log.d(TAG, "Descripción: " + descripcion);
        Log.d(TAG, "Tipo original: " + tipo);
        Log.d(TAG, "Tipo normalizado: " + tipoNormalizado);

        // Crear el item del historial
        HistorialItem item = new HistorialItem(descripcion, tipoNormalizado, Timestamp.now());

        // Verificar que el tipo se guardó correctamente
        Log.d(TAG, "Tipo final en el item: " + item.getTipo());

        // Guardar en Firestore
        db.collection("historial")
                .add(item)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Evento registrado con ID: " + documentReference.getId());

                    // Verificar el documento guardado
                    documentReference.get().addOnSuccessListener(documentSnapshot -> {
                        HistorialItem itemGuardado = documentSnapshot.toObject(HistorialItem.class);
                        if (itemGuardado != null) {
                            Log.d(TAG, "Verificación del documento guardado:");
                            Log.d(TAG, "Tipo guardado: " + itemGuardado.getTipo());
                            Log.d(TAG, "Descripción guardada: " + itemGuardado.getDescripcion());
                        }
                    });
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error al registrar el evento", e));
    }

    // Métodos de conveniencia para registrar eventos específicos
    public static void registrarModificacion(String descripcion) {
        registrarEvento(descripcion, TIPO_MODIFICACION);
    }

    public static void registrarEliminacion(String descripcion) {
        registrarEvento(descripcion, TIPO_ELIMINACION);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}