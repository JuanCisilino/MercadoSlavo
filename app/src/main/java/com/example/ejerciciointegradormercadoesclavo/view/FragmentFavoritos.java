package com.example.ejerciciointegradormercadoesclavo.view;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ejerciciointegradormercadoesclavo.R;
import com.example.ejerciciointegradormercadoesclavo.controller.ControlerArticulo;
import com.example.ejerciciointegradormercadoesclavo.controller.FirestoreController;
import com.example.ejerciciointegradormercadoesclavo.model.Articulo;
import com.example.ejerciciointegradormercadoesclavo.utils.ResultListener;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentFavoritos extends Fragment implements AdapterFavoritos.ListenerDelAdapter{

    @BindView(R.id.fragmentFavoritos_Recycler_Lista)
    RecyclerView recyclerViewLista;
    private AdapterFavoritos adapterFavoritos;
    private View view;
    private FirestoreController firestoreController;
    private ListenerDeFragment listenerDeFragment;
    private ItemTouchHelper itemTouchHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favoritos, container, false);
        ButterKnife.bind(this, view);
        firestoreController = new FirestoreController();
        adapterFavoritos = new AdapterFavoritos(this);
        firestoreController.traerListaDeFavorito(new ResultListener<List<Articulo>>() {
            @Override
            public void finish(List<Articulo> result) {
                adapterFavoritos.setListaDeFavoritos(result);
            }
        });
        recyclerViewLista.setLayoutManager(new LinearLayoutManager(getContext(), recyclerViewLista.VERTICAL, false));
        recyclerViewLista.setAdapter(adapterFavoritos);
        itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerViewLista);
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

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN |
            ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                              @NonNull RecyclerView.ViewHolder target) {

            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();

            Collections.swap(adapterFavoritos.getListaDeFavoritos(), fromPosition, toPosition);

            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);

            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };
}
