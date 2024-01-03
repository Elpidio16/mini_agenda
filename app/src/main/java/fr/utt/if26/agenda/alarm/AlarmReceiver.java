package fr.utt.if26.agenda.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationHelper = new NotificationHelper(context);
        }
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification("Rappel", "Tu dois debuter ton programme de la journee");
        notificationHelper.getmManager().notify(1, nb.build());
    }
}
