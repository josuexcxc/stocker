package com.example.login;

public class ClienteVo {
    private String clave_cliente="";
    private String nombre="";
    private String app="";

    public ClienteVo(String clave_cliente, String nombre, String app) {
        this.clave_cliente = clave_cliente;
        this.nombre = nombre;
        this.app = app;
    }

    public String getClave_cliente() {
        return clave_cliente;
    }

    public void setClave_cliente(String clave_cliente) {
        this.clave_cliente = clave_cliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }
}
