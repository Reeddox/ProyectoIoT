package com.example.contraseniaproyecto;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import java.util.Random;

public class Generador extends AppCompatActivity {
    private EditText passwordEditText;
    private SeekBar lengthSeekBar;
    private TextView lengthTextView;
    private CheckBox numbersCheckBox, specialCharsCheckBox, upperCaseCheckBox;
    private Button generateButton;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_generador);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainGenerador), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar vistas del generador de contraseñas
        passwordEditText = findViewById(R.id.passwordEditText);
        lengthSeekBar = findViewById(R.id.lengthSeekBar);
        lengthTextView = findViewById(R.id.lengthTextView);
        numbersCheckBox = findViewById(R.id.numbersCheckBox);
        specialCharsCheckBox = findViewById(R.id.specialCharsCheckBox);
        upperCaseCheckBox = findViewById(R.id.upperCaseCheckBox);
        generateButton = findViewById(R.id.generateButton);

        // Configurar SeekBar
        lengthSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lengthTextView.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Configurar botón de generación
        generateButton.setOnClickListener(v -> generatePassword());

        // Inicializar y configurar el menú lateral
        drawerLayout = findViewById(R.id.mainGenerador);
        NavigationView menu = findViewById(R.id.menuGenerador);
        menu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_item1) {
                    Intent intentPrincipal = new Intent(Generador.this, VentanaPrincipal.class);
                    startActivity(intentPrincipal);

                } else if (id == R.id.nav_item6) {
                    Intent intentLogout = new Intent(Generador.this, MainActivity.class);
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
                Intent intentAniadirContrasenia = new Intent(Generador.this, AniadirContrasenia.class);
                startActivity(intentAniadirContrasenia);
            }
        });
    }

    private void generatePassword() {
        int length = lengthSeekBar.getProgress();
        boolean useNumbers = numbersCheckBox.isChecked();
        boolean useSpecialChars = specialCharsCheckBox.isChecked();
        boolean useUpperCase = upperCaseCheckBox.isChecked();

        String lowercaseChars = "abcdefghijklmnopqrstuvwxyz";
        String uppercaseChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String numberChars = "0123456789";
        String specialChars = "!@#$%^&*()_-+=<>?";

        StringBuilder validChars = new StringBuilder(lowercaseChars);
        if (useNumbers) validChars.append(numberChars);
        if (useSpecialChars) validChars.append(specialChars);
        if (useUpperCase) validChars.append(uppercaseChars);

        Random random = new Random();
        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            password.append(validChars.charAt(random.nextInt(validChars.length())));
        }

        passwordEditText.setText(password.toString());
    }
}