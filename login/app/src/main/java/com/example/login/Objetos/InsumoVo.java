package com.example.login.Objetos;

public class InsumoVo {
    private String clave_insumo = "";
    private String nombre = "";
    private String unida = "";
    private String cantidad = "";
    private String stock = "";
    private String stock_max= "";
    private String cantidad_r = "";

    public String getCantidad_r() {
        return cantidad_r;
    }

    public void setCantidad_r(String cantidad_r) {
        this.cantidad_r = cantidad_r;
    }

    public String getClave_insumo() {
        return clave_insumo;
    }

    public void setClave_insumo(String clave_insumo) {
        this.clave_insumo = clave_insumo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUnida() {
        return unida;
    }

    public void setUnida(String unida) {
        this.unida = unida;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
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
