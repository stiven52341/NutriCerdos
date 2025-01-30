package com.example.nutricerdos.matenimientos;

import static android.view.View.VISIBLE;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.Manifest;
import android.widget.Toast;

import com.example.nutricerdos.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Services.Conex;
import models.Alimento;
import models.Callback;
import models.GlobalData;
import models.Imagen;
import models.InsertResponse;
import models.ResponseEntity;

public class ArticulosImagenes extends AppCompatActivity {
    private List<Alimento> alimentos;
    private List<Imagen> imagenes;

    private Imagen imagen;

    private static final int PICK_IMAGE_REQUEST = 1;

    private Spinner alimentosSpinner;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_articulos_imagenes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.alimentosSpinner = findViewById(R.id.alimento);
        this.image = findViewById(R.id.img);

        onInit(this);
    }

    private void onInit(ArticulosImagenes context){
        runOnUiThread(() -> {
            Toast.makeText(context, "Cargando imagenes", Toast.LENGTH_LONG).show();
        });
        getAlimentos(context);

        this.alimentosSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setImage();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setImage(){
        String option = ((Spinner)findViewById(R.id.alimento)).getSelectedItem().toString();
        if(imagenes == null || imagenes.isEmpty()) return;

        int idAlimento = Integer.parseInt(option.split(". ")[0]);

        ImageView view = findViewById(R.id.img);
        for(Imagen imagen: this.imagenes){
            if(idAlimento == imagen.getId_alimento()){
                Bitmap bitmap = this.convertBase64ToBitmap(imagen.getImagen());


                runOnUiThread(()-> {
                    view.setImageBitmap(bitmap);
                    view.setVisibility(VISIBLE);
                });

                return;
            }

        }
        view.setVisibility(View.INVISIBLE);
    }

    private void getAlimentos(ArticulosImagenes context){
        Conex<Alimento> con = new Conex<>(GlobalData.path + "/alimento", Alimento.class);
        con.getAll(new Callback<List<Alimento>>() {
            @Override
            public void onSuccess(List<Alimento> result) {
                context.alimentos = result;

                ArrayList<String> alimentosStr = new ArrayList<>();

                for(Alimento alimento: result){
                    alimentosStr.add(alimento.getId() + ". " + alimento.getNombre());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        context,
                        android.R.layout.simple_spinner_item,
                        alimentosStr
                );

                adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);

                runOnUiThread(() -> {
                    context.alimentosSpinner.setAdapter(adapter);

                    runOnUiThread(() -> {
                        Toast.makeText(context, "Alimentos cargados", Toast.LENGTH_LONG).show();
                    });
                });

                Conex<Imagen> con2 = new Conex<>(GlobalData.path + "/imagen", Imagen.class);
                con2.getAll(new Callback<List<Imagen>>() {
                    @Override
                    public void onSuccess(List<Imagen> result) {
                        context.imagenes = result;
                        context.setImage();

                        runOnUiThread(() -> {
                            Toast.makeText(context, "Imagenes cargadas", Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onError(Exception e) {
                        runOnUiThread(() -> {
                            Toast.makeText(context, "Error al cargar imagenes", Toast.LENGTH_SHORT).show();
                        });
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(context, "Error al cargar alimentos", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    public void openImgPicker(View view){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }

        Intent intent = new Intent();
        intent.setType("image/*"); // Filtra solo imágenes
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            ImageView imageView = findViewById(R.id.img);
            // Mostrar la imagen seleccionada en un ImageView
            runOnUiThread(() -> {

                imageView.setImageURI(imageUri);
                imageView.setVisibility(VISIBLE);
            });
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageView.setImageBitmap(bitmap);
                String base64 = bitToBase64(bitmap);

                this.imagen = new Imagen();
                this.imagen.setImagen(base64);
                Conex<Imagen> con = new Conex<>(GlobalData.path + "imagen", Imagen.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            // Si necesitas la ruta del archivo, puedes obtenerla con content resolver
        }
    }

    private String bitToBase64(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        // Comprimir el Bitmap a formato JPEG o PNG
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        // Convertir los bytes de la imagen a Base64
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public void guardar(View view){
        int idAlimento = Integer.parseInt(((Spinner)findViewById(R.id.alimento)).getSelectedItem().toString().split(". ")[0]);
        this.imagen.setId_alimento(idAlimento);

        if(!this.isAlimentoImage(idAlimento)){
            Conex<Imagen> con = new Conex<>(GlobalData.path + "/imagen", Imagen.class);
            con.insert(this.imagen, new Callback<InsertResponse>() {
                @Override
                public void onSuccess(InsertResponse result) {
                    runOnUiThread(() -> {
                        Toast.makeText(view.getContext(), "IMAGEN GUARDADA", Toast.LENGTH_LONG).show();
                    });
                }

                @Override
                public void onError(Exception e) {
                    runOnUiThread(() -> {
                        Toast.makeText(view.getContext(), "ERROR", Toast.LENGTH_LONG).show();
                    });
                    Log.e("IMAGEN ERROR", e.getMessage());
                }
            });
        }else{
            Conex<Imagen> con = new Conex<>(GlobalData.path + "/imagen/update",Imagen.class);
            con.update(this.imagen, new Callback<ResponseEntity<Boolean>>() {
                @Override
                public void onSuccess(ResponseEntity<Boolean> result) {
                    runOnUiThread(() -> {
                        Toast.makeText(view.getContext(), "Imagen actualizada", Toast.LENGTH_SHORT).show();
                    });
                }

                @Override
                public void onError(Exception e) {
                    runOnUiThread(() -> {
                        Toast.makeText(view.getContext(), "Error al actualizar", Toast.LENGTH_SHORT).show();
                    });
                    Log.e("IMAGEN ERROR", e.getMessage());
                }
            });
        }
    }

    private Bitmap convertBase64ToBitmap(String base64){
        try{
//            base64 = "data:image/jpeg;base64," + base64;
            byte[] decodedBytes = Base64.decode(base64, Base64.DEFAULT);
            // Convertir los bytes a un Bitmap
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;

            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length,options);
        }catch(Exception e){
            runOnUiThread(() -> {
                Toast.makeText(this, "Error al convertir", Toast.LENGTH_SHORT).show();
            });
        }
        return null;
    }

    private boolean isAlimentoImage(int idAlimento){
        for(Imagen imagen: this.imagenes){
            if(idAlimento == imagen.getId_alimento()){
                return true;
            }
        }
        return false;
    }
}