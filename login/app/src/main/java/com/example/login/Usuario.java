package com.example.login;

public class Usuario {
    private String clave_usuario="";
    private String usuario="";
    private String password="";
    private String img_path="";
    private String clave_tipo="";
    private String nombre="";
    private String appaterno="";

    public Usuario(String clave_usuario, String usuario, String password, String img_path, String clave_tipo, String nombre, String appaterno) {
        this.clave_usuario = clave_usuario;
        this.usuario = usuario;
        this.password = password;
        this.img_path = img_path;
        this.clave_tipo = clave_tipo;
        this.nombre = nombre;
        this.appaterno = appaterno;
    }

    public String getClave_usuario() {
        return clave_usuario;
    }

    public void setClave_usuario(String clave_usuario) {
        this.clave_usuario = clave_usuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public String getClave_tipo() {
        return clave_tipo;
    }

    public void setClave_tipo(String clave_tipo) {
        this.clave_tipo = clave_tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAppaterno() {
        return appaterno;
    }

    public void setAppaterno(String appaterno) {
        this.appaterno = appaterno;
    }
}

