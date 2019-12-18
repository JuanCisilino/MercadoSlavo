package com.example.ejerciciointegradormercadoesclavo.view;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;

import com.example.ejerciciointegradormercadoesclavo.R;
import com.example.ejerciciointegradormercadoesclavo.controller.ControlerArticulo;
import com.example.ejerciciointegradormercadoesclavo.model.Articulo;
import com.example.ejerciciointegradormercadoesclavo.model.ArticuloDao;
import com.example.ejerciciointegradormercadoesclavo.model.Pictures;
import com.example.ejerciciointegradormercadoesclavo.utils.ResultListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentListaDeArticulos extends Fragment implements AdapterArticulo.ListenerDelAdapter            {

    @BindView(R.id.fragmentArticulos_recycler) RecyclerView recyclerView;
    private ListenerDeFragment listenerDeFragment;
    private ControlerArticulo controlerArticulo;
    private AdapterArticulo adapterArticulo;
    private View view;
    @BindView(R.id.FragmntArticulos_SearchView) SearchView consulta;
    private String consul;
    private Bundle bundle = new Bundle();
    private ItemTouchHelper itemTouchHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_lista_de_articulos, container, false);
        ButterKnife.bind(this, view);
        controlerArticulo = new ControlerArticulo(getContext());
        adapterArticulo = new AdapterArticulo(this);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        if (!hayInternet(getContext())) {
            controlerArticulo.traeUltimaBusqueda(new ResultListener<List<Articulo>>() {
                @Override
                public void finish(List<Articulo> result) {
                    adapterArticulo.setArticuloList(result);
                    recyclerView.setAdapter(adapterArticulo);
                }
            });
        }
        hacerConsulta();

        itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listenerDeFragment = (ListenerDeFragment) context;
    }

    @Override
    public void informarArticuloSeleccionado(Articulo articulo) {
        listenerDeFragment.recibirArticulo(articulo);
    }

    public interface ListenerDeFragment {
        public void recibirArticulo(Articulo articulo);
    }

    public void hacerConsulta(){
        consulta.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                bundle.putString(ArticuloDao.ARTICULOSELECCIONADO, s);
                consul = s;
                controlerArticulo.traerListaDeArticulos(consul, new ResultListener<List<Articulo>>() {
                    @Override
                    public void finish(List<Articulo> result) {
                        adapterArticulo.setArticuloList(result);
                        recyclerView.setAdapter(adapterArticulo);
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN |
            ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                              @NonNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            Collections.swap(adapterArticulo.getArticuloList(), fromPosition, toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        }
    };


    public Boolean hayInternet(Context context){

        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }
}
