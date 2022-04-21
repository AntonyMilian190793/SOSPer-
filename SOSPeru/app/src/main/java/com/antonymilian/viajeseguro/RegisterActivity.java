package com.antonymilian.viajeseguro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.antonymilian.viajeseguro.models.Client;
import com.antonymilian.viajeseguro.models.User;
import com.antonymilian.viajeseguro.providers.ClientProvider;
import com.antonymilian.viajeseguro.providers.AuthProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//import java.security.AuthProvider;

import dmax.dialog.SpotsDialog;

public class RegisterActivity extends AppCompatActivity {

    SharedPreferences mPref;

    AuthProvider mAuthProvider;
    ClientProvider mClientProvider;

    //Views
    Button mButtonRegister;
    TextInputEditText mTextInputName;
    TextInputEditText mTextInputEmail;
    TextInputEditText mTextInputPassword;

    AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuthProvider = new AuthProvider();
        mClientProvider = new ClientProvider();

        mPref = getApplicationContext().getSharedPreferences("typeUser", MODE_PRIVATE);

        mDialog = new SpotsDialog.Builder().setContext(RegisterActivity.this).setMessage("Espere un momento").build();

        //Toast.makeText(this, "El valor que seleccionó fue " + selectedUser, Toast.LENGTH_SHORT).show();

        mButtonRegister = findViewById(R.id.btnRegister);
        mTextInputName = findViewById(R.id.textInputNombre);
        mTextInputEmail = findViewById(R.id.textImputCorreo);
        mTextInputPassword = findViewById(R.id.textInputContrasena);

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clickRegister();
            }
        });
    }

    void clickRegister(){

         final String name = mTextInputName.getText().toString();
         final String email = mTextInputEmail.getText().toString();
         final String password = mTextInputPassword.getText().toString();

        if(!name.isEmpty() && !email.isEmpty() && !password.isEmpty()){
            if(password.length() >= 6){
                mDialog.show();
                register(name, email, password);

            }else{
                Toast.makeText(this, "La contraseña debe tener 6 caracteres.", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Ingrese todos los campos.", Toast.LENGTH_SHORT).show();
        }
    }

    void register(final String name, String email, String password){
        mAuthProvider.register(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mDialog.hide();
                if(task.isSuccessful()){
                    String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Client client = new Client(id, name, email);
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
                    Toast.makeText(RegisterActivity.this, "El registro se realizó exitosamente", Toast.LENGTH_SHORT).show();
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

