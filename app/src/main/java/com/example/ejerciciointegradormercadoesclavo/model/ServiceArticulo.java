package com.example.ejerciciointegradormercadoesclavo.model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ServiceArticulo {

    @GET("sites/MLA/search")
    Call<ContenedorArticulo> traeListadeArticulos(@Query("q") String nombreProducto);

    @GET("items/{articulo_id}")
    Call<Articulo> traerArticuloPorId(@Path("articulo_id") String articulo_id);

    @GET("items/{articulo_id}/descriptions")
    Call<List<Descriptions>> traerDescripcionPorId(@Path("articulo_id") String articulo_id);



}
