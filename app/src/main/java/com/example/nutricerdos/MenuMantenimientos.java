package com.example.nutricerdos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.nutricerdos.matenimientos.AlimentosMant;
import com.example.nutricerdos.matenimientos.LotesMant;

public class MenuMantenimientos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_mantenimientos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void GoToLotesMant(View view){
        Intent intent = new Intent(this, LotesMant.class);
        startActivity(intent); // Inicia la nueva actividad
    }

    public void GoToAliMant(View view){
        Intent intent = new Intent(this, AlimentosMant.class);
        startActivity(intent);
    }
}