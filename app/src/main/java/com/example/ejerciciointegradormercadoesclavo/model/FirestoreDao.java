package com.example.ejerciciointegradormercadoesclavo.model;

import androidx.annotation.NonNull;

import com.example.ejerciciointegradormercadoesclavo.utils.ResultListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class FirestoreDao {

    public static final String ARTICULOS_FAVORITOS = "articulosfavoritos";
    private FirebaseFirestore firestore;
    private FirebaseUser currentUser;
    private ContenedorArticulo contenedorArticulo;

    public FirestoreDao() {
        firestore = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        traerArticulosFavoritos();
    }

    public void agregarArticuloAFav(Articulo articulo){
        if (currentUser == null)
            return;
        if (contenedorArticulo.contieneArticulo(articulo)){
            contenedorArticulo.removerPelicula(articulo);
        }
        else {
            contenedorArticulo.agregarPelicula(articulo);
        }
        firestore.collection(ARTICULOS_FAVORITOS)
                .document(currentUser.getUid())
                .set(contenedorArticulo);
    }

    private void traerArticulosFavoritos() {
        if (currentUser== null){
            contenedorArticulo = new ContenedorArticulo();
            return;
        }
        firestore.collection(ARTICULOS_FAVORITOS)
                .document(currentUser.getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            //en el listener del on succes intento tranfomar el documento a un container
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                contenedorArticulo = documentSnapshot.toObject(ContenedorArticulo.class);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    //en el on failure del listener inicializo un container vacio
                    public void onFailure(@NonNull Exception e) {
                        contenedorArticulo = new ContenedorArticulo();
                    }
                });
    }

    public void traerArticulosFavoritos(final ResultListener<List<Articulo>> listenerDelController){
        if (currentUser== null){
            contenedorArticulo = new ContenedorArticulo();
            listenerDelController.finish(contenedorArticulo.getResults());
            return;
        }
        firestore.collection(ARTICULOS_FAVORITOS)
                .document(currentUser.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            //en el listener del on succes intento tranfomar el documento a un container
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                contenedorArticulo = documentSnapshot.toObject(ContenedorArticulo.class);
                if (contenedorArticulo == null) {
                    contenedorArticulo = new ContenedorArticulo();}
                    listenerDelController.finish(contenedorArticulo.getResults());
                }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    //en el on failure del listener inicializo un container vacio
                    public void onFailure(@NonNull Exception e) {
                        contenedorArticulo = new ContenedorArticulo();
                        //ademas de actualizar la lista se lo doy a la vista que lo va a necesitar
                        listenerDelController.finish(contenedorArticulo.getResults());
                    }
                });
    }
}
