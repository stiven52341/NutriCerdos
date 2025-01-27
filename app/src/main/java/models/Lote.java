package models;

public class Lote {
    private short id;
    private String nombre;
    private short id_tipo;
    private int cantidad_cerdos;
    private String fecha_creacion;
    private boolean estado;

    public Lote() {
    }

    public Lote(short id, String nombre, short id_tipo, int cantidad_cerdos, String fecha_creacion, boolean estado) {
        this.id = id;
        this.nombre = nombre;
        this.id_tipo = id_tipo;
        this.cantidad_cerdos = cantidad_cerdos;
        this.fecha_creacion = fecha_creacion;
        this.estado = estado;
    }

    public short getId() {
        return id;
    }

    public void setId(short id) {
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

    public int getCantidad_cerdos() {
        return cantidad_cerdos;
    }

    public void setCantidad_cerdos(int cantidad_cerdos) {
        this.cantidad_cerdos = cantidad_cerdos;
    }

    public String getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(String fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
