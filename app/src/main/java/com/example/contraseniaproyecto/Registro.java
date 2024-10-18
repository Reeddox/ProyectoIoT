package com.example.contraseniaproyecto;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class Registro extends AppCompatActivity {

    // Declaración de variables
    private static final int PICK_IMAGE_REQUEST = 1; // Constante para la solicitud de imagen

    private EditText editTextNombre, editTextApellido, editTextUsuario, editTextEmail,
            editTextContrasena, editTextConfirmarContrasena, editTextPIN;
    private CheckBox checkBoxTerminos;
    private ImageView imagenPerfil;
    private Uri imagenUri;

    // Referencias a Firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Inicializar Firebase Auth y Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference("profile_images");

        // Encontrar vistas
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextApellido = findViewById(R.id.editTextApellido);
        editTextUsuario = findViewById(R.id.Usuario);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextContrasena = findViewById(R.id.editTextContrasena);
        editTextConfirmarContrasena = findViewById(R.id.editTextConfirmarContrasena);
        editTextPIN = findViewById(R.id.editTextPIN);
        checkBoxTerminos = findViewById(R.id.checkBoxTerminos);
        imagenPerfil = findViewById(R.id.ImagenPerfil);

        imagenPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirSelectorDeImagen();
            }
        });
    }

    // Método para abrir el selector de imágenes
    private void abrirSelectorDeImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI); // Intent para seleccionar imagen
        startActivityForResult(intent, PICK_IMAGE_REQUEST); // Iniciar la actividad
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imagenUri = data.getData();
            imagenPerfil.setImageURI(imagenUri);
        }
    }

    // Método para validar el registro
    public void validarRegistro(View v) {
        // Obtener datos del formulario
        final String nombre = editTextNombre.getText().toString().trim();
        final String apellido = editTextApellido.getText().toString().trim();
        final String usuario = editTextUsuario.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        String contrasena = editTextContrasena.getText().toString().trim();
        String confirmarContrasena = editTextConfirmarContrasena.getText().toString().trim();
        final String pin = editTextPIN.getText().toString().trim();

        // Validar campos
        if (nombre.isEmpty() || apellido.isEmpty() || usuario.isEmpty() || email.isEmpty() ||
                contrasena.isEmpty() || confirmarContrasena.isEmpty() || pin.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }
        // Validar contraseñas
        if (!contrasena.equals(confirmarContrasena)) {
            Toast.makeText(this, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
            return;
        }
        // Validar si se ha aceptado los términos y condiciones
        if (!checkBoxTerminos.isChecked()) {
            Toast.makeText(this, "Debe aceptar los términos y condiciones.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear usuario en Firebase Auth usando el nombre de usuario como email
        mAuth.createUserWithEmailAndPassword(usuario + "@dominio.com", contrasena)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registro exitoso, ahora se sube la imagen y guardamos los datos
                            String userId = mAuth.getCurrentUser().getUid();
                            subirImagenYGuardarDatos(userId, nombre, apellido, usuario, email, pin);
                        } else {
                            Toast.makeText(Registro.this, "Error en el registro: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Método para subir la imagen y guardar los datos del usuario
    private void subirImagenYGuardarDatos(final String userId, final String nombre, final String apellido,
                                          final String usuario, final String email, final String pin) {
        if (imagenUri != null) {
            final StorageReference fileReference = storageRef.child(userId + "." + getFileExtension(imagenUri));
            fileReference.putFile(imagenUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();
                                    guardarDatosUsuario(userId, nombre, apellido, usuario, email, pin, imageUrl);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Registro.this, "Error al subir la imagen: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            guardarDatosUsuario(userId, nombre, apellido, usuario, email, pin, null);
                        }
                    });
        } else {
            guardarDatosUsuario(userId, nombre, apellido, usuario, email, pin, null);
        }
    }

    // Método para guardar los datos del usuario en Firestore
    private void guardarDatosUsuario(String userId, String nombre, String apellido, String usuario,
                                     String email, String pin, String imageUrl) {
        Map<String, Object> user = new HashMap<>();
        user.put("nombre", nombre);
        user.put("apellido", apellido);
        user.put("usuario", usuario);
        user.put("email", email);
        user.put("pin", pin);
        if (imageUrl != null) {
            user.put("imagenPerfil", imageUrl);
        }

        // Guardar los datos en Firestore
        db.collection("usuarios").document(userId)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Registro.this, "Registro exitoso.", Toast.LENGTH_SHORT).show();
                        Intent vueltaLogin = new Intent(Registro.this, MainActivity.class);
                        startActivity(vueltaLogin);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Registro.this, "Error al guardar los datos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    // Método para obtener la extensión del archivo
    private String getFileExtension(Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(getContentResolver().getType(uri));
    }
    // Método para ir a la pantalla de inicio de sesión
    public void IrLogin(View v) {
        Intent vueltaLogin = new Intent(this, MainActivity.class);
        startActivity(vueltaLogin);
        finish();
    }
}