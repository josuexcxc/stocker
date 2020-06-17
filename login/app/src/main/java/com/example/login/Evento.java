package com.example.login;

public class Evento {
    private String clave_orden;
    private String titulo;
    private String fecha;
    private String Cliente;

    public Evento(String clave_orden, String titulo, String fecha, String cliente) {
        this.clave_orden = clave_orden;
        this.titulo = titulo;
        this.fecha = fecha;
        Cliente = cliente;
    }

    public String getClave_orden() {
        return clave_orden;
    }

    public void setClave_orden(String clave_orden) {
        this.clave_orden = clave_orden;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getCliente() {
        return Cliente;
    }

    public void setCliente(String cliente) {
        Cliente = cliente;
    }
}
