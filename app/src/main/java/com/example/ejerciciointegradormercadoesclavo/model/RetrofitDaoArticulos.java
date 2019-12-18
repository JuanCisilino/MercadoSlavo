package com.example.ejerciciointegradormercadoesclavo.model;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitDaoArticulos {

    private Retrofit retrofit;
    protected ServiceArticulo serviceArticulo;

    public RetrofitDaoArticulos(String baseURL) {

        retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        serviceArticulo = retrofit.create(ServiceArticulo.class);

    }
}
