package Services;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import models.Callback;
import models.InsertResponse;
import models.ResponseEntity;
import models.Usuario;

public class Conex<T>{
    private String path;
    private URL url;
    private Gson gson;
    private HttpURLConnection conn;
    private Class<T> clase;

    public Conex(String path, Class<T> clase) {
        try{
            this.clase = clase;
            this.path = path;
            this.url = new URL(this.path);
            this.conn = (HttpURLConnection) url.openConnection();
            this.gson = new Gson();
        }catch(Exception e){
            Log.d("Error en conexion","Error en Conex: " + e.getMessage());
        }
    }

    public void getAll(Callback<List<T>> callback){
        new Thread(() -> {
            try{
                if(this.conn == null)throw new RuntimeException("Sin conexión");

                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json");
                int responseCode = conn.getResponseCode();
                if(responseCode != HttpURLConnection.HTTP_OK) throw new RuntimeException(
                        "Petición no realizada: " + responseCode
                );

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while((inputLine = in.readLine()) != null){
                    response.append(inputLine);
                }
                in.close();
                ResponseEntity<LinkedTreeMap> resp = this.gson.fromJson(response.toString(), ResponseEntity.class);
                List<T> datos = new ArrayList<>();
                for(LinkedTreeMap row: resp.getData()){
                    datos.add(gson.fromJson(gson.toJson(row),this.clase));
                }

                callback.onSuccess(datos);
            }catch(Exception e){
                callback.onError(e);
            }
        }).start();
    }

    public void insert(T obj, Callback<InsertResponse> callback){
        new Thread(() -> {
            try{


                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                String json = this.gson.toJson(obj);

                Log.d("LOTE DATA", json);

                Log.d("POST DATA", json);
                try(OutputStream os = conn.getOutputStream()){
                    os.write(json.getBytes());
                    os.flush();
                }

                int respCode = conn.getResponseCode();
                if(respCode != HttpURLConnection.HTTP_OK && respCode != HttpURLConnection.HTTP_CREATED)throw new RuntimeException(conn.getResponseMessage());

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while((inputLine = in.readLine()) != null){
                    response.append(inputLine);
                }

                in.close();
                String ddd = response.toString();
                Log.d("DATOS AA", response.toString());
                InsertResponse resp = this.gson.fromJson(response.toString(), InsertResponse.class);
                callback.onSuccess(resp);

            }catch(Exception e){
                callback.onError(e);
            }
        }).start();
    }

    public void update(T obj, Callback<ResponseEntity<Boolean>> callback){
        new Thread(() -> {
            try{
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                String json = this.gson.toJson(obj);
                try(OutputStream os = conn.getOutputStream()){
                    os.write(json.getBytes());
                    os.flush();
                }

                int respCode = conn.getResponseCode();
                if(respCode != HttpURLConnection.HTTP_OK && respCode != HttpURLConnection.HTTP_CREATED)throw new RuntimeException("No se pudo actualizar: " + respCode);

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while((inputLine = in.readLine()) != null){
                    response.append(inputLine);
                }

                in.close();
                ResponseEntity<Boolean> resp = this.gson.fromJson(response.toString(), ResponseEntity.class);
                callback.onSuccess(resp);
            }catch(Exception e){
                callback.onError(e);
            }
        }).start();
    }

    public void login(String username, String password, Callback<Usuario> callback){
        new Thread(() -> {
            try{
                LoginHelper helper = new LoginHelper(username,password);

                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                String json = this.gson.toJson(helper);
                Log.d("POST DATA", json);
                try(OutputStream os = conn.getOutputStream()){
                    os.write(json.getBytes());
                    os.flush();
                }

                int respCode = conn.getResponseCode();
                if(respCode != HttpURLConnection.HTTP_OK)throw new RuntimeException("Error en login: " + respCode);

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while((inputLine = in.readLine()) != null){
                    response.append(inputLine);
                }

                in.close();
                Log.d("DATA USUARIO",response.toString());
                ResponseEntity<LinkedTreeMap> data = this.gson.fromJson(response.toString(), ResponseEntity.class);

                Usuario usuario = gson.fromJson(gson.toJson(data.getData().get(0)), Usuario.class);

                callback.onSuccess(usuario);
            }catch(Exception e){
                callback.onError(e);
            }
        }).start();
    }

    private class LoginHelper{
        String username;
        String password;

        public LoginHelper(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }
}


