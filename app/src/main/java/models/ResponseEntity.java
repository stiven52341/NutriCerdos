package models;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ResponseEntity<T>{
    private List<T> data;
    private boolean success;

    public ResponseEntity() {
        this.data = data;
    }

    public ResponseEntity(List<T> data, boolean success) {
        this.data = data;
        this.success = success;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
