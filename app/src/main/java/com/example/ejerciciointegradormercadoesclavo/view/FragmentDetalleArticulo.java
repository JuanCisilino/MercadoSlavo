package com.example.ejerciciointegradormercadoesclavo.view;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ejerciciointegradormercadoesclavo.R;
import com.example.ejerciciointegradormercadoesclavo.controller.ControlerArticulo;
import com.example.ejerciciointegradormercadoesclavo.controller.FirestoreController;
import com.example.ejerciciointegradormercadoesclavo.model.Articulo;
import com.example.ejerciciointegradormercadoesclavo.model.Descriptions;
import com.example.ejerciciointegradormercadoesclavo.model.Pictures;
import com.example.ejerciciointegradormercadoesclavo.utils.ResultListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDetalleArticulo extends Fragment implements AdapterAtributo.ListenerDelAdapter,
        AdapterImagen.ListenerDelAdapter, AdapterFavoritos.ListenerDelAdapter {

    public static final String CLAVE_ARTICULO = "claveArticulo";

    @BindView(R.id.fragment_detalle_textView_condicion)
    TextView textViewCondition;
    @BindView(R.id.fragment_detalle_textView_descripcion)
    TextView textViewDescripcion;
    @BindView(R.id.fragment_detalle_textview_nombre_articulo)
    TextView textViewTitulo;
    @BindView(R.id.fragment_detalle_textView_precio_articulo)
    TextView textViewPrecio;
    @BindView(R.id.fragment_detalle_recycler)
    RecyclerView recyclerdescripcion;
    @BindView(R.id.fragment_detalle_imagen_celda_articulo)
    RecyclerView imagenArticulo;
    @BindView(R.id.fragment_detalle_floatingShare)
    FloatingActionButton floatingLocation;
    @BindView(R.id.fragment_detalle_floatingFav)
    FloatingActionButton floatingFav;
    private View view;
    private Articulo articuloSeleccionado;
    private AdapterAtributo adapterAtributo;
    private AdapterFavoritos adapterFavoritos;
    private ControlerArticulo controlerAtributo;
    private AdapterImagen adapterImagen;
    private Boolean esFavorita;
    private FirestoreController firestoreController;
    private FirebaseUser firebaseUser;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_detalle_articulo, container, false);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firestoreController = new FirestoreController();
        articuloSeleccionado = (Articulo) bundle.getSerializable(CLAVE_ARTICULO);
        controlerAtributo = new ControlerArticulo(getContext());
        adapterAtributo = new AdapterAtributo(this);
        adapterImagen = new AdapterImagen(this);
        adapterFavoritos = new AdapterFavoritos(this);

        controlerAtributo.traerArticulo(articuloSeleccionado.getId(), new ResultListener<Articulo>() {
            @Override
            public void finish(Articulo result) {
                articuloSeleccionado = result;
            }
        });
        controlerAtributo.traerDescripcion(articuloSeleccionado.getId(), new ResultListener<Descriptions>() {
            @Override
            public void finish(Descriptions result) {
                textViewDescripcion.setText(result.getPlain_text());
            }
        });

        floatingLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MapsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(MapsActivity.LONGITUD, articuloSeleccionado.getLocation().getLongitude());
                bundle.putString(MapsActivity.LATITUD, articuloSeleccionado.getLocation().getLatitude());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        floatingFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firebaseUser == null) {
                    Toast.makeText(getContext(), "por favor logueateee!!!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getContext(), ActivityLogin.class);
                    startActivity(intent);
                }
                firestoreController.agregarArticuloAFav(articuloSeleccionado);
                esFavorita = !esFavorita;
                actualizarFav();
            }
        });


        firestoreController.traerListaDeFavorito(new ResultListener<List<Articulo>>() {
            @Override
            public void finish(List<Articulo> result) {
                esFavorita = result.contains(articuloSeleccionado);
                actualizarFav();
                habilitarOnClickDeFav();
            }
        });

        traerlistas();
        setearVariables();

        recyclerdescripcion.setLayoutManager(new LinearLayoutManager(getContext(), recyclerdescripcion.HORIZONTAL, false));
        recyclerdescripcion.setAdapter(adapterAtributo);
        imagenArticulo.setLayoutManager(new LinearLayoutManager(getContext(), imagenArticulo.HORIZONTAL, false));
        imagenArticulo.setAdapter(adapterImagen);
        return view;
    }

    private void setearVariables() {
        textViewTitulo.setText(articuloSeleccionado.getNombreDeArticulo());
        textViewPrecio.setText("$" + articuloSeleccionado.getPrecioDeArticulo());
        if (articuloSeleccionado.getCondition().equals("new")) {
            textViewCondition.setText("Nuevo");
        } else {
            textViewCondition.setText("Usado");
        }
    }

    public void traerlistas() {
        controlerAtributo.traerArticulo(articuloSeleccionado.getId(), new ResultListener<Articulo>() {
            @Override
            public void finish(Articulo articulo) {
                adapterAtributo.setAtributoList(articulo.getAtributo());
                adapterImagen.setPicturesList(articulo.getImagenes());
            }
        });
    }

    private void habilitarOnClickDeFav() {
        floatingFav.setClickable(true);
    }

    private void actualizarFav() {
        if (esFavorita) {
            floatingFav.setImageResource(R.drawable.ic_favorite_black_24dp);
        } else {
            floatingFav.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
    }

    @Override
    public void informarArticuloSeleccionado(Articulo articulo) {
    }

    @Override
    public void informarImagenSeleccionada(Pictures pictures) {
    }
}
