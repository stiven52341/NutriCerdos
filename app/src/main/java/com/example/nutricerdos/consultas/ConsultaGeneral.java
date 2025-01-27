package com.example.nutricerdos.consultas;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.nutricerdos.R;

import java.util.List;

import Services.Conex;
import models.Alimento;
import models.AlimentoUnidad;
import models.Callback;
import models.GlobalData;
import models.Lote;

public class ConsultaGeneral extends AppCompatActivity {
    private TextView lotes;
    private TextView cerdos;
    private TextView proLotesCerdos;
    private TextView proAliPre;
    private TextView aliCaro;
    private TextView aliBara;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_consulta_general);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        lotes = findViewById(R.id.totalLotes);
        cerdos = findViewById(R.id.totalCerdos);
        proLotesCerdos = findViewById(R.id.proCerdoLote);
        proAliPre = findViewById(R.id.proAliPre);
        aliCaro = findViewById(R.id.aliCaro);
        aliBara = findViewById(R.id.aliBara);

        onInit();
    }

    private void onInit(){
        getLotes(this);
    }

    private void getLotes(ConsultaGeneral context){
        Conex<Lote> con = new Conex<>(GlobalData.path + "/lotes", Lote.class);
        con.getAll(new Callback<List<Lote>>() {
            @Override
            public void onSuccess(List<Lote> result) {
                context.lotes.setText("TOTAL DE LOTES: " + result.size());

                int totalCerdos = 0;
                for(Lote lote: result){
                    totalCerdos += lote.getCantidad_cerdos();
                }

                context.cerdos.setText("TOTAL DE CERDOS: " + totalCerdos);
                context.proLotesCerdos.setText(String.format("PROMEDIO CERDO-LOTE: %.2d", totalCerdos / result.size()));
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(context, "ERROR LOTES", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void getAlimentos(ConsultaGeneral context){
        Conex<Alimento> con = new Conex<>(GlobalData.path + "/alimentos", Alimento.class);
        con.getAll(new Callback<List<Alimento>>() {
            @Override
            public void onSuccess(List<Alimento> result) {
                List<Alimento> alimentos = result;



                Conex<AlimentoUnidad> con = new Conex<>(GlobalData.path + "/alimento_unidad", AlimentoUnidad.class);
                con.getAll(new Callback<List<AlimentoUnidad>>() {
                    @Override
                    public void onSuccess(List<AlimentoUnidad> result) {
                        List<AlimentoUnidad> aus = result;

                        int totalPrecios = 0;
                        for(AlimentoUnidad au: aus){
                            totalPrecios += au.getPrecio();
                        }

                        context.proAliPre.setText(String.format("PROMEDIO ALIMENTO-PRECIO: %.2d", totalPrecios / alimentos.size()));

                        context.aliCaro.setText("ALIMENTO MÁS BARATO: " + getAlimentoMasCaro(alimentos, aus).getNombre());
                        context.aliBara.setText("ALIMENTO MÁS BARATO: " + getAlimentoMasBarato(alimentos, aus));
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    private Alimento getAlimentoMasCaro(List<Alimento> alimentos, List<AlimentoUnidad> unidades){
        Alimento masCaro = alimentos.get(0);

        for(Alimento a: alimentos){
            if(getUnidadFromAlimento(a, unidades).getPrecio() > getUnidadFromAlimento(masCaro,unidades).getPrecio()){
                masCaro = a;
            }
        }

        return masCaro;
    }
    private AlimentoUnidad getUnidadFromAlimento(Alimento a, List<AlimentoUnidad> unidades){
        for(AlimentoUnidad unidad: unidades){
            if(a.getId() == unidad.getId_alimento()){
                return unidad;
            }
        }

        return null;
    }

    private Alimento getAlimentoMasBarato(List<Alimento> alimentos, List<AlimentoUnidad> unidades){
        Alimento menosCaro = alimentos.get(0);

        for(Alimento a: alimentos){
            if(getUnidadFromAlimento(a, unidades).getPrecio() < getUnidadFromAlimento(menosCaro,unidades).getPrecio()){
                menosCaro = a;
            }
        }

        return menosCaro;
    }
}