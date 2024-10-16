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
    private Button savePasswordButton;
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
        generateButton = findViewById(R.id.btnGuardar);
        savePasswordButton = findViewById(R.id.btnSavePassword);
        savePasswordButton.setVisibility(View.GONE); // Inicialmente oculto

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
        generateButton.setOnClickListener(v -> {
            generatePassword();
            savePasswordButton.setVisibility(View.VISIBLE); // Mostrar el botón después de generar
        });

        // Configurar el nuevo botón para guardar contraseña
        savePasswordButton.setOnClickListener(v -> {
            String generatedPassword = passwordEditText.getText().toString();
            Intent intent = new Intent(Generador.this, AniadirContrasenia.class);
            intent.putExtra("CONTRASENIA_GENERADA", generatedPassword);
            startActivity(intent);
        });

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
                } else if (id == R.id.nav_item2) {
                    drawerLayout.closeDrawers();
                } else if (id == R.id.nav_item3) {
                    Intent intentHistorial = new Intent(Generador.this, HIstorial.class);
                    startActivity(intentHistorial);
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
        // Obtiene el valor de la lonmgitud seleccionada en el Seekbar
        int length = lengthSeekBar.getProgress();

        // Verifica si los Checkboxs estan marcados ya sea el de números, caracteres especiales y letras mayúsculas
        boolean useNumbers = numbersCheckBox.isChecked();
        boolean useSpecialChars = specialCharsCheckBox.isChecked();
        boolean useUpperCase = upperCaseCheckBox.isChecked();

        // Cadenas de caracteres disponibles para la generacion de la contraseña
        String lowercaseChars = "abcdefghijklmnopqrstuvwxyz"; // Letras minusculas
        String uppercaseChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; // Letras mayusculas
        String numberChars = "0123456789"; // Números
        String specialChars = "!@#$%^&*()_-+=<>?"; // Caracteres especiales

        // Inicializa la lista de caracteres válidos con las letras minúsculas
        StringBuilder validChars = new StringBuilder(lowercaseChars);

        // Si el checkbox de números esta marcado, se agregan los numeros a la contraseña generada
        if (useNumbers) validChars.append(numberChars);

        // Si el checkbox de caracteres especiales esta marcado, se agregan los esos caracteres a la contraseña generada
        if (useSpecialChars) validChars.append(specialChars);

        // Si el checkbox de letras mayúsculas esta marcado, se agregan las letras mayúsculas a la contraseña generada
        if (useUpperCase) validChars.append(uppercaseChars);

        // Crea un objeto Random para seleccionar caracteres al azar
        Random random = new Random();

        // StringBuilder para almacenar la contraseña generada
        StringBuilder password = new StringBuilder(length);

        // Genera la contraseña caracter por caracter de manera aleatoria
        for (int i = 0; i < length; i++) {
            // Añade un caracter aleatorio de los caracteres validos
            password.append(validChars.charAt(random.nextInt(validChars.length())));
        }

        // Establece la contraseña generada en el EditText para mostrarla al usuario
        passwordEditText.setText(password.toString());

        // Mostrar el botón de guardar después de generar la contraseña
        savePasswordButton.setVisibility(View.VISIBLE);
    }
}