package com.stonetech.mezamamamama211;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by syuto on 2016/10/09.
 */

public class AlarmBroadcastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        Intent notification2 = new Intent(context, AlarmActivity.class);

        notification2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(notification2);

//        Log.d("AlarmBroadcastReceiver","onReceive() pid=" + android.os.Process.myPid());
//
//        int bid = intent.getIntExtra("intentId",0);
//
//        Intent intent2 = new Intent(context, MainActivity.class);
//        PendingIntent pendingIntent =
//                PendingIntent.getActivity(context, bid, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
//
////
//        NotificationManager notificationManager =
//                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification notification = new NotificationCompat.Builder(context)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setTicker("時間ですdedededede")
//                .setWhen(System.currentTimeMillis())
//              //  .setContentTitle("TestAlarm "+bid)
//                .setContentText("時間になりましたdedededee")
//                // 音、バイブレート、LEDで通知
//                .setDefaults(Notification.DEFAULT_ALL)
//                // 通知をタップした時にMainActivityを立ち上げる
//              //  .setContentIntent(pendingIntent)
//                .build();

//
//        // 古い通知を削除
//        notificationManager.cancelAll();
//        // 通知
//        notificationManager.notify(R.string.app_name, notification);


    }
}
