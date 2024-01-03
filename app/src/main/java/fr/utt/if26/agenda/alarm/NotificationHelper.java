package fr.utt.if26.agenda.alarm;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import fr.utt.if26.agenda.R;

public class NotificationHelper extends ContextWrapper {
    public static final String chaneL1ID = "chaneL1ID";
    public static final String chaneL1Name = "chanel 1";
    public static final String chaneL2ID = "chaneL2ID";
    public static final String chaneL2Name = "chanel 2";

    private NotificationManager mManager;
    @RequiresApi(api = Build.VERSION_CODES.O)
    public NotificationHelper(Context base) {
        super(base);
            creatChannels();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void creatChannels() {
        NotificationChannel channel1 = new NotificationChannel(chaneL1ID, chaneL1Name, NotificationManager.IMPORTANCE_DEFAULT);
        channel1.enableLights(true);
        channel1.enableVibration(true);
        channel1.setLightColor(com.google.android.material.R.color.design_default_color_primary);
        channel1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getmManager().createNotificationChannel(channel1);

        NotificationChannel channel2 = new NotificationChannel(chaneL2ID, chaneL2Name, NotificationManager.IMPORTANCE_DEFAULT);
        channel2.enableLights(true);
        channel2.enableVibration(true);
        channel2.setLightColor(com.google.android.material.R.color.design_default_color_primary);
        channel2.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getmManager().createNotificationChannel(channel2);
    }

    public NotificationManager getmManager(){
        if (mManager == null){
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    public NotificationCompat.Builder getChannelNotification(String title, String message){
        return new NotificationCompat.Builder(getApplicationContext(), chaneL1ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_one);
    }

    public NotificationCompat.Builder getChannel2Notification(String title, String message){
        return new NotificationCompat.Builder(getApplicationContext(), chaneL2ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_two);
    }
}
