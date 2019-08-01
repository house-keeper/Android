package com.example.housekeeper_android.ui.FCM;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.housekeeper_android.ui.activity.DoorActivity;
import com.example.housekeeper_android.ui.etc.PushWakeLock;
import com.google.firebase.*;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.example.housekeeper_android.R;


public class FireBaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        screenOn();

        sendNotification(title, body);


    }

    private void handleNow() {

        Log.d(TAG, "Short lived task is done.");

    }

    private void screenOn(){
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (!pm.isScreenOn()) {
            Log.d("TESTTEST","screen is off -> on");
            PushWakeLock.acquireCpuWakeLock(getApplicationContext());
            PushWakeLock.releaseCpuLock();
        }else{
            Log.d("TESTTEST","screen is on -> off");
        }
    }
    
    private void sendNotification(String title, String body){
        Intent intent = new Intent(this, DoorActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,

                PendingIntent.FLAG_ONE_SHOT);



//        String channelId = getString(R.string.);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder =

                new NotificationCompat.Builder(this, "블라블라")

                        .setSmallIcon(R.drawable.bar_button_alarm)

                        .setContentTitle(title)

                        .setContentText(body)

                        .setAutoCancel(true)

                        .setSound(defaultSoundUri)
                        .setDefaults(Notification.DEFAULT_VIBRATE)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                        .setContentIntent(pendingIntent);



        NotificationManager notificationManager =

                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

//            String channelName = getString(R.string.default_notification_channel_name);

            NotificationChannel channel = new NotificationChannel("블라블라", "아이씨!", NotificationManager.IMPORTANCE_HIGH);

            notificationManager.createNotificationChannel(channel);

        }

        notificationManager.notify(0, notificationBuilder.build());

    }





}
