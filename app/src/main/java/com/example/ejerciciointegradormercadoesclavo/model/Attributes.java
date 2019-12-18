package com.example.ejerciciointegradormercadoesclavo.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Attributes implements Serializable {

    @SerializedName("name")
    private String nombreAtributo;
    @SerializedName("value_name")
    private String detalleAtributo;

    public Attributes(String nombreAtributo, String detalleAtributo) {
        this.nombreAtributo = nombreAtributo;
        this.detalleAtributo = detalleAtributo;
    }

    public Attributes() {
    }

    public String getNombreAtributo() {
        return nombreAtributo;
    }

    public String getDetalleAtributo() {
        return detalleAtributo;
    }

}
