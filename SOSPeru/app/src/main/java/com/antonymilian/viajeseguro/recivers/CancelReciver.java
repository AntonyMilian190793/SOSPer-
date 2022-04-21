package com.antonymilian.viajeseguro.recivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.antonymilian.viajeseguro.providers.ClientBookingProvider;

public class CancelReciver extends BroadcastReceiver {

    private ClientBookingProvider mClientBookingProvider;

    @Override
    public void onReceive(Context context, Intent intent) {
        String idClient = intent.getExtras().getString("idClient");
        mClientBookingProvider = new ClientBookingProvider();
        mClientBookingProvider.updateStatus(idClient, "cancel");

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(2);

    }
}
