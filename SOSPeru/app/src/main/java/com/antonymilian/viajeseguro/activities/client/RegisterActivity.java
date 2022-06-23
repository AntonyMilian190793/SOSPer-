package com.antonymilian.viajeseguro.activities.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

//import java.security.AuthProvider;

import dmax.dialog.SpotsDialog;

public class RegisterActivity extends AppCompatActivity {

//    SharedPreferences mPref;

    AuthProvider mAuthProvider;
    ClientProvider mClientProvider;

    //Views
    Button btnRegister;
    Button btnSaveDni;
    ImageView imgDniUser;
    TextInputEditText textInputNombres;
    TextInputEditText textInputApellidos;
    TextInputEditText textInputDni;
    TextInputEditText textInputCorreo;
    TextInputEditText textInputMovil;
    TextInputEditText textInputContrasena;

    AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuthProvider = new AuthProvider();
        mClientProvider = new ClientProvider();

        mDialog = new SpotsDialog.Builder().setContext(RegisterActivity.this).setMessage("Espere un momento").build();

        //Toast.makeText(this, "El valor que seleccionó fue " + selectedUser, Toast.LENGTH_SHORT).show();

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
                    Client client = new Client(id, nombres, apellidos,correo,movil,dni.substring(0,dni.indexOf("@")));
                    create(client);
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
                    //Toast.makeText(RegisterActivity.this, "El registro se realizó exitosamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, MapClientActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else{
                    Toast.makeText(RegisterActivity.this, "No se pudo crear el cliente", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

//    void saveUser(String id,String name, String email){
//        String selectedUser = mPref.getString("user", "");
//        User user = new User();
//        user.setEmail(email);
//        user.setName(name);
//
//        if(selectedUser.equals("driver")){
//
//            mDatabase.child("Users").child("Drivers").child(id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                   if(task.isSuccessful()){
//                       Toast.makeText(RegisterActivity.this, "Registro exitoso!", Toast.LENGTH_SHORT).show();
//                   }else{
//                       Toast.makeText(RegisterActivity.this, "Fallo el registro", Toast.LENGTH_SHORT).show();
//                   }
//                }
//            });
//        }else if(selectedUser.equals("client")){
//            mDatabase.child("Users").child("Clients").child(id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if(task.isSuccessful()){
//                        Toast.makeText(RegisterActivity.this, "Registro exitoso!", Toast.LENGTH_SHORT).show();
//                    }else{
//                        Toast.makeText(RegisterActivity.this, "Fallo el registro", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        }
//    }
}

