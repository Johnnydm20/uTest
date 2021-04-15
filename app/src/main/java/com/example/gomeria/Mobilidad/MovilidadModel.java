package com.example.gomeria.Mobilidad;

public class MovilidadModel {
    int id;
    String descripcion;
    String foto; //url_image

    public MovilidadModel(int id, String descripcion, String foto) {
        this.id = id;
        this.descripcion = descripcion;
        this.foto = foto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
