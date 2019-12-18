package com.example.ejerciciointegradormercadoesclavo.view;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import com.example.ejerciciointegradormercadoesclavo.model.Attributes;
import com.example.ejerciciointegradormercadoesclavo.model.Pictures;

import java.util.ArrayList;
import java.util.List;

public class AdapterImagen extends RecyclerView.Adapter<AdapterImagen.ViewHolderPictures>{

    private List<Pictures> picturesList;
    private ListenerDelAdapter listener;
    private View vistaDeLaCelda;
    private ProgressBar progressBar;


    public AdapterImagen(List<Pictures>picturesList) {
        this.picturesList = picturesList;
    }

    public AdapterImagen(ListenerDelAdapter listener){
        picturesList = new ArrayList<>();
        this.listener = listener;
    }


    public void setPicturesList(List<Pictures> picturesList) {
        this.picturesList = picturesList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderPictures onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        vistaDeLaCelda = layoutInflater.inflate(R.layout.celda_imagen,parent,false);
        return new AdapterImagen.ViewHolderPictures(vistaDeLaCelda);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPictures holder, int position) {
        Pictures atributoaMostrar = picturesList.get(position);
        holder.cargarArticulo(atributoaMostrar);
    }

    @Override
    public int getItemCount() {
        return picturesList.size();
    }


    public class ViewHolderPictures extends RecyclerView.ViewHolder{

        private ImageView imagen;

        public ViewHolderPictures(@NonNull View itemView) {
            super(itemView);
            encontrarVariables();

        }

        public void encontrarVariables(){
            imagen = itemView.findViewById(R.id.celdaImagen_Imageview_Imagen);
            progressBar = itemView.findViewById(R.id.celdaImagen_ProgressBar);
        }

        public void cargarArticulo(Pictures imagenAMostrar){
            Glide.with(imagen.getContext())
                    .load(imagenAMostrar.getSecure_url())
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
                    .error(R.drawable.error).into(imagen);
        }
    }

    public interface ListenerDelAdapter{
        public void informarImagenSeleccionada(Pictures pictures);
    }

}