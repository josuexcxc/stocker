package com.example.login;

public class Insumo {
    private String id_insumo;
    private String descripcion;
    private String Unidad;
    private String cant_unidad;
    private String cantidad;

    public Insumo(String id_insumo, String descripcion, String unidad, String cant_unidad, String cantidad) {
        this.id_insumo = id_insumo;
        this.descripcion = descripcion;
        Unidad = unidad;
        this.cant_unidad = cant_unidad;
        this.cantidad = cantidad;
    }

    public String getId_insumo() {
        return id_insumo;
    }

    public void setId_insumo(String id_insumo) {
        this.id_insumo = id_insumo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUnidad() {
        return Unidad;
    }

    public void setUnidad(String unidad) {
        Unidad = unidad;
    }

    public String getCant_unidad() {
        return cant_unidad;
    }

    public void setCant_unidad(String cant_unidad) {
        this.cant_unidad = cant_unidad;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }
}
