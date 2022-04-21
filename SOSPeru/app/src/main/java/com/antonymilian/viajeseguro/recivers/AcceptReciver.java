package com.antonymilian.viajeseguro.recivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.antonymilian.viajeseguro.activities.driver.MapDriverBookingActivity;
import com.antonymilian.viajeseguro.providers.AuthProvider;
import com.antonymilian.viajeseguro.providers.ClientBookingProvider;
import com.antonymilian.viajeseguro.providers.GeofireProvider;

public class AcceptReciver extends BroadcastReceiver {

    private ClientBookingProvider mClientBookingProvider;
    private GeofireProvider mGiofireProvider;
    private AuthProvider mAuthProvider;

    @Override
    public void onReceive(Context context, Intent intent) {
        mAuthProvider = new AuthProvider();
        mGiofireProvider = new GeofireProvider("drivers_activos");
        mGiofireProvider.removeLocation(mAuthProvider.getId());

        String idClient = intent.getExtras().getString("idClient");
        mClientBookingProvider = new ClientBookingProvider();
        mClientBookingProvider.updateStatus(idClient, "accept");

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(2);

        Intent intent1 = new Intent(context, MapDriverBookingActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent1.putExtra("idClient", idClient);
        context.startActivity(intent1);
    }
}
