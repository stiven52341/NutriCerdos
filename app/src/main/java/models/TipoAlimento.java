package models;

public class TipoAlimento {
    private short id;
    private String descr;
    private boolean estado;

    public TipoAlimento() {
    }

    public TipoAlimento(short id, String descr, boolean estado) {
        this.id = id;
        this.descr = descr;
        this.estado = estado;
    }

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
