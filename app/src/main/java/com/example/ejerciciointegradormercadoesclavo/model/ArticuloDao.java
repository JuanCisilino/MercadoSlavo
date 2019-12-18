package com.example.ejerciciointegradormercadoesclavo.model;

import android.util.Log;

import com.example.ejerciciointegradormercadoesclavo.utils.ResultListener;
import com.example.ejerciciointegradormercadoesclavo.view.AdapterArticulo;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticuloDao extends RetrofitDaoArticulos {

    public static final String BASE_URL = "https://api.mercadolibre.com/";
    public static final String ARTICULOSELECCIONADO = "Producto";
    private List<Articulo> listaDeFavoritos = new ArrayList<>();


    public ArticuloDao() {
        super(BASE_URL);
    }

    public void traerListaDeArticulos(String ARTICULOSELECCIONADO, final ResultListener<List<Articulo>> listenerDelControler) {

        Call<ContenedorArticulo> call = serviceArticulo.traeListadeArticulos(ARTICULOSELECCIONADO);

        call.enqueue(new Callback<ContenedorArticulo>() {
            @Override
            public void onResponse(Call<ContenedorArticulo> call, Response<ContenedorArticulo> response) {
                ContenedorArticulo contenedorArticulo = response.body();
                listenerDelControler.finish(contenedorArticulo.getResults());
            }

            @Override
            public void onFailure(Call<ContenedorArticulo> call, Throwable t) {

            }
        });
    }

    public void traerArticulo(String ARTICULOSELECCIONADO, final ResultListener<Articulo> listenerDelControler) {

        Call<Articulo> llamada = serviceArticulo.traerArticuloPorId(ARTICULOSELECCIONADO);

        llamada.enqueue(new Callback<Articulo>() {
            @Override
            public void onResponse(Call<Articulo> call, Response<Articulo> response) {
                Articulo articulo = response.body();
                listenerDelControler.finish(articulo);
            }

            @Override
            public void onFailure(Call<Articulo> call, Throwable t) {

            }
        });
    }

    public void traerDescripcion(String ARTICULOSELECCIONADO, final ResultListener<Descriptions> listenerDelControler) {

        Call<List<Descriptions>> llamada = serviceArticulo.traerDescripcionPorId(ARTICULOSELECCIONADO);

        llamada.enqueue(new Callback<List<Descriptions>>() {
            @Override
            public void onResponse(Call<List<Descriptions>> call, Response<List<Descriptions>> response) {
                Descriptions descriptions = response.body().get(0);
                listenerDelControler.finish(descriptions);
            }

            @Override
            public void onFailure(Call<List<Descriptions>> call, Throwable t) {
                Log.d("Hola", "Holas");
            }
        });
    }

    public void traerArticulosFavoritos(ResultListener<List<Articulo>> listenerFavoritos){

        listenerFavoritos.finish(listaDeFavoritos);
    }
}
