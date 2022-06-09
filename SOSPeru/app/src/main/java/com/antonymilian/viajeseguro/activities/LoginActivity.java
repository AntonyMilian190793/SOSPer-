package com.antonymilian.viajeseguro.activities;

import static android.widget.Toast.makeText;

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
import com.antonymilian.viajeseguro.activities.client.MapClientActivity;
import com.antonymilian.viajeseguro.activities.driver.MapDriverActivity;
import com.antonymilian.viajeseguro.activities.driver.RegisterDriverActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dmax.dialog.SpotsDialog;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText mTextInputEmail;
    TextInputEditText mTextInputPassword;
    Button mButtonLogin;

    SharedPreferences mPref;

    FirebaseAuth mAuth;
    //DatabaseReference mDatabase;

    AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mTextInputEmail = findViewById(R.id.textImputEmail);
        mTextInputPassword = findViewById(R.id.textImputPassword);
        mButtonLogin = findViewById(R.id.btnLogin);

        mAuth = FirebaseAuth.getInstance();
        //mDatabase = FirebaseDatabase.getInstance().getReference();

        mDialog = new SpotsDialog.Builder().setContext(LoginActivity.this).setMessage("Espere un momento").build();
        mPref = getApplicationContext().getSharedPreferences("typeUser", MODE_PRIVATE);

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mTextInputEmail.getText().toString() + "@sosperu.com.pe";
                String password = mTextInputPassword.getText().toString();
                Toast toast = validate(email,password);
                if(toast != null)
                    toast.show();
                else
                    Authenticator(email,password);
            }
        });

    }

    private Toast validate(String email,String password){
        if(email.isEmpty())
            return makeText(LoginActivity.this, "El email es oblogatorio!", Toast.LENGTH_SHORT);
        if(password.isEmpty())
            return makeText(LoginActivity.this, "La contraseña es oblogatorio!", Toast.LENGTH_SHORT);
        if(password.length() < 6)
            return makeText(LoginActivity.this, "La contraseña debe tener más de 6 caracteres", Toast.LENGTH_SHORT);
        return null;
    }

    private void Authenticator(String email,String password){
        mAuth.signInAnonymously();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    makeText(LoginActivity.this, "El usuario y/o contraseña son incorrectos", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                if(mPref.getString("user", "").equals("client")){
                    Intent intent = new Intent(LoginActivity.this, MapClientActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(LoginActivity.this, MapDriverActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                mDialog.dismiss();
                }
        });
    }
}