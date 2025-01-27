package com.example.nutricerdos.matenimientos;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
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
import java.util.Calendar;
import java.util.List;

import Services.Conex;
import models.Callback;
import models.GlobalData;
import models.InsertResponse;
import models.Lote;
import models.ResponseEntity;
import models.TipoLote;

public class LotesMant extends AppCompatActivity {
    private ArrayList<String> lotesStrList = new ArrayList<>();
    private List<Lote> lotes;

    private Calendar calendar = Calendar.getInstance();

    private Spinner lotesSp;
    private Context context;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lotes_mant);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        lotesSp = findViewById(R.id.lotes);
        context = this;

        onInit();
    }

    private void onInit(){
        runOnUiThread(() -> {
            Toast.makeText(this, "CARGANDO...", Toast.LENGTH_SHORT).show();
        });
        getLotes(this);
        getTipos(this);

        ((Spinner)(findViewById(R.id.lotes))).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                ((Spinner)(findViewById(R.id.lotes))).setSelection(i);
                setForm(getSelectedLote());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void showDatePicker(View view){

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog picker = new DatePickerDialog(
                this,
                (DatePicker view_, int selectedYear,int selectedMonth, int selectedDay) -> {
                    selectedMonth += 1;
                    String selectedMonthStr = selectedMonth <= 9 ? "0"+ selectedMonth : selectedMonth + "";

                    String selectedDayStr = selectedDay < 10 ? "0" + selectedDay : selectedDay + "";

                    String date = selectedYear + "-" + selectedMonthStr + "-" + selectedDayStr;
                    ((EditText)(findViewById(R.id.fecCreacion))).setText(date);
                },
                year,month,day
        );
        picker.show();
    }

    private void getLotes(LotesMant clase){


        Conex<Lote> lotesDao = new Conex<>(GlobalData.path +"/lotes", Lote.class);

        lotesDao.getAll(new Callback<List<Lote>>() {
            @Override
            public void onSuccess(List<Lote> result) {
                clase.lotes = result;
                clase.lotesStrList.add("NUEVO");
                for(Lote lote: result) {
                    clase.lotesStrList.add(lote.getId() + " - " + lote.getNombre());
                }

                runOnUiThread(() -> {
                    adapter = new ArrayAdapter<>(
                            clase.context,
                            android.R.layout.simple_spinner_item,
                            clase.lotesStrList
                    );
                    clase.adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                    clase.lotesSp.setAdapter(clase.adapter);
                });

            }

            @Override
            public void onError(Exception e) {

                Log.e("LOTES ERROR", e.getMessage());
            }
        });
    }

    private void getTipos(LotesMant clase){
        Conex<TipoLote> tipoLotesDao = new Conex<>(GlobalData.path + "/tipolote", TipoLote.class);

        tipoLotesDao.getAll(new Callback<List<TipoLote>>() {
            @Override
            public void onSuccess(List<TipoLote> result) {
                List<String> tipoLotes = new ArrayList<>();

                for(TipoLote tl: result){
                   tipoLotes.add(tl.getId() + ". " + tl.getDescr());
                }

                runOnUiThread(() -> {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            clase.context,
                            android.R.layout.simple_spinner_item,
                            tipoLotes
                    );
                    adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                    Spinner spTipos = findViewById(R.id.tipo);
                    spTipos.setAdapter(adapter);
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(clase, "Error obteniendo tipos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    public void guardar(View view) {
        try{
            String lote = (String)this.lotesSp.getSelectedItem();
            String nombre = ((EditText)findViewById(R.id.nombre)).getText().toString();
            short tipo = Short.parseShort(((String)((Spinner)(findViewById(R.id.tipo))).getSelectedItem()).split(". ")[0]);
            int cantidad = Integer.parseInt(((EditText)findViewById(R.id.cantCerdos)).getText().toString());
            String fecha = ((EditText)(findViewById(R.id.fecCreacion))).getText().toString();
            boolean activo = ((CheckBox)(findViewById(R.id.activo))).isChecked();

            Lote loteObj = new Lote();
            loteObj.setNombre(nombre);
            loteObj.setEstado(activo);
            loteObj.setId_tipo(tipo);
            loteObj.setCantidad_cerdos(cantidad);
            loteObj.setFecha_creacion(fecha);
            loteObj.setId(getSelectedLote().getId());

            if(!lote.equalsIgnoreCase("nuevo")){

                Conex<Lote> conn = new Conex<>(GlobalData.path + "/lotes/update",Lote.class);
                conn.update(loteObj, new Callback<ResponseEntity<Boolean>>() {
                    @Override
                    public void onSuccess(ResponseEntity<Boolean> result) {
                        runOnUiThread(() -> {
                            Toast.makeText(view.getContext(),"LOTE ACTUALIZADO",Toast.LENGTH_LONG).show();
                        });
                    }

                    @Override
                    public void onError(Exception e) {
                        runOnUiThread(() -> {
                            Toast.makeText(view.getContext(),"ERROR",Toast.LENGTH_LONG).show();
                        });
                        Log.e("ERROR ACT", e.getMessage());
                    }
                });

            }else{
                Conex<Lote> conn = new Conex<>(GlobalData.path + "/lotes", Lote.class);

                conn.insert(loteObj, new Callback<InsertResponse>() {
                    @Override
                    public void onSuccess(InsertResponse result) {
                        runOnUiThread(() -> {
                            Toast.makeText(view.getContext(), "LOTE GUARDADO", Toast.LENGTH_LONG).show();
                        });
                    }

                    @Override
                    public void onError(Exception e) {
                        runOnUiThread(() -> {
                            Toast.makeText(view.getContext(), "ERROR AL GUARDAR", Toast.LENGTH_LONG).show();
                            Log.e("ERROR INGRESANDO LOTE", e.getMessage());
                        });

                    }
                });
            }
        }catch(Exception e){

        }
    }

    public void cleanForm(View view){
        this.lotesSp.setSelection(0);
        ((EditText)findViewById(R.id.nombre)).setText("");
        ((Spinner)findViewById(R.id.tipo)).setSelection(0);
        ((EditText)findViewById(R.id.cantCerdos)).setText("");
        ((EditText)findViewById(R.id.fecCreacion)).setText("");
        findViewById(R.id.activo).setSelected(false);
    }

    public void setForm(Lote lote){
        if(lote == null) return;
        try{
            ((EditText)findViewById(R.id.nombre)).setText(lote.getNombre());
            ((Spinner)(findViewById(R.id.tipo))).setSelection(getTipoFromSpiner(lote));
            ((EditText)findViewById(R.id.cantCerdos)).setText(lote.getCantidad_cerdos() + "");
            ((EditText)findViewById(R.id.fecCreacion)).setText(lote.getFecha_creacion());
            boolean e = lote.isEstado();
            ((CheckBox)findViewById(R.id.activo)).setChecked(lote.isEstado());
        }catch(Exception e){
            Log.e("ERROR AAWF", e.getMessage());
        }
    }

    private int getTipoFromSpiner(Lote lote){
        int index = 0;
        Spinner tipo = (Spinner)findViewById(R.id.tipo);

        ArrayAdapter adapter = (ArrayAdapter) tipo.getAdapter();
        for(int i = 0; i < adapter.getCount(); i++){
            int id = Integer.parseInt(adapter.getItem(i).toString().split(". ")[0]);

            if(lote.getId() == id){
                index = i;
                break;
            }

        }
        return index;
    }

    private Lote getSelectedLote(){
        Spinner spLotes = (Spinner)findViewById(R.id.lotes);
        String text = (String)spLotes.getSelectedItem();
        if(text.equalsIgnoreCase("nuevo"))return null;

        int id = Integer.parseInt(text.split(" - ")[0]);

        for(Lote lote: this.lotes){
            if(lote.getId() == id){
                return lote;
            }
        }

        return null;
    }
}