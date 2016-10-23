package com.stonetech.mezamamamama211;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

/**
 * Created by syuto on 2016/10/11.
 */

public class BroadcastReceiverSleep extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Notification noti = new NotificationCompat.Builder(context)
                .setContentTitle("そろそろ就寝時間ですか?")    //  タイトルです（太字）。
                .setContentText("早く寝て明日に備えましょう!")    //  メッセージテキストです。
                .setAutoCancel(true)    //  タップされた時に、通知バーから消去する場合はtrueにします。
                .setSmallIcon(R.drawable.time_icon)    //  左側のアイコン画像です。

                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setWhen(System.currentTimeMillis())
                .build();

        NotificationManager manager = (NotificationManager)context.getSystemService
                (Context.NOTIFICATION_SERVICE);
        manager.notify(0, noti);

    }
}
