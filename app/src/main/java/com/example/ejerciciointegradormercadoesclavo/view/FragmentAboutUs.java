package com.example.ejerciciointegradormercadoesclavo.view;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ejerciciointegradormercadoesclavo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAboutUs extends Fragment {

    @BindView
   (R.id.fragmentaboutus_textview_contenido) TextView descripcion;
    private String historia;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);
        ButterKnife.bind(this, view);
        historia = "Somos una compania destinada a satisfacer las necesidades del cliente, nos enfocamos en sus pedidos especificos,\n" +
                "lo cual nos brinda una enorme variedad de productos que llevan desde los hobbies hasta lo profesional.";
        descripcion.setText(historia);
        return view;
    }

}
