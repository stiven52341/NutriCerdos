package models;

public class TipoLote {
    private short id;
    private String descr;
    private boolean estado;

    public TipoLote() {
    }

    public TipoLote(short id, String descr, boolean estado) {
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
