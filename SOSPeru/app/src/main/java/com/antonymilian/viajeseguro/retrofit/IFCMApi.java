package com.antonymilian.viajeseguro.retrofit;

import com.antonymilian.viajeseguro.models.FCMBody;
import com.antonymilian.viajeseguro.models.FCMResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMApi {

    @Headers({
            "Content-Type: application/json",
            "Authorization: key=AAAA7mrynTk:APA91bG3nmVEddEFnpQmsSs108h4_NqAGiLMgVfEZka0R7U7e6sheC-12i1oQyFUVPw0vmYkZCpTU0vZ85qNW8SH0tQRc231exLRCapb4ErLqIGvAX1y2nYF_vc8Hck8MT6D_HZhqd1l\t\n"
    })

    @POST("fcm/send")
    Call<FCMResponse> send(@Body FCMBody body);

}
