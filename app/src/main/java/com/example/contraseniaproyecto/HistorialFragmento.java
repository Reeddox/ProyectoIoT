package com.example.contraseniaproyecto;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class HistorialFragmento extends Fragment {
    private RecyclerView recyclerView;
    private HistorialAdapter adapter;
    private List<String> eventos = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historial, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewHistorial);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new HistorialAdapter(eventos);
        recyclerView.setAdapter(adapter);
        return view;
    }

    public void agregarEvento(String evento) {
        eventos.add(0, evento);
        adapter.notifyItemInserted(0);
        recyclerView.smoothScrollToPosition(0);
    }
}