package com.example.ejerciciointegradormercadoesclavo.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UsuarioNuevo extends AppCompatActivity {

    private Uri uri;
    private int PICK_IMAGE_REQUEST = 1;
    @BindView(R.id.FragmentUsuarioNuevo_botonGuardarCambios) Button botonGuardarCambios;
    @BindView(R.id.FragmentUsuarioNuevo_Button_botonCargarImagen) Button subirImagen;
    @BindView(R.id.FragmentUsuarioNuevo_editText_nombreUsuario) EditText editTextNombre;
    @BindView(R.id.FragmentUsuarioNuevo_editText_apellidoUsuario) EditText editTextApellido;
    @BindView(R.id.FragmentUsuarioNuevo_imageview_Imagen) ImageView profilePic;
    private FirebaseFirestore firestore;
    private FirebaseUser currentUsuer;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_nuevo);
        ButterKnife.bind(this);
        storage = FirebaseStorage.getInstance();
        currentUsuer = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        subirImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
        botonGuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombreUsuario = editTextNombre.getText().toString();
                String apellidoUsuario = editTextApellido.getText().toString();
                Usuario usuario = new Usuario(nombreUsuario, apellidoUsuario,profilePic.toString());
                cargarImagenAFirebase();
                guardarInfoUsuario(usuario);
                Toast.makeText(UsuarioNuevo.this, "Bienvenido", Toast.LENGTH_LONG).show();
                startActivity(new Intent(UsuarioNuevo.this, MainActivity.class));
            }
        });
        traerUsuarioLogueado();
    }

    private void guardarInfoUsuario(final Usuario usuario) {
        firestore.collection("Usuarios")
                .document(currentUsuer.getUid())
                .set(usuario)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UsuarioNuevo.this, "No Guardado!", Toast.LENGTH_SHORT).show();
                    }
                });
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
                                Glide.with(UsuarioNuevo.this)
                                        .load(usuario.getImagenUrl())
                                        .into(profilePic);
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UsuarioNuevo.this, "No se puede Traer!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Elige tu foto de Perfil!!"), PICK_IMAGE_REQUEST);
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
                Toast.makeText(getBaseContext(), "imagen Cargada exitosamente", Toast.LENGTH_SHORT).show();
            }
        });
        startActivity(new Intent(getBaseContext(), MainActivity.class));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getBaseContext().getContentResolver(), uri);
                profilePic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


