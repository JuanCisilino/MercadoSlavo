package com.example.ejerciciointegradormercadoesclavo.view;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.ejerciciointegradormercadoesclavo.R;
import com.example.ejerciciointegradormercadoesclavo.model.Articulo;
import com.example.ejerciciointegradormercadoesclavo.utils.ResultListener;

import java.util.ArrayList;
import java.util.List;

public class AdapterArticulo extends RecyclerView.Adapter<AdapterArticulo.ViewHolderArticulos> {

    private List<Articulo> articuloList;
    private ListenerDelAdapter listener;
    private View vistaDeLaCelda;
    private ProgressBar progressBar;

    public AdapterArticulo(List<Articulo>articuloList) {
        this.articuloList = articuloList;
    }

    public AdapterArticulo(ListenerDelAdapter listener){
        articuloList = new ArrayList<>();
        this.listener = listener;
        notifyDataSetChanged();
    }

    public void actualizarLista(List<Articulo> articuloList){
        this.articuloList = articuloList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderArticulos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        vistaDeLaCelda = layoutInflater.inflate(R.layout.celda_articulo,parent,false);
        return new ViewHolderArticulos(vistaDeLaCelda);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderArticulos holder, int position) {
        Articulo articuloAMostrar = articuloList.get(position);
        holder.cargarArticulo(articuloAMostrar);
    }

    @Override
    public int getItemCount() {
        return articuloList.size();
    }

    public void setArticuloList(List<Articulo> articuloList) {
        this.articuloList = articuloList;
        notifyDataSetChanged();
    }


    public class ViewHolderArticulos extends RecyclerView.ViewHolder{

        private TextView textViewNombreArticulo, textViewPrecio, textViewCondition;
        private ImageView imagenDeArticulo;

        public ViewHolderArticulos(@NonNull View itemView) {
            super(itemView);
           encontrarVariables();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Articulo articuloSeleccionado = articuloList.get(getAdapterPosition());
                    listener.informarArticuloSeleccionado(articuloSeleccionado);
                }
            });
        }

        public void encontrarVariables(){
            imagenDeArticulo = itemView.findViewById(R.id.imagen_celda_articulo);
            textViewNombreArticulo = itemView.findViewById(R.id.textView_nombre_articulo);
            textViewCondition = itemView.findViewById(R.id.textView_condicion_articulo);
            textViewPrecio = itemView.findViewById(R.id.textView_precio_articulo);
            progressBar = itemView.findViewById(R.id.celdaArticulo_ProgressBar);
        }

        public void cargarArticulo(Articulo articulo){
            Glide.with(imagenDeArticulo.getContext())
                    .load(articulo.getImagen())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                           progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .error(R.drawable.error).into(imagenDeArticulo);
            textViewNombreArticulo.setText(articulo.getNombreDeArticulo());
            textViewPrecio.setText( "$" + articulo.getPrecioDeArticulo());
            if (articulo.getCondition().equals("new")) {
                textViewCondition.setText("Nuevo");
            }else{
                textViewCondition.setText("Usado");
            }
        }
    }

    public List<Articulo> getArticuloList() {
        return articuloList;
    }

    public interface ListenerDelAdapter{
        public void informarArticuloSeleccionado(Articulo articulo);
    }

}
