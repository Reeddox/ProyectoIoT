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
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FirebaseAuth mAuth;

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

    // Método para registrar un nuevo evento en el historial
    public static void registrarEvento(String descripcion, String tipo) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Crear un nuevo documento en la colección "historial"
        HistorialItem item = new HistorialItem(descripcion, tipo, Timestamp.now()); // Añadir la marca temporal
        db.collection("historial")
                .add(item)
                .addOnSuccessListener(documentReference -> Log.d("Firestore", "Evento registrado con ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w("Firestore", "Error al registrar el evento", e));
    }
}