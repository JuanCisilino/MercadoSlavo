package com.example.ejerciciointegradormercadoesclavo.model;

import java.util.ArrayList;
import java.util.List;

public class ContenedorArticulo {

    private Integer page;
    private Integer total_pages;
    private List<Articulo> results;

    public Integer getPage() {
        return page;
    }

    public Integer getTotal_pages() {
        return total_pages;
    }

    public List<Articulo> getResults() {
        return results;
    }

    public ContenedorArticulo() {
        results = new ArrayList<>();
    }

    public void setResults (List<Articulo> resultsList) {
        this.results = resultsList;
    }

    public void agregarPelicula(Articulo articulo){
        results.add(articulo);
    }
    public void removerPelicula(Articulo articulo){ results.remove(articulo);
    }

    public Boolean contieneArticulo(Articulo articulo){
        return results.contains(articulo);
    }
}
