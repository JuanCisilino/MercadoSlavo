package com.example.ejerciciointegradormercadoesclavo.controller;

import com.example.ejerciciointegradormercadoesclavo.model.Articulo;
import com.example.ejerciciointegradormercadoesclavo.model.FirestoreDao;
import com.example.ejerciciointegradormercadoesclavo.utils.ResultListener;

import java.util.List;

public class FirestoreController {

    private FirestoreDao firestoreDao;

    public FirestoreController() {
        firestoreDao = new FirestoreDao();
    }

    public void agregarArticuloAFav(Articulo articulo){
        firestoreDao.agregarArticuloAFav(articulo);
    }

    public void traerListaDeFavorito(final ResultListener<List<Articulo>> listenerDeLaVista){
        firestoreDao.traerArticulosFavoritos(new ResultListener<List<Articulo>>() {
            @Override
            public void finish(List<Articulo> result) {
                listenerDeLaVista.finish(result);
            }
        });
    }
}
