package com.example.ejerciciointegradormercadoesclavo.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ArticuloRoomDao {

    @Query("SELECT * FROM articulo")
    List<Articulo> getAllArticulos();

    @Insert
    void insertArticulo(Articulo... articulos);

    @Insert
    void insertArticulo(List<Articulo> articuloList);

    @Query("DELETE FROM articulo")
    void deleteAll();
}
