package com.antonymilian.viajeseguro.providers;

import com.antonymilian.viajeseguro.models.FCMBody;
import com.antonymilian.viajeseguro.models.FCMResponse;
import com.antonymilian.viajeseguro.retrofit.IFCMApi;
import com.antonymilian.viajeseguro.retrofit.RetrofitClient;

import retrofit2.Call;

public class NotificationProvieder {

    private String url = "https://fcm.googleapis.com";

    public NotificationProvieder() {
    }

    public Call<FCMResponse> sendNotification(FCMBody body){
        return RetrofitClient.getClientObject(url).create(IFCMApi.class).send(body);
    }
}
