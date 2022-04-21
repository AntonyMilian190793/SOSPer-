package com.antonymilian.viajeseguro.models;

public class Driver {

    String id;
    String name;
    String email;
    String marcaVehiculo;
    String placaVehiculo;

    public Driver(String id, String name, String email, String marcaVehiculo, String placaVehiculo) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.marcaVehiculo = marcaVehiculo;
        this.placaVehiculo = placaVehiculo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMarcaVehiculo() {
        return marcaVehiculo;
    }

    public void setMarcaVehiculo(String marcaVehiculo) {
        this.marcaVehiculo = marcaVehiculo;
    }

    public String getPlacaVehiculo() {
        return placaVehiculo;
    }

    public void setPlacaVehiculo(String placaVehiculo) {
        this.placaVehiculo = placaVehiculo;
    }
}
