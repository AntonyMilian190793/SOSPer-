package com.antonymilian.viajeseguro.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.antonymilian.viajeseguro.models.Client;

import java.util.HashMap;
import java.util.Map;

public class ClientProvider {

    DatabaseReference mDatabase;

    public ClientProvider(){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Clients");
    }

    public Task<Void> create(Client client){
        Map<String, Object> map = new HashMap<>();
        map.put("nombres", client.getNombres());
        map.put("apellidos", client.getApellidos());
        map.put("correo", client.getCorreo());
        map.put("movil", client.getMovil());
        map.put("dni", client.getDni());
        return mDatabase.child(client.getId()).setValue(map);
    }

    public Task<Void> update(Client client){
        Map<String, Object> map = new HashMap<>();
        map.put("name", client.getNombres());
        map.put("image", client.getImage());
        return mDatabase.child(client.getId()).updateChildren(map);
    }

    public DatabaseReference getClient(String idClient){
        return mDatabase.child(idClient);
    }
}
