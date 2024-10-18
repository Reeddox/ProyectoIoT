package com.example.contraseniaproyecto;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HistorialFragmento extends Fragment {
    private HistorialAdapter adapter; // Adaptador para el RecyclerView
    private FirebaseFirestore db; // Instancia de Firestore

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historial, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.historialRecyclerView); // Encontrar el RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Configurar el LayoutManager
        adapter = new HistorialAdapter(getContext()); // Crear el adaptador
        recyclerView.setAdapter(adapter); // Configurar el adaptador

        // Inicializar Firestore
        db = FirebaseFirestore.getInstance();

        cargarHistorial(); // Cargar el historial al crear el fragmento

        return view; // Devolver la vista
    }

    private void cargarHistorial() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("historial").document(userId).collection("historialGuardado") // Obtener la colección del historial
                .orderBy("timestamp", Query.Direction.DESCENDING) // Ordenar por la marca temporal
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) { // Verificar si la tarea fue exitosa
                        List<HistorialItem> historialItems = new ArrayList<>(); // Crear una lista para los items
                        for (QueryDocumentSnapshot document : task.getResult()) { // Recorrer los documentos
                            HistorialItem item = document.toObject(HistorialItem.class); // Convertir el documento a un objeto HistorialItem
                            historialItems.add(item); // Agregar el item a la lista
                        }
                        adapter.actualizarLista(historialItems); // Actualizar el adaptador con la lista
                    } else {
                        Toast.makeText(getContext(), "Error al cargar el historial: " + task.getException().getMessage(), // Mostrar un mensaje de error
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
