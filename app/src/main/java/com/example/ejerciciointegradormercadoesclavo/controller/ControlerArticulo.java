package com.example.ejerciciointegradormercadoesclavo.controller;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.ejerciciointegradormercadoesclavo.config.AppDataBase;
import com.example.ejerciciointegradormercadoesclavo.model.Articulo;
import com.example.ejerciciointegradormercadoesclavo.model.ArticuloDao;
import com.example.ejerciciointegradormercadoesclavo.model.ArticuloRoomDao;
import com.example.ejerciciointegradormercadoesclavo.model.Attributes;
import com.example.ejerciciointegradormercadoesclavo.model.Descriptions;
import com.example.ejerciciointegradormercadoesclavo.utils.ResultListener;

import java.util.List;

public class ControlerArticulo {

    private ArticuloRoomDao articuloRoomDao;
    private ArticuloDao articuloDao;
    private Context context;
    private Integer paginaActual = 1;

    public ControlerArticulo(Context context) {
        this.articuloDao = new ArticuloDao();
        this.articuloRoomDao = AppDataBase.getInstance(context).articuloRoomDao();
        this.context = context;
    }

    public void traerListaDeArticulos(String consulta, final ResultListener<List<Articulo>> listenerDeLaVista) {
       if (hayInternet(context)) {
           articuloDao.traerListaDeArticulos(consulta, new ResultListener<List<Articulo>>() {
               @Override
               public void finish(List<Articulo> result) {
                    articuloRoomDao.deleteAll();
                    articuloRoomDao.insertArticulo(result);
                    listenerDeLaVista.finish(result);
               }
           });
       } else {
           List<Articulo> articuloList = articuloRoomDao.getAllArticulos();
           listenerDeLaVista.finish(articuloList);
       }
    }

    public void traeUltimaBusqueda(final ResultListener<List<Articulo>> listenerDeLaVista) {
        List<Articulo> articuloList = articuloRoomDao.getAllArticulos();
        listenerDeLaVista.finish(articuloList);
    }

    public void traerArticulo(String articuloSeleccionado, final ResultListener<Articulo> listenerDeLaVista) {
        articuloDao.traerArticulo(articuloSeleccionado, new ResultListener<Articulo>() {
            @Override
            public void finish(Articulo resultarticulo) {
                listenerDeLaVista.finish(resultarticulo);
            }
        });
    }

    public void traerDescripcion(String articuloSeleccionado, final ResultListener<Descriptions> listenerDeLaVista) {
        articuloDao.traerDescripcion(articuloSeleccionado, new ResultListener<Descriptions>() {
            @Override
            public void finish(Descriptions resultarticulo) {
                listenerDeLaVista.finish(resultarticulo);

            }
        });
    }

    public void traerListaDeFavoritos(final ResultListener listenerDeLaVista) {
        articuloDao.traerArticulosFavoritos(new ResultListener<List<Articulo>>() {
            @Override
            public void finish(List<Articulo> result) {
                listenerDeLaVista.finish(result);
            }
        });
    }

    public Boolean hayInternet(Context context){

        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }
}
