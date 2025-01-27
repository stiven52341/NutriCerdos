package models;

public class Imagen {
    private int id_alimento;
    private String imagen;

    public Imagen() {
    }

    public Imagen(int id_alimento, String imagen) {
        this.id_alimento = id_alimento;
        this.imagen = imagen;
    }

    public int getId_alimento() {
        return id_alimento;
    }

    public void setId_alimento(int id_alimento) {
        this.id_alimento = id_alimento;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
