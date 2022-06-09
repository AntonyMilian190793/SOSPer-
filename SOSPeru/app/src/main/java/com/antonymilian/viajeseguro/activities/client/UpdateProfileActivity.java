package com.antonymilian.viajeseguro.activities.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.antonymilian.viajeseguro.R;
import com.antonymilian.viajeseguro.providers.AuthProvider;
import com.antonymilian.viajeseguro.providers.ClientProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class UpdateProfileActivity extends AppCompatActivity {

    private ImageView mImageViewprofile;
    private Button mButtonProfile;
    private TextView mTextViewName;

    private ClientProvider mClientProvider;
    private AuthProvider mAuthProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        mImageViewprofile = findViewById(R.id.imageViewProfile);
        mButtonProfile = findViewById(R.id.btnUpdateProfile);
        mTextViewName = findViewById(R.id.textImputName);

        mClientProvider = new ClientProvider();
        mAuthProvider = new AuthProvider();

        getClientInfo();

        mButtonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
    }

    private void getClientInfo(){
        mClientProvider.getClient(mAuthProvider.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String name = dataSnapshot.child("name").getValue().toString();
                    mTextViewName.setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateProfile() {
    }
}