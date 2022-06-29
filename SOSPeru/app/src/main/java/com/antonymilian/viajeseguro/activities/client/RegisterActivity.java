package com.antonymilian.viajeseguro.activities.client;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.antonymilian.viajeseguro.R;
import com.antonymilian.viajeseguro.activities.driver.MapDriverActivity;
import com.antonymilian.viajeseguro.activities.driver.RegisterDriverActivity;
import com.antonymilian.viajeseguro.models.Client;
import com.antonymilian.viajeseguro.providers.ClientProvider;
import com.antonymilian.viajeseguro.providers.AuthProvider;
import com.antonymilian.viajeseguro.providers.ImagesProvider;
import com.antonymilian.viajeseguro.utils.FileUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.UploadTask;

//import java.security.AuthProvider;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import dmax.dialog.SpotsDialog;

public class RegisterActivity extends AppCompatActivity {

//    SharedPreferences mPref;

    AuthProvider mAuthProvider;
    ClientProvider mClientProvider;
    ImagesProvider mImagesProvider;
    //Views
    Button btnRegister;
    Button btnSaveDni;
    ImageView imgDniUser;
    File mImageFile;
    TextInputEditText textInputNombres;
    TextInputEditText textInputApellidos;
    TextInputEditText textInputDni;
    TextInputEditText textInputCorreo;
    TextInputEditText textInputMovil;
    TextInputEditText textInputContrasena;

    AlertDialog mDialog;
    private ProgressDialog mProgressDialog;
    private final int GALLERY_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuthProvider = new AuthProvider();
        mClientProvider = new ClientProvider();
        mImagesProvider = new ImagesProvider("client_DNI_images");
        mDialog = new SpotsDialog.Builder().setContext(RegisterActivity.this).setMessage("Espere un momento").build();
        mProgressDialog = new ProgressDialog(this);

        btnRegister = findViewById(R.id.btnRegister);
        btnSaveDni = findViewById(R.id.btnSaveDni);
        imgDniUser = findViewById(R.id.imgDniUser);

        textInputNombres = findViewById(R.id.textInputNombres);
        textInputApellidos = findViewById(R.id.textInputApellidos);
        textInputDni = findViewById(R.id.textInputDni);
        textInputCorreo = findViewById(R.id.textInputCorreo);
        textInputMovil = findViewById(R.id.textInputMovil);
        textInputContrasena = findViewById(R.id.textInputContrasena);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });

        btnSaveDni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    void registrarUsuario(){

        final String nombres = textInputNombres.getText().toString();
        final String apellidos = textInputApellidos.getText().toString();
        final String dni = textInputDni.getText().toString() + "@sosperu.com.pe";
        final String correo = textInputCorreo.getText().toString();
        final String movil = textInputMovil.getText().toString();
        final String password = textInputContrasena.getText().toString();

        if(!nombres.isEmpty() && !dni.isEmpty() && !password.isEmpty()){
            if(password.length() >= 6){
                mDialog.show();
                register(nombres, apellidos, correo, movil, dni, password);

            }else{
                Toast.makeText(this, "La contraseña debe tener 6 caracteres.", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Ingrese todos los campos.", Toast.LENGTH_SHORT).show();
        }
    }

    void register(String nombres, String apellidos, String correo, String movil, String dni, String password){
        mAuthProvider.register(dni, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mDialog.hide();
                if(task.isSuccessful()){
                    String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Client client = new Client(id, nombres, apellidos,correo,movil,textInputDni.getText().toString());
                    create(client);
                    saveImage();
                }else{
                    Toast.makeText(RegisterActivity.this, "No se pudo registrar el Usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void create(Client client){
        mClientProvider.create(client).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(RegisterActivity.this, MapClientActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else{
                    Toast.makeText(RegisterActivity.this, "No se pudo crear el cliente", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_REQUEST );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== GALLERY_REQUEST && resultCode == RESULT_OK) {
            try {
                mImageFile = FileUtil.from(this, data.getData());
                imgDniUser.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
            } catch(Exception e) {
                Log.d("ERROR", "Mensaje: " +e.getMessage());
            }
        }
    }

    private void saveImage() {
        mImagesProvider.saveImages(RegisterActivity.this, mImageFile, mAuthProvider.getId()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    mImagesProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String image = uri.toString();
                            Client client = new Client();
                            client.setImage(image);
                            client.setId(mAuthProvider.getId());
                            mClientProvider.update(image,mAuthProvider.getId()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mProgressDialog.dismiss();
                                }
                            });
                        }
                    });
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Hubo un error al subir la imagen", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}

