package com.antonymilian.viajeseguro.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.antonymilian.viajeseguro.R;
import com.antonymilian.viajeseguro.activities.client.RegisterActivity;
import com.antonymilian.viajeseguro.activities.driver.RegisterDriverActivity;

public class SelectOptionAuthActivity extends AppCompatActivity {

    Button mButtonGotoLogin;
    Button mButtonRegister;
    SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_option_auth);

        mButtonGotoLogin = findViewById(R.id.btnGoToLogin);
        mButtonGotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLogin();
            }
        });

        mButtonRegister = findViewById(R.id.btnGoToRegister);
        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegister();
            }
        });

        mPref = getApplicationContext().getSharedPreferences("typeUser", MODE_PRIVATE);
    }



    public void goToLogin(){
        Intent intent = new Intent(SelectOptionAuthActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void goToRegister(){
        String typeUser = mPref.getString("user", "");
        if(typeUser.equals("client")){
            Intent intent = new Intent(SelectOptionAuthActivity.this, RegisterActivity.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(SelectOptionAuthActivity.this, RegisterDriverActivity.class);
            startActivity(intent);
        }
    }
}