package com.antonymilian.viajeseguro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelectOptionAuthActivity extends AppCompatActivity {

    Button mButtonGotoLogin;
    Button mButtonRegister;

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

    }



    public void goToLogin(){
        Intent intent = new Intent(SelectOptionAuthActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void goToRegister(){
        Intent intent = new Intent(SelectOptionAuthActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}