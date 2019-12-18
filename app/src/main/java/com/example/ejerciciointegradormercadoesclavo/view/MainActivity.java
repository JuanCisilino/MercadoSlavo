package com.example.ejerciciointegradormercadoesclavo.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.ejerciciointegradormercadoesclavo.R;
import com.example.ejerciciointegradormercadoesclavo.model.Articulo;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        FragmentListaDeArticulos.ListenerDeFragment, FragmentFavoritos.ListenerDeFragment {

    @BindView(R.id.drawerLayout) DrawerLayout drawerLayout;
    @BindView(R.id.navigationView) NavigationView navigationView;
    private Integer id;
    private FirebaseUser currentUsuer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        currentUsuer = FirebaseAuth.getInstance().getCurrentUser();
        pegarFragment(new FragmentListaDeArticulos());
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void pegarFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainActivity_Frame, fragment)
                .addToBackStack("vamos para atras!!")
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        id = menuItem.getItemId();
        switch (id) {
            case R.id.menuPrincipal_aboutus:
                Toast.makeText(this, "About Us", Toast.LENGTH_LONG).show();
                getSupportFragmentManager().beginTransaction().replace(R.id.mainActivity_Frame, new FragmentAboutUs()).commit();
                break;
            case R.id.menuPrincipal_favoritos:
                if (currentUsuer == null) {
                    Toast.makeText(this, "Por Favor Inicie Sesion!!!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getBaseContext(), ActivityLogin.class));
                }else {
                Toast.makeText(this, "Ingresando a sus Articulos Favoritos", Toast.LENGTH_LONG).show();
                getSupportFragmentManager().beginTransaction().replace(R.id.mainActivity_Frame, new FragmentFavoritos()).commit();}
                break;
            case R.id.menuPrincipal_home:
                Toast.makeText(this, "Volviendo al Home", Toast.LENGTH_LONG).show();
                getSupportFragmentManager().beginTransaction().replace(R.id.mainActivity_Frame, new FragmentListaDeArticulos()).commit();
                break;
            case R.id.menuPrincipal_perfil:
                if (currentUsuer == null) {
                    Toast.makeText(this, "Por Favor Inicie Sesion!!!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getBaseContext(), ActivityLogin.class));
                }else {
                Toast.makeText(this, "Ingresando a su Perfil", Toast.LENGTH_LONG).show();
                getSupportFragmentManager().beginTransaction().replace(R.id.mainActivity_Frame, new FragmentPerfil()).commit();}
                break;
            case R.id.menuPrincipal_logout:
                Toast.makeText(this, "Gracias... Vuelva prontos!!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getBaseContext(), ActivityLogin.class));
                break;
        }
        drawerLayout.closeDrawers();
        return true;
    }

    @Override
    public void recibirArticulo(final Articulo articulo) {
        Toast.makeText(this, articulo.getNombreDeArticulo(), Toast.LENGTH_LONG).show();
        FragmentDetalleArticulo fragmentDescripcionDelArticulo = new FragmentDetalleArticulo();
        Bundle bundle = new Bundle();
        bundle.putSerializable(fragmentDescripcionDelArticulo.CLAVE_ARTICULO, articulo);
        fragmentDescripcionDelArticulo.setArguments(bundle);
        pegarFragment(fragmentDescripcionDelArticulo);
    }
}
