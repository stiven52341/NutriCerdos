package models;

public class AlimentoUnidad {
    private short id_unidad;
    private int id_alimento;
    private float precio;
    private float disponible;
    private boolean estado;

    public AlimentoUnidad() {
    }

    public AlimentoUnidad(short id_unidad, int id_alimento, float precio, float disponible, boolean estado) {
        this.id_unidad = id_unidad;
        this.id_alimento = id_alimento;
        this.precio = precio;
        this.disponible = disponible;
        this.estado = estado;
    }

    public short getId_unidad() {
        return id_unidad;
    }

    public void setId_unidad(short id_unidad) {
        this.id_unidad = id_unidad;
    }

    public int getId_alimento() {
        return id_alimento;
    }

    public void setId_alimento(int id_alimento) {
        this.id_alimento = id_alimento;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public float getDisponible() {
        return disponible;
    }

    public void setDisponible(float disponible) {
        this.disponible = disponible;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
