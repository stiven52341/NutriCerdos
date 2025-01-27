package models;

import java.sql.Timestamp;

public class Cerdo {
    private int id;
    private short id_lote;
    private Timestamp fecha_registro;
    private short id_etapa_vida;
    private short id_estado;

    public Cerdo() {
    }

    public Cerdo(int id, short id_lote, Timestamp fecha_registro, short id_etapa_vida, short id_estado) {
        this.id = id;
        this.id_lote = id_lote;
        this.fecha_registro = fecha_registro;
        this.id_etapa_vida = id_etapa_vida;
        this.id_estado = id_estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public short getId_lote() {
        return id_lote;
    }

    public void setId_lote(short id_lote) {
        this.id_lote = id_lote;
    }

    public Timestamp getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(Timestamp fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public short getId_etapa_vida() {
        return id_etapa_vida;
    }

    public void setId_etapa_vida(short id_etapa_vida) {
        this.id_etapa_vida = id_etapa_vida;
    }

    public short getId_estado() {
        return id_estado;
    }

    public void setId_estado(short id_estado) {
        this.id_estado = id_estado;
    }
}
