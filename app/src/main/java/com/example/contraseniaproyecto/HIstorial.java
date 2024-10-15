package com.example.contraseniaproyecto;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class HIstorial extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdaptadorHistorial adaptador;
    private GestorHistorial gestorHistorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        gestorHistorial = new GestorHistorial(this);

        recyclerView = findViewById(R.id.recyclerViewHistorial);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<ElementoHistorial> elementosHistorial = gestorHistorial.obtenerHistorial();
        adaptador = new AdaptadorHistorial(elementosHistorial);
        recyclerView.setAdapter(adaptador);

        adaptador.setOnItemClickListener(item -> {
            // Mostrar fragmento de detalle
            FragmentoDetalleHistorial fragmentoDetalle = FragmentoDetalleHistorial.newInstance(item);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main, fragmentoDetalle)
                    .addToBackStack(null)
                    .commit();
        });
    }
}