package models;

public interface Callback<T> {
    void onSuccess(T result);
    void onError(Exception e);
}
