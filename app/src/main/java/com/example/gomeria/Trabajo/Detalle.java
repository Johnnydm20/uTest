package com.example.gomeria.Trabajo;

import com.google.firebase.database.Exclude;
import java.util.HashMap;
import java.util.Map;

public class Detalle {
    int id;
    String descripcion;
    float precio;

    public Detalle(int id, String descripcion, float precio) {
        this.id = id;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    public Map<String, Object> Detalle() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("descripcion", descripcion);
        result.put("precio", precio);
        return result;
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

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }
}
