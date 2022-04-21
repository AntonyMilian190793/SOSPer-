package com.antonymilian.viajeseguro.activities.driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.antonymilian.viajeseguro.R;
import com.antonymilian.viajeseguro.activities.client.RegisterActivity;
import com.antonymilian.viajeseguro.models.Client;
import com.antonymilian.viajeseguro.models.Driver;
import com.antonymilian.viajeseguro.providers.AuthProvider;
import com.antonymilian.viajeseguro.providers.ClientProvider;
import com.antonymilian.viajeseguro.providers.DriverProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import dmax.dialog.SpotsDialog;

public class RegisterDriverActivity extends AppCompatActivity {


    AuthProvider mAuthProvider;
    DriverProvider mDriveProvider;

    //Views
    Button mButtonRegister;
    TextInputEditText mTextInputName;
    TextInputEditText mTextInputEmail;
    TextInputEditText mTextInputPassword;
    TextInputEditText mTextInputPlacaVehiculo;
    TextInputEditText mTextInputMarcaVehiculo;

    AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_driver);

        mAuthProvider = new AuthProvider();
        mDriveProvider = new DriverProvider();


        mDialog = new SpotsDialog.Builder().setContext(RegisterDriverActivity.this).setMessage("Espere un momento").build();

        //Toast.makeText(this, "El valor que seleccionó fue " + selectedUser, Toast.LENGTH_SHORT).show();

        mButtonRegister = findViewById(R.id.btnRegister);
        mTextInputName = findViewById(R.id.textInputNombre);
        mTextInputEmail = findViewById(R.id.textImputCorreo);
        mTextInputPassword = findViewById(R.id.textInputContrasena);
        mTextInputMarcaVehiculo = findViewById(R.id.textImputMarcaVehiculo);
        mTextInputPlacaVehiculo = findViewById(R.id.textImputMarcaVehiculo);

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
        final String vehiculoMarca = mTextInputMarcaVehiculo.getText().toString();
        final String vehiculoPlaca = mTextInputPlacaVehiculo.getText().toString();

        if(!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && !vehiculoPlaca.isEmpty() && !vehiculoMarca.isEmpty()){
            if(password.length() >= 6){
                mDialog.show();
                register(name, email, password, vehiculoMarca, vehiculoPlaca);

            }else{
                Toast.makeText(this, "La contraseña debe tener 6 caracteres.", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Ingrese todos los campos.", Toast.LENGTH_SHORT).show();
        }
    }

    void register(final String name, String email, String password, String vehiculoMarca, String vehiculoPlaca){
        mAuthProvider.register(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mDialog.hide();
                if(task.isSuccessful()){
                    String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Driver driver = new Driver(id, name, email, vehiculoMarca, vehiculoPlaca);
                    create(driver);
                }else{
                    Toast.makeText(RegisterDriverActivity.this, "No se pudo registrar el Usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void create(Driver driver){
        mDriveProvider.create(driver).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //Toast.makeText(RegisterDriverActivity.this, "El registro se realizó exitosamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterDriverActivity.this, MapDriverActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else{
                    Toast.makeText(RegisterDriverActivity.this, "No se pudo crear el cliente", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}