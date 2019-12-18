package com.example.ejerciciointegradormercadoesclavo.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity(tableName = "articulo")
public class Articulo implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    private Integer idArticulo;
    @ColumnInfo
    @SerializedName("title")
    private String nombreDeArticulo;
    @ColumnInfo
    @SerializedName("price")
    private String precioDeArticulo;
    @ColumnInfo
    @SerializedName("thumbnail")
    private String imagen;

    @Ignore
    @SerializedName("attributes")
    private List<Attributes> atributo;
    @Ignore
    @SerializedName("pictures")
    private List<Pictures> imagenes;
    @Ignore
    @SerializedName("geolocation")
    private Geolocation location;
    @Ignore
    private String id;
    @Ignore
    private String descripcionDeArticulo;
    @Ignore
    private String condition;
    @Ignore
    private Paging paging;
    @Ignore
    private Descriptions descripcion;

    public Articulo(String nombreDeArticulo, String imagen, String precioDeArticulo) {
        this.nombreDeArticulo = nombreDeArticulo;
        this.imagen = imagen;
        this.precioDeArticulo = precioDeArticulo;
    }

    @Ignore
    public Articulo(){

    }

    public Integer getIdArticulo() {
        return idArticulo;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setIdArticulo(Integer idArticulo) {
        this.idArticulo = idArticulo;
    }

    public void setNombreDeArticulo(String nombreDeArticulo) {
        this.nombreDeArticulo = nombreDeArticulo;
    }

    public void setPrecioDeArticulo(String precioDeArticulo) {
        this.precioDeArticulo = precioDeArticulo;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public void setAtributo(List<Attributes> atributo) {
        this.atributo = atributo;
    }

    public void setImagenes(List<Pictures> imagenes) {
        this.imagenes = imagenes;
    }

    public void setLocation(Geolocation location) {
        this.location = location;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDescripcionDeArticulo(String descripcionDeArticulo) {
        this.descripcionDeArticulo = descripcionDeArticulo;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    public void setDescripcion(Descriptions descripcion) {
        this.descripcion = descripcion;
    }

    public Geolocation getLocation() {
        return location;
    }

    public List<Attributes> getAtributo() {
        return atributo;
    }

    public String getId() {
        return id;
    }


    public String getNombreDeArticulo() {
        return nombreDeArticulo;
    }

    public String getDescripcionDeArticulo() {
        return descripcionDeArticulo;
    }

    public String getPrecioDeArticulo() {
        return precioDeArticulo;
    }

    public String getImagen() {
        return imagen;
    }

    public String getCondition() {
        return condition;
    }

    public List<Pictures> getImagenes() {
        return imagenes;
    }

    public Descriptions getDescripcion() {
        return descripcion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Articulo articulo = (Articulo) o;
        return Objects.equals(nombreDeArticulo, articulo.nombreDeArticulo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombreDeArticulo);
    }
}
