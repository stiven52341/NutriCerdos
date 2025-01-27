package com.example.nutricerdos;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.nutricerdos.databinding.ActivityMainBinding;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;

import Services.Conex;
import models.Alimentacion;
import models.Callback;
import models.GlobalData;
import models.ResponseEntity;
import models.Usuario;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.materialToolbar);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void onIngresar(View view){
        EditText userfield = findViewById(R.id.username);
        EditText passfield = findViewById(R.id.password);

        String username = userfield.getText().toString();
        String password = passfield.getText().toString();

        Conex<Usuario> conn = new Conex<Usuario>(GlobalData.path + "/usuario/login", Usuario.class);

        conn.login(username, password, new Callback<Usuario>() {
            @Override
            public void onSuccess(Usuario result) {
                if(result == null){
                    runOnUiThread(() -> {
                        Toast.makeText(MainActivity.this, "USUARIO O CONTRASEÑA INCORRECTOS", Toast.LENGTH_SHORT).show();
                    });
                }

                GlobalData.usuario = result;
                if(GlobalData.usuario == null) return;
                goToMenu();
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "USUARIO O CONTRASEÑA INCORRECTOS", Toast.LENGTH_SHORT).show();
                });
                Log.e("ERROR LOGIN", e.getMessage());
            }
        });
    }

    private void goToMenu(){
        Intent intent = new Intent(this, MenuPrincipal.class);
        startActivity(intent); // Inicia la nueva actividad
    }
}