package com.example.contraseniaproyecto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


public class Registro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
    }
    public void IrLogin(View v){
        Intent VueltaLogin = new Intent(this, MainActivity.class);
        startActivity(VueltaLogin);
    }
}