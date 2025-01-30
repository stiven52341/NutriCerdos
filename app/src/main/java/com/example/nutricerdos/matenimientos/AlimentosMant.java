package com.example.nutricerdos.matenimientos;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.nutricerdos.R;

import java.util.ArrayList;
import java.util.List;

import Services.Conex;
import models.Alimentacion;
import models.Alimento;
import models.AlimentoUnidad;
import models.Callback;
import models.GlobalData;
import models.InsertResponse;
import models.ResponseEntity;
import models.TipoAlimento;
import models.Unidad;

public class AlimentosMant extends AppCompatActivity {
    private List<Alimento> alimentos;
    private List<TipoAlimento> tiposAlimentos;
    private List<Unidad> unidades;
    private List<AlimentoUnidad> alimento_unidades;

    private Spinner alimentosSp;
    private Spinner tipoSp;
    private Spinner unidadesSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_alimentos_mant);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.alimentosSp = findViewById(R.id.alimentos);
        this.tipoSp = findViewById(R.id.tipo);
        this.unidadesSp = findViewById(R.id.unidad);

        onInit();
    }

    private void onInit(){
        getAlimentos(this);
        getTipos(this);
        getUnidades(this);
        getAlimentosUnidad(this);

        alimentosSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(getIdAlimento() == -1)return;

                Alimento al = null;
                for(Alimento a: alimentos){
                    if(a.getId() == getIdAlimento()){
                        al = a;
                        break;
                    }
                }

                AlimentoUnidad au_ = null;
                for(AlimentoUnidad au: alimento_unidades){
                    assert al != null;
                    if(au.getId_alimento() == al.getId()){
                        au_ = au;
                        break;
                    }
                }
                if(al == null){
                    return;
                }
                setForm(al,au_);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getAlimentos(AlimentosMant context){
        Conex<Alimento> conn = new Conex<>(GlobalData.path + "/alimento", Alimento.class);

        conn.getAll(new Callback<List<Alimento>>() {
            @Override
            public void onSuccess(List<Alimento> result) {
                context.alimentos = result;
                ArrayList<String> aliStr = new ArrayList<>();
                aliStr.add("Nuevo");
                for(Alimento a: result){
                    aliStr.add(a.getId() + ". " + a.getNombre());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        context,
                        android.R.layout.simple_spinner_item,
                        aliStr
                );

                adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);

                runOnUiThread(() -> {
                    context.alimentosSp.setAdapter(adapter);
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(context, "ERROR CARGANDO ALIMENTOS", Toast.LENGTH_SHORT).show();
                });
                Log.e("ERROR ALIMENTOS", e.getMessage());
            }
        });
    }

    private void getTipos(AlimentosMant context){
        Conex<TipoAlimento> conn = new Conex<>(GlobalData.path + "/tipo-alimento", TipoAlimento.class);

        conn.getAll(new Callback<List<TipoAlimento>>() {
            @Override
            public void onSuccess(List<TipoAlimento> result) {
                context.tiposAlimentos = result;

                ArrayList<String> aliStr = new ArrayList<>();
                for(TipoAlimento a: result){
                    aliStr.add(a.getId() + ". " + a.getDescr());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        context,
                        android.R.layout.simple_spinner_item,
                        aliStr
                );

                adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);

                runOnUiThread(() -> {
                    context.tipoSp.setAdapter(adapter);
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(context, "ERROR AL CARGAR TIPOS ALIMENTOS", Toast.LENGTH_SHORT).show();
                });
                Log.e("ERROR TIPO ALIMENTOS", e.getMessage());
            }
        });
    }

    private void getUnidades(AlimentosMant context){
        Conex<Unidad> conex = new Conex<>(GlobalData.path +"/unidades", Unidad.class);

        conex.getAll(new Callback<List<Unidad>>() {
            @Override
            public void onSuccess(List<Unidad> result) {
                context.unidades = result;

                ArrayList<String> aliStr = new ArrayList<>();
                for(Unidad a: result){
                    aliStr.add(a.getId() + ". " + a.getNombre());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        context,
                        android.R.layout.simple_spinner_item,
                        aliStr
                );

                adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);

                runOnUiThread(() -> {
                    context.unidadesSp.setAdapter(adapter);
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(context, "ERROR CON UNIDADES", Toast.LENGTH_SHORT);
                });
                Log.e("ERROR UNIDADES", e.getMessage());
            }
        });
    }

    private void getAlimentosUnidad(AlimentosMant context){
        Conex<AlimentoUnidad> au = new Conex<>(GlobalData.path + "/alimento_unidad", AlimentoUnidad.class);
        au.getAll(new Callback<List<AlimentoUnidad>>() {
            @Override
            public void onSuccess(List<AlimentoUnidad> result) {
                context.alimento_unidades = result;
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(context, "ERROR CON A-U", Toast.LENGTH_SHORT);
                });
            }
        });
    }

    public void guardar(View view){
        String nombre = ((EditText)findViewById(R.id.nombre)).getText().toString();
        short idTipo = getIdTipoFromSpinner();
        float cantidad = Float.parseFloat(((EditText)findViewById(R.id.cantidad)).getText().toString());
        short idUnidad = getIdUnidad();
        float precio = Float.parseFloat(((EditText)findViewById(R.id.precio)).getText().toString());

        String idAlimento = ((Spinner)findViewById(R.id.alimentos)).getSelectedItem().toString();

        Alimento a = new Alimento();
        a.setId_tipo(idTipo);
        a.setNombre(nombre);

        Conex<Alimento> cnAli = new Conex<>(GlobalData.path + "/alimento",Alimento.class);
        Conex<AlimentoUnidad>  cnAliUni = new Conex<>(GlobalData.path + "/alimento_unidad",AlimentoUnidad.class);

        AlimentoUnidad au = new AlimentoUnidad();
        au.setDisponible(cantidad);
        au.setEstado(true);
        au.setPrecio(precio);
        au.setId_unidad(idUnidad);

        if(idAlimento.equalsIgnoreCase("nuevo")){
            cnAli.insert(a, new Callback<InsertResponse>() {
                @Override
                public void onSuccess(InsertResponse result) {
                    int id = result.getData();

                    au.setId_alimento(id);

                    cnAliUni.insert(au, new Callback<InsertResponse>() {
                        @Override
                        public void onSuccess(InsertResponse result) {
                            runOnUiThread(() -> {
                                Toast.makeText(view.getContext(), "ALIMENTO GUARDADO", Toast.LENGTH_LONG).show();
                            });
                        }

                        @Override
                        public void onError(Exception e) {
                            runOnUiThread(() -> {
                                Toast.makeText(view.getContext(), "ERROR AL GUARDAR ALIMENTO-UNIDAD", Toast.LENGTH_LONG).show();
                            });
                            Log.e("ALIMENTO-UNIDAD", e.getMessage());
                        }
                    });
                }

                @Override
                public void onError(Exception e) {
                    runOnUiThread(() -> {
                        Toast.makeText(view.getContext(), "ERROR AL GUARDAR ALIMENTO", Toast.LENGTH_LONG).show();
                    });
                    Log.e("ALIMENTO", e.getMessage());
                }
            });
        }else{
            int id = getIdAlimento();
            a.setId(id);

            cnAli = new Conex<>(GlobalData.path + "/alimento/update", Alimento.class);
            cnAli.update(a, new Callback<ResponseEntity<Boolean>>() {
                @Override
                public void onSuccess(ResponseEntity<Boolean> result) {
                    au.setId_alimento(a.getId());

                    Conex<AlimentoUnidad> cnAliUni = new Conex<>(GlobalData.path + "/alimento_unidad/update",AlimentoUnidad.class);

                    cnAliUni.update(au, new Callback<ResponseEntity<Boolean>>() {
                        @Override
                        public void onSuccess(ResponseEntity<Boolean> result) {
                            runOnUiThread(() -> {
                                Toast.makeText(view.getContext(), "ALIMENTO ACTUALIZADO", Toast.LENGTH_LONG).show();
                            });
                        }

                        @Override
                        public void onError(Exception e) {
                            runOnUiThread(() -> {
                                Toast.makeText(view.getContext(), "ERROR AL ACTUALIZAR", Toast.LENGTH_LONG).show();
                            });
                            Log.e("ACTIALIZAR", e.getMessage());
                        }
                    });
                }

                @Override
                public void onError(Exception e) {
                    runOnUiThread(() -> {
                        Toast.makeText(view.getContext(), "ERROR AL ACTUALIZAR", Toast.LENGTH_LONG).show();
                    });
                    Log.e("ACTIALIZAR", e.getMessage());
                }
            });
        }
    }

    private short getIdTipoFromSpinner(){
        Spinner sp = findViewById(R.id.tipo);
        return Short.parseShort(sp.getSelectedItem().toString().split(". ")[0]);
    }

    private short getIdUnidad(){
        Spinner sp = findViewById(R.id.unidad);
        return Short.parseShort(sp.getSelectedItem().toString().split(". ")[0]);
    }

    private int getIdAlimento(){
        Spinner sp = findViewById(R.id.alimentos);
        if(sp.getSelectedItem().toString().equalsIgnoreCase("nuevo")) return -1;

        return Integer.parseInt(sp.getSelectedItem().toString().split(". ")[0]);
    }

    private void setForm(Alimento a, AlimentoUnidad au){
        ((EditText)findViewById(R.id.nombre)).setText(a.getNombre());
        ((Spinner)findViewById(R.id.tipo)).setSelection(locateIdTipo(a));
        ((EditText)findViewById(R.id.cantidad)).setText(au.getDisponible() + "");
        ((Spinner)findViewById(R.id.unidad)).setSelection(locateIdUnidad(au.getId_unidad()));
        ((EditText)findViewById(R.id.precio)).setText(au.getPrecio() + "");
    }

    private int locateIdTipo(Alimento a){
        int index = 0;
        Spinner tipo = (Spinner)findViewById(R.id.tipo);

        ArrayAdapter adapter = (ArrayAdapter) tipo.getAdapter();
        for(int i = 0; i < adapter.getCount(); i++){
            int id = Integer.parseInt(adapter.getItem(i).toString().split(". ")[0]);

            if(a.getId() == id){
                index = i;
                break;
            }

        }
        return index;
    }

    private int locateIdUnidad(short idUnidad){
        int index = 0;
        Spinner tipo = (Spinner)findViewById(R.id.unidad);

        ArrayAdapter adapter = (ArrayAdapter) tipo.getAdapter();
        for(int i = 0; i < adapter.getCount(); i++){
            int id = Integer.parseInt(adapter.getItem(i).toString().split(". ")[0]);

            if(idUnidad == id){
                index = i;
                break;
            }

        }
        return index;
    }
}