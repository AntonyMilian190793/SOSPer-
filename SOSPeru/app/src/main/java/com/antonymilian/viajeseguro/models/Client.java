package com.antonymilian.viajeseguro.models;

public class Client {

    String id;
    String image;
    String nombres;
    String apellidos;
    String correo;
    String movil;
    String dni;

    public Client(){
    }

    public Client(String id, String name, String dni) {
        this.id = id;
        this.nombres = name;
        this.dni = dni;
    }

    public Client(String id, String nombres, String apellidos, String correo, String movil, String dni) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
        this.movil = movil;
        this.dni = dni;
    }

    public Client(String id, String nombres, String dni, String image) {
        this.id = id;
        this.nombres = nombres;
        this.dni = dni;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getMovil() {
        return movil;
    }

    public void setMovil(String movil) {
        this.movil = movil;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }
}
