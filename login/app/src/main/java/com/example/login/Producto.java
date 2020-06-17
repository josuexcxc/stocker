package com.example.login;

public class Producto {
    private String id;
    private String descripcion;
    private String presentacion;
    private String cantidad;

    public Producto(String id, String descripcion, String presentacion, String cantidad) {
        this.id = id;
        this.descripcion = descripcion;
        this.presentacion = presentacion;
        this.cantidad = cantidad;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }
}