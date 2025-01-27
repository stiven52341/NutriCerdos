package models;

public class Usuario {
    private int id;
    private String nombre;
    private String ocupacion;
    private boolean estado;
    private String username;
    private String password;

    public Usuario() {
    }

    public Usuario(int id, String nombre, String ocupacion, boolean estado, String username, String password) {
        this.id = id;
        this.nombre = nombre;
        this.ocupacion = ocupacion;
        this.estado = estado;
        this.username = username;
        this.password = password;
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

    public String getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
