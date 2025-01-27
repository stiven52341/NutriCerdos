package models;

public class Alimento {
    private int id;
    private String nombre;
    private short id_tipo;

    public Alimento() {
    }

    public Alimento(int id, String nombre, short id_tipo) {
        this.id = id;
        this.nombre = nombre;
        this.id_tipo = id_tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public short getId_tipo() {
        return id_tipo;
    }

    public void setId_tipo(short id_tipo) {
        this.id_tipo = id_tipo;
    }
}
