package com.antonymilian.viajeseguro.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.antonymilian.viajeseguro.R;
import com.antonymilian.viajeseguro.activities.client.RegisterActivity;
import com.antonymilian.viajeseguro.activities.driver.RegisterDriverActivity;

public class SelectOptionAuthActivity extends AppCompatActivity {

    Button mButtonGotoLogin;
    Button mButtonRegister;
    ImageView mImagenIconCategory;
    SharedPreferences mPref;
    TextView mtextViewCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_option_auth);

        mtextViewCategory = findViewById(R.id.textCategory);
        mImagenIconCategory = findViewById(R.id.iconCategory);
        mButtonGotoLogin = findViewById(R.id.btnGoToLogin);
        mButtonRegister = findViewById(R.id.btnGoToRegister);

        mPref = getApplicationContext().getSharedPreferences("typeUser", MODE_PRIVATE);
        String typeUser = mPref.getString("user", "");

        mButtonGotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLogin();
            }
        });

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegister();
            }
        });
        mButtonRegister.setVisibility(typeUser.equals("client") ? View.VISIBLE : View.INVISIBLE);
        mImagenIconCategory.setImageResource(typeUser.equals("client") ? R.drawable.usuarios256 : R.drawable.iconoayuda256);
        mtextViewCategory.setText(typeUser.equals("client") ? R.string.USER : R.string.PA);

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