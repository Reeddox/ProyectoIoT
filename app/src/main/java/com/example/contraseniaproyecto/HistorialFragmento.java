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
    private HistorialAdapter adapter;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historial, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.historialRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new HistorialAdapter(getContext());
        recyclerView.setAdapter(adapter);

        // Inicializar Firestore
        db = FirebaseFirestore.getInstance();

        cargarHistorial();

        return view;
    }

    private void cargarHistorial() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("historial").document(userId).collection("historialGuardado")
                .orderBy("timestamp", Query.Direction.DESCENDING) // Ordenar por la marca temporal
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<HistorialItem> historialItems = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            HistorialItem item = document.toObject(HistorialItem.class);
                            historialItems.add(item);
                        }
                        adapter.actualizarLista(historialItems);
                    } else {
                        Toast.makeText(getContext(), "Error al cargar el historial: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
