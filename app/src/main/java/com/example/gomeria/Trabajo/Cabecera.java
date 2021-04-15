package com.example.gomeria.Trabajo;

import com.google.firebase.database.Exclude;
import java.util.HashMap;
import java.util.Map;

public class Cabecera {
    int id;
    String fecha;
    String auto;

    public Cabecera(int id, String fecha, String auto) {
        this.id = id;
        this.fecha = fecha;
        this.auto = auto;
    }

    @Exclude
    public Map<String, Object> Cabecera() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("fecha", fecha);
        result.put("auto", auto);
        return result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getAuto() {
        return auto;
    }

    public void setAuto(String auto) {
        this.auto = auto;
    }


}
