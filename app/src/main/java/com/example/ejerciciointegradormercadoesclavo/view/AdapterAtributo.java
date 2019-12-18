package com.example.ejerciciointegradormercadoesclavo.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ejerciciointegradormercadoesclavo.R;
import com.example.ejerciciointegradormercadoesclavo.model.Articulo;
import com.example.ejerciciointegradormercadoesclavo.model.Attributes;

import org.w3c.dom.Attr;

import java.util.ArrayList;
import java.util.List;

public class AdapterAtributo extends RecyclerView.Adapter<AdapterAtributo.ViewHolderAtributo>{

    private List<Attributes> atributoList;
    private ListenerDelAdapter listener;
    private View vistaDeLaCelda;

    public AdapterAtributo(List<Attributes>atributoList) {
        this.atributoList = atributoList;
    }

    public AdapterAtributo(ListenerDelAdapter listener){
        atributoList = new ArrayList<>();
        this.listener = listener;
    }

    public void setAtributoList(List<Attributes> atributoList) {
        this.atributoList = atributoList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderAtributo onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        vistaDeLaCelda = layoutInflater.inflate(R.layout.celda_atributos,parent,false);
        return new AdapterAtributo.ViewHolderAtributo(vistaDeLaCelda);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderAtributo holder, int position) {
        Attributes atributoaMostrar = atributoList.get(position);
        holder.cargarArticulo(atributoaMostrar);
    }

    @Override
    public int getItemCount() {
        return atributoList.size();
    }

    public class ViewHolderAtributo extends RecyclerView.ViewHolder{

        private TextView textViewNombreAtributo, textViewDetalleAtributo;

        public ViewHolderAtributo(@NonNull View itemView) {
            super(itemView);
            encontrarVariables();
        }

        public void encontrarVariables(){
            textViewNombreAtributo = itemView.findViewById(R.id.celdaAtributos_Textview_NombreAtributo);
            textViewDetalleAtributo = itemView.findViewById(R.id.celdaAtributos_Textview_DetalleAtributo);
        }

        public void cargarArticulo(Attributes atributo){
            textViewNombreAtributo.setText(atributo.getNombreAtributo());
            textViewDetalleAtributo.setText(atributo.getDetalleAtributo());
        }
    }

    public interface ListenerDelAdapter{
        public void informarArticuloSeleccionado(Articulo articulo);
    }

}
