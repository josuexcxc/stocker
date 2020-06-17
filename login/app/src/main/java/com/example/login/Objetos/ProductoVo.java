package com.example.login.Objetos;

public class ProductoVo {
    private String clave_producto="";
    private String nombre="";
    private String presentacion="";
    private String precio="";
    private String stock="";
    private String stock_max="";
    private String cantidad ="";

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getClave_producto() {
        return clave_producto;
    }

    public void setClave_producto(String clave_producto) {
        this.clave_producto = clave_producto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getStock_max() {
        return stock_max;
    }

    public void setStock_max(String stock_max) {
        this.stock_max = stock_max;
    }
}
