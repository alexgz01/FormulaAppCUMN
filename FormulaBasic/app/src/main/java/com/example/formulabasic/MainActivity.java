package com.example.formulabasic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView textViewBienvenida;
    private Button buttonCerrarSesion;
    private Button buttonCircuitos;
    private Button buttonPilotos;
    private Button buttonFavoritos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        textViewBienvenida = findViewById(R.id.textViewBienvenida);
        buttonCerrarSesion = findViewById(R.id.buttonCerrarSesion);
        buttonCircuitos = findViewById(R.id.buttonCircuitos);
        buttonPilotos = findViewById(R.id.buttonPilotos);
        buttonFavoritos = findViewById(R.id.buttonFavoritos);


        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            textViewBienvenida.setText("Bienvenido, " + currentUser.getEmail());
        } else {
            // Si no hay usuario logueado, redirigir a la pantalla de login
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return;
        }

        buttonCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });

        buttonCircuitos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción para el botón Circuitos: Iniciar CircuitsActivity
                startActivity(new Intent(MainActivity.this, CircuitsActivity.class));
            }
        });

        buttonPilotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción para el botón Pilotos: Iniciar DriversActivity
                startActivity(new Intent(MainActivity.this, DriversActivity.class));
            }
        });

        buttonFavoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción para el botón Favoritos: Iniciar FavoritesActivity
                startActivity(new Intent(MainActivity.this, FavoritesActivity.class));
            }
        });
    }
}