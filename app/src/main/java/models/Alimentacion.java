package models;

import java.sql.Timestamp;

public class Alimentacion {
    private int id;
    private int id_cerdo;
    private short id_dieta;
    private String fecha;

    public Alimentacion() {
    }

    public Alimentacion(int id, int id_cerdo, short id_dieta, String fecha) {
        this.id = id;
        this.id_cerdo = id_cerdo;
        this.id_dieta = id_dieta;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_cerdo() {
        return id_cerdo;
    }

    public void setId_cerdo(int id_cerdo) {
        this.id_cerdo = id_cerdo;
    }

    public short getId_dieta() {
        return id_dieta;
    }

    public void setId_dieta(short id_dieta) {
        this.id_dieta = id_dieta;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
