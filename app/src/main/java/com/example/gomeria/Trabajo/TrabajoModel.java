package com.example.gomeria.Trabajo;

public class TrabajoModel {
    int id;
    String descripcion;
    String foto; //url_image
    float precio;

    public TrabajoModel(int id, String descripcion, String foto, float precio) {
        this.id = id;
        this.descripcion = descripcion;
        this.foto = foto;
        this.precio = precio;
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

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }
}
