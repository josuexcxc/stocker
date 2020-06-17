package com.example.login;

public class StockVo {
    private String nombre="";
    private String descripcion="";
    private String stock="";

    public StockVo(String nombre, String descripcion, String stock) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.stock = stock;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }
}
