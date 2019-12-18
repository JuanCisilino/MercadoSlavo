package com.example.ejerciciointegradormercadoesclavo.model;

public class Usuario {

    private String Nombre, Apellido;
    private String imagenUrl;
    public Usuario() {
    }

    public Usuario(String nombre, String apellido, String imagenUrl) {
        Nombre = nombre;
        Apellido = apellido;
        imagenUrl = imagenUrl;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String apellido) {
        Apellido = apellido;
    }
}
