package com.example.ejerciciointegradormercadoesclavo.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ejerciciointegradormercadoesclavo.R;
import com.example.ejerciciointegradormercadoesclavo.model.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mahfa.dnswitch.DayNightSwitch;
import com.mahfa.dnswitch.DayNightSwitchListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPerfil extends Fragment {

    private Uri uri;
    private int PICK_IMAGE_REQUEST = 1;
    @BindView(R.id.FragmentPerfil_imageview_Imagen) ImageView profilePic;
    @BindView(R.id.FragmentPerfil_Button_botonCargarImagen) Button botonCargarImagen;
    @BindView(R.id.FragmentPerfil_Button_botonSubirImagen) Button botonSubirImagen;
    @BindView(R.id.FragmentPerfil_editText_Nombre) EditText editTextNombre;
    @BindView(R.id.FragmentPerfil_editText_Apellido) EditText editTextApellido;
    @BindView(R.id.FragmentPerfil_FrameLayout) FrameLayout layout;
    @BindView(R.id.FragmentPerfil_DayNightSwitch) DayNightSwitch dayNightSwitch;
    private FirebaseStorage storage;
    private FirebaseFirestore firestore;
    private FirebaseUser currentUsuer;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.fragment_perfil, container, false);
        ButterKnife.bind(this, view);
        storage = FirebaseStorage.getInstance();
        currentUsuer = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        botonCargarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        botonSubirImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarImagenAFirebase();
                String nombreUsuario = editTextNombre.getText().toString();
                String apellidoUsuario = editTextApellido.getText().toString();
                Usuario usuario = new Usuario(nombreUsuario,apellidoUsuario,profilePic.toString());
                guardarInfoUsuario(usuario);
            }
        });
        dayNightSwitch.setDuration(450);
        dayNightSwitch.setListener(new DayNightSwitchListener() {
            @Override
            public void onSwitch(boolean isNight) {
                if (isNight) {
                    layout.setAlpha(0.7f);
                } else {
                    layout.setAlpha(1f);
                }
            }
        });
        traerUsuarioLogueado();
        return view;
    }


    public void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                profilePic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void cargarImagenAFirebase() {
        StorageReference path = storage.getReference().child("ProfiePics").child(currentUsuer.getUid());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Bitmap bm=((BitmapDrawable)profilePic.getDrawable()).getBitmap();
        bm.compress(Bitmap.CompressFormat.JPEG,20,byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();
        UploadTask uploadTask = path.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getContext(), "imagen Cargada exitosamente", Toast.LENGTH_SHORT).show();
            }
        });
        startActivity(new Intent(getContext(), MainActivity.class));
    }

    private void guardarInfoUsuario(final Usuario usuario){
        firestore.collection("Usuarios")
                .document(currentUsuer.getUid())
                .set(usuario);
        cargarImagenDelStorageAlDatabase();
    }

    private void cargarImagenDelStorageAlDatabase() {
        StorageReference path = storage.getReference().child("ProfiePics").child(currentUsuer.getUid());
        path.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                firestore.collection("Usuarios")
                        .document(currentUsuer.getUid())
                        .update("imagenUrl",uri.toString());
            }
        });
    }

    private void traerUsuarioLogueado() {
        firestore.collection("Usuarios")
                .document(currentUsuer.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Usuario usuario = documentSnapshot.toObject(Usuario.class);
                        if (usuario != null) {
                            editTextNombre.setHint(usuario.getNombre());
                            editTextApellido.setHint(usuario.getApellido());
                            if (usuario.getImagenUrl() != null) {
                                Glide.with(getContext())
                                        .load(usuario.getImagenUrl())
                                        .into(profilePic);
                            }
                        }
                    }
                });
    }

}
