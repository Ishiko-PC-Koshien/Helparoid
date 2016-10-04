package com.stonetech.helparoid;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class Main2Activity extends AppCompatActivity implements LocationListener {

    private static String KEY_CURRENT_STATE = "CurrentState";
    private static int REST_OVER_1HOUR_LARGER = 8;
    private static int REST_OVER_1HOUR = 7;
    private static int REST_OVER_30MINUTE = 6;
    private static int REST_OVER_10MINUTE = 5;
    private static int REST_OVER_5MINUTE = 4;
    private static int REST_OVER_2MINUTE = 3;
    private static int REST_OVER_1MINUTE = 2;
    private static int REST_OVER_PASS = 1;
    private static int GO_HOUSE_NOTI = -5;

    int endnoti = 0;

    private static int LIMIT_1HOUR_LARGER = 61, LIMIT_1HOUR = 60,
            LIMIT_30MINUTE = 30, LIMIT_10MINUTE = 10, LIMIT_5MINUTE = 5, LIMIT_2MINUTE = 2,
            LIMIT_1MINUTE = 1, LIMIT_PASS = -10, GO_HOUSE = -50, END_NOTI = -100;

    private int currentState;

    //残り時間, カウントダウン, 過ぎた時間のテキスト
    TextView TimeText, CountText, PassText;

    //時刻設定, 家から出たのボタン
    Button GoButton;

    //カウントダウン、過ぎた時間のタイマー
    Timer CountTimer, PassTimer;
    //ハンドラー?
    Handler handler = new Handler();

    //杉田病、杉田分
    int passsecond = -1;
    int passminute = 0;

    int notitu = 100;

    //今の時間と、佐野時間
    int nowhour, nowminute, nowsecond,
            reshour = -5, resminute = -5, ressecond = -5;

    private GoogleApiClient client;

    LocationManager mLocationManager;

    Context context = this;

    SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent intent = getIntent();
        preferences = getSharedPreferences("preferenceSample", MODE_PRIVATE);

        final SharedPreferences.Editor editor = preferences.edit();
        //editor.putInt("CurrentState", currentState );

        //currentState = preferences.getInt(KEY_CURRENT_STATE, 0);

        final int hourOfDay = preferences.getInt("HourOfDay", 0);
        final int minute = preferences.getInt("Minute", 0);
        final int second = preferences.getInt("Second", 0);


        //テキストやボタンたち
        TimeText = (TextView) findViewById(R.id.timetext);
        CountText = (TextView) findViewById(R.id.counttext);
        PassText = (TextView) findViewById(R.id.passtext);
        GoButton = (Button) findViewById(R.id.gobutton);

        //GPS関係
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
        String provider = mLocationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationManager.requestLocationUpdates(provider, 0, 0, this);

        //家から出たボタン視覚化
        GoButton.setVisibility(View.VISIBLE);

        //設定した時刻を表示
        TimeText.setText(hourOfDay + "時" + minute + "分に出発です");


        //タイマーのリセット
        if (null != CountTimer) {
            CountTimer.cancel();
            CountTimer = null;
        }

        if (null != PassTimer) {
            PassTimer.cancel();
            PassTimer = null;
            passsecond = -1;
            passminute = 0;
        }

        //タイマーの処理
        CountTimer = new Timer();

        CountTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //現在時刻の取得
                        Calendar calendar = Calendar.getInstance();
                        nowhour = calendar.get(Calendar.HOUR_OF_DAY);
                        nowminute = calendar.get(Calendar.MINUTE);
                        nowsecond = calendar.get(Calendar.SECOND);

                        //設定時刻と現在時刻の差
                        reshour = hourOfDay - nowhour;
                        resminute = minute - nowminute;
                        ressecond = second - nowsecond;
                        //マイナス時刻になったら修正
                        if (ressecond < 0) {
                            ressecond += 60;
                            resminute--;
                        }
                        if (resminute < 0) {
                            resminute += 60;
                            reshour--;
                        }
                        if (reshour < 0) {
                            reshour += 24;
                        }

                        //残り時間の表示
                        CountText.setText("残り" + String.format("%1$02d:%2$02d:%3$02d",
                                reshour, resminute, ressecond));

                        currentState = preferences.getInt(KEY_CURRENT_STATE, 0);

                        //各残り時間ごとの言葉
                        // 1時間以上の時
                        if (reshour > 0) {
                            PassText.setText("まだまだ余裕ですね");
                            if (currentState != REST_OVER_1HOUR_LARGER) {
                                currentState = REST_OVER_1HOUR_LARGER;
                                editor.putInt(KEY_CURRENT_STATE, currentState);
                                notitu = LIMIT_1HOUR_LARGER;
                                buildNotification();
                                editor.apply();
                            }
                        }
                        if (reshour == 0 && resminute >= 30 && resminute  <= 59) {
                            PassText.setText("まだまだ余裕ですね");
                            if (currentState != REST_OVER_1HOUR) {
                                currentState = REST_OVER_1HOUR;
                                editor.putInt(KEY_CURRENT_STATE, currentState);
                                notitu = LIMIT_1HOUR;
                                buildNotification();
                                editor.apply();

                            }
                        }
                        if (reshour == 0 && resminute >= 10 && resminute <= 29) {
                            PassText.setText("時間が近づいてきました");
                            if (currentState != REST_OVER_30MINUTE) {
                                currentState = REST_OVER_30MINUTE;
                                editor.putInt(KEY_CURRENT_STATE, currentState);
                                notitu = LIMIT_30MINUTE;
                                buildNotification();
                                editor.apply();
                            }
                        }
                        if (reshour == 0 && resminute >= 5 && resminute <= 9) {
                            PassText.setText("時間が近づいてきました。");
                            if (currentState != REST_OVER_10MINUTE) {
                                currentState = REST_OVER_10MINUTE;
                                editor.putInt(KEY_CURRENT_STATE, currentState);
                                notitu = LIMIT_10MINUTE;
                                buildNotification();
                                editor.apply();
                            }
                        }
                        if (reshour == 0 && resminute >= 2 && resminute <= 4) {
                            PassText.setText("時間がありません。急いでください!");
                            if (currentState != REST_OVER_5MINUTE) {
                                currentState = REST_OVER_5MINUTE;
                                editor.putInt(KEY_CURRENT_STATE, currentState);
                                notitu = LIMIT_5MINUTE;
                                buildNotification();
                                editor.apply();
                            }
                        }
                        if (reshour == 0 && resminute == 1) {
                            PassText.setText("まだですか?急げ!");
                            if (currentState != REST_OVER_2MINUTE){
                                currentState = REST_OVER_2MINUTE;
                                editor.putInt(KEY_CURRENT_STATE, currentState);
                                notitu = LIMIT_2MINUTE;
                                buildNotification();
                                editor.apply();
                            }
                        }
                        if (reshour == 0 && resminute == 0){
                            PassText.setText("急げ!急げ!急げ!!");
                            if (currentState != REST_OVER_1MINUTE){
                                currentState = REST_OVER_1MINUTE;
                                editor.putInt(KEY_CURRENT_STATE, currentState);
                                notitu = LIMIT_1MINUTE;
                                buildNotification();
                                editor.apply();

                            }
                        }



                        //全部ゼロになったら
                        if ((reshour == 0) && (resminute == 0) && (ressecond == 0)) {

                            if (currentState != REST_OVER_PASS) {
                                currentState = REST_OVER_PASS;
                                editor.putInt(KEY_CURRENT_STATE, currentState);
                                notitu = LIMIT_PASS;
                                buildNotification();
                                editor.apply();
                            }

                            //いろいろリセット
                            CountTimer.cancel();
                            CountTimer = null;

                            //一応過ぎたタイマーもリセットしとく
                            if (null != PassTimer) {
                                PassTimer.cancel();
                                PassTimer = null;
                            }
                            //過ぎたタイマーの処理
                            PassTimer = new Timer();

                            PassTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            //過ぎた秒のカウント
                                            passsecond += 1;
                                            if (passsecond >= 60) {
                                                passsecond -= 60;
                                                passminute += 1;
                                            }

                                            //杉田病の表示
                                            PassText.setText(String.format("%1$02d:%2$02d",
                                                    passminute, passsecond) + "過ぎてます!!");

                                        }
                                    });
                                }
                            }, 0, 1000);
                        }

                        //家から出たボタンの処理(一応)
                        GoButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //時間過ぎてなかったら
                                if (CountTimer != null) {
                                    //その時用のアクティビティに飛ぶ
                                    Intent intent = new Intent(getApplication(), ResResultActivity.class);
                                    //時、分、秒を渡す
                                    intent.putExtra("ResHour", reshour);
                                    intent.putExtra("ResMinute", resminute);
                                    intent.putExtra("ResSecond", ressecond);
                                    startActivity(intent);

                                    reshour = -5;
                                    resminute = -5;
                                    ressecond = -5;
                                    CountTimer.cancel();
                                    CountTimer = null;

                                    if (endnoti == 0){
                                        notitu = END_NOTI;
                                        buildNotification();
                                        endnoti = 1;

                                    }

                                    //時間が過ぎていたら
                                } else if (PassTimer != null) {
                                    //その時用のアクティビティに飛ぶ
                                    Intent intent = new Intent(getApplication(), PassResultActivity.class);
                                    intent.putExtra("PassMinute", passminute);
                                    intent.putExtra("PassSecond", passsecond);
                                    startActivity(intent);

                                    reshour = -5;
                                    resminute = -5;
                                    ressecond = -5;
                                    PassTimer.cancel();
                                    PassTimer = null;

                                    if (endnoti == 0){
                                        notitu = END_NOTI;
                                        buildNotification();
                                        endnoti = 1;

                                    }

                                }

                            }
                        });


                    }
                });
            }
        }, 0, 1000);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


    }

    private boolean showingNotification = false;

    private void buildNotification() {

        Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
        Intent intentresres = new Intent(getApplicationContext(), ResResultActivity.class);
        Intent intentpasres = new Intent(getApplicationContext(), PassResultActivity.class);
        Intent intentend = new Intent(getApplicationContext(), EndActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        PendingIntent pendingIntentend = PendingIntent.getActivity(context, 0, intentend, 0);
        PendingIntent pendingIntentresres = PendingIntent.getActivity(context, 0, intentresres, 0);
        PendingIntent pendingIntentpasres = PendingIntent.getActivity(context, 0, intentpasres, 0);



        if (notitu == LIMIT_1HOUR_LARGER) {
            Notification notification = new Notification.Builder(getApplicationContext())
                    .setContentTitle("残り1時間以上あります。")    //  タイトルです（太字）。
                    .setContentText("まだまだ余裕ですね")    //  メッセージテキストです。
                    .setContentIntent(pendingIntent)    //  タップされた時に発行するインテントを指定します。
                    .setAutoCancel(false)    //  タップされた時に、通知バーから消去する場合はtrueにします。
                    .setSmallIcon(R.mipmap.ic_launcher)    //  左側のアイコン画像です。
                    .setLights(Color.GREEN, 1000, 2000)
                    .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                    .setWhen(System.currentTimeMillis())
                    .build();

            notification.flags = Notification.FLAG_NO_CLEAR;
            NotificationManagerCompat nm = NotificationManagerCompat.from(getApplicationContext());
            nm.notify(0, notification);

        }
        if (notitu == LIMIT_1HOUR) {
            Notification notification = new Notification.Builder(getApplicationContext())
                    .setContentTitle("残り1時間あります。")    //  タイトルです（太字）。
                    .setContentText("まだまだ余裕ですね")    //  メッセージテキストです。
                    .setContentIntent(pendingIntent)    //  タップされた時に発行するインテントを指定します。
                    .setAutoCancel(false)    //  タップされた時に、通知バーから消去する場合はtrueにします。
                    .setSmallIcon(R.mipmap.ic_launcher)    //  左側のアイコン画像です。
                    .setLights(Color.GREEN, 1000, 2000)
                    .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                    .setWhen(System.currentTimeMillis())
                    .build();

            notification.flags = Notification.FLAG_NO_CLEAR;
            NotificationManagerCompat nm = NotificationManagerCompat.from(getApplicationContext());
            nm.notify(0, notification);
        }
        if (notitu == LIMIT_30MINUTE) {
            Notification notification = new Notification.Builder(getApplicationContext())
                    .setContentTitle("残り30分です。")    //  タイトルです（太字）。
                    .setContentText("まだ余裕ですね")    //  メッセージテキストです。
                    .setContentIntent(pendingIntent)    //  タップされた時に発行するインテントを指定します。
                    .setAutoCancel(false)    //  タップされた時に、通知バーから消去する場合はtrueにします。
                    .setSmallIcon(R.mipmap.ic_launcher)    //  左側のアイコン画像です。
                    .setLights(Color.GREEN, 1000, 2000)
                    .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                    .setWhen(System.currentTimeMillis())
                    .build();
            notification.flags = Notification.FLAG_NO_CLEAR;
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nm.notify(0, notification);

        }
        if (notitu == LIMIT_10MINUTE) {
            Notification notification = new Notification.Builder(getApplicationContext())
                    .setContentTitle("残り10分です。")    //  タイトルです（太字）。
                    .setContentText("時間が近づいてきました。")    //  メッセージテキストです。
                    .setContentIntent(pendingIntent)    //  タップされた時に発行するインテントを指定します。
                    .setAutoCancel(false)    //  タップされた時に、通知バーから消去する場合はtrueにします。
                    .setSmallIcon(R.mipmap.ic_launcher)    //  左側のアイコン画像です。
                    .setLights(Color.GREEN, 1000, 2000)
                    .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                    .setWhen(System.currentTimeMillis())
                    .build();
            notification.flags = Notification.FLAG_NO_CLEAR;
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nm.notify(0, notification);

        }
        if (notitu == LIMIT_5MINUTE) {
            Notification notification = new Notification.Builder(getApplicationContext())
                    .setContentTitle("残り5分です。")    //  タイトルです（太字）。
                    .setContentText("時間がありません。急いでください!")    //  メッセージテキストです。
                    .setContentIntent(pendingIntent)    //  タップされた時に発行するインテントを指定します。
                    .setAutoCancel(false)    //  タップされた時に、通知バーから消去する場合はtrueにします。
                    .setSmallIcon(R.mipmap.ic_launcher)    //  左側のアイコン画像です。
                    .setLights(Color.GREEN, 1000, 2000)
                    .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                    .setWhen(System.currentTimeMillis())
                    .build();
            notification.flags = Notification.FLAG_NO_CLEAR;
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nm.notify(0, notification);

        }
        if (notitu == LIMIT_2MINUTE) {
            Notification notification = new Notification.Builder(getApplicationContext())
                    .setContentTitle("残り2分です。")    //  タイトルです（太字）。
                    .setContentText("まだですか?急げ!")    //  メッセージテキストです。
                    .setContentIntent(pendingIntent)    //  タップされた時に発行するインテントを指定します。
                    .setAutoCancel(false)    //  タップされた時に、通知バーから消去する場合はtrueにします。
                    .setSmallIcon(R.mipmap.ic_launcher)    //  左側のアイコン画像です。
                    .setLights(Color.GREEN, 1000, 2000)
                    .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                    .setWhen(System.currentTimeMillis())
                    .build();
            notification.flags = Notification.FLAG_NO_CLEAR;
            NotificationManagerCompat nm = NotificationManagerCompat.from(getApplicationContext());
            nm.notify(0, notification);

        }
        if (notitu == LIMIT_1MINUTE) {
            Notification notification = new Notification.Builder(getApplicationContext())
                    .setContentTitle("残り1分です。")    //  タイトルです（太字）。
                    .setContentText("急げ!急げ!急げ!!")    //  メッセージテキストです。
                    .setContentIntent(pendingIntent)    //  タップされた時に発行するインテントを指定します。
                    .setAutoCancel(false)    //  タップされた時に、通知バーから消去する場合はtrueにします。
                    .setSmallIcon(R.mipmap.ic_launcher)    //  左側のアイコン画像です。
                    .setLights(Color.GREEN, 1000, 2000)
                    .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                    .setWhen(System.currentTimeMillis())
                    .build();
            notification.flags = Notification.FLAG_NO_CLEAR;
            NotificationManagerCompat nm = NotificationManagerCompat.from(getApplicationContext());
            nm.notify(0, notification);

        }

        if (notitu == LIMIT_PASS) {
            Notification notification = new Notification.Builder(getApplicationContext())
                    .setContentTitle("時間過ぎてます!")    //  タイトルです（太字）。
                    .setContentText("急いでください!")    //  メッセージテキストです。
                    .setContentIntent(pendingIntent)    //  タップされた時に発行するインテントを指定します。
                    .setAutoCancel(false)    //  タップされた時に、通知バーから消去する場合はtrueにします。
                    .setSmallIcon(R.mipmap.ic_launcher)    //  左側のアイコン画像です。
                    .setLights(Color.GREEN, 1000, 2000)
                    .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                    .setWhen(System.currentTimeMillis())
                    .build();
            notification.flags = Notification.FLAG_NO_CLEAR;
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nm.notify(0, notification);

        }
        if (notitu == GO_HOUSE) {
            if (PassTimer == null) {
                Notification notification = new Notification.Builder(getApplicationContext())
                        .setContentTitle("家から出ました!")    //  タイトルです（太字）。
                        .setContentText("タップで結果へ")//  メッセージテキストです。
                        .setContentIntent(pendingIntentresres)    //  タップされた時に発行するインテントを指定します。
                        .setAutoCancel(true)    //  タップされた時に、通知バーから消去する場合はtrueにします。
                        .setSmallIcon(R.mipmap.ic_launcher)//  左側のアイコン画像です。
                        .setLights(Color.GREEN, 1000, 2000)
                        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                        .setWhen(System.currentTimeMillis())
                        .build();
                notification.flags = Notification.FLAG_NO_CLEAR;
                NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                nm.notify(0, notification);


            }
            if (PassTimer != null) {
                Notification notification = new Notification.Builder(getApplicationContext())
                        .setContentTitle("家から出ました!")    //  タイトルです（太字）。
                        .setContentText("タップで結果へ")//  メッセージテキストです。
                        .setContentIntent(pendingIntentpasres)    //  タップされた時に発行するインテントを指定します。
                        .setAutoCancel(true)    //  タップされた時に、通知バーから消去する場合はtrueにします。
                        .setSmallIcon(R.mipmap.ic_launcher)//  左側のアイコン画像です。
                        .setLights(Color.GREEN, 1000, 2000)
                        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                        .setWhen(System.currentTimeMillis())
                        .build();
                notification.flags = Notification.FLAG_NO_CLEAR;
                NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                nm.notify(0, notification);

            }
        }

        if (notitu == END_NOTI){
            Notification notification = new Notification.Builder(getApplicationContext())
                    .setContentTitle("今日は終了です")    //  タイトルです（太字）。
                    .setContentText("今日も一日頑張りましょう!")//  メッセージテキストです。
                    .setContentIntent(pendingIntentend)    //  タップされた時に発行するインテントを指定します。
                    .setAutoCancel(true)    //  タップされた時に、通知バーから消去する場合はtrueにします。
                    .setSmallIcon(R.mipmap.ic_launcher)//  左側のアイコン画像です。
                    //.setLights(Color.GREEN, 1000, 2000)
                    //.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                    .setWhen(System.currentTimeMillis())
                    .build();
            //notification.flags = Notification.FLAG_NO_CLEAR;
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nm.notify(0, notification);
        }

//        //LEDライト LEDは画面がOFFの時に出力してる
//        builder.setDefaults(Notification.DEFAULT_LIGHTS); //この行を削除すると点滅しつづける。
//        builder.setLights(Color.GREEN, 1000, 2000); //発光色,発光時間,消灯時間
//
//        //通知の音とバイブ
//        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
//        builder.setAutoCancel(false);//クリックで通知バーから削除
//        NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
//        manager.notify(0, builder.build());

    }

    //GPSが有効じゃなかったら設定画面を開く
    private void chkGpsService() {

        //GPSセンサーが利用可能か？
        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("GPSが有効になっていません。" +
                    "\n有効化すれば家から出たのを自動で検知します\n有効化しますか?")
                    .setCancelable(false)

                    //GPS設定画面起動用ボタンとイベントの定義
                    .setPositiveButton("GPS設定画面",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent callGPSSettingIntent = new Intent(
                                            Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivity(callGPSSettingIntent);
                                }
                            });
            //キャンセルボタン処理
            alertDialogBuilder.setNegativeButton("キャンセル",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = alertDialogBuilder.create();
            // 設定画面へ移動するかの問い合わせダイアログを表示
            alert.show();
        }
    }

    // OnResumeでの呼び出しはこんな感じです。
    @Override
    protected void onResume() {
        if (mLocationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
//                LocationManager.NETWORK_PROVIDER,
                    1000,
                    0,
                    this);
        }

        chkGpsService();

        super.onResume();
    }

    // GPSを取得した時の最初の座標、最新の座標の変数
    double inilati = 0;
    double inilongi = 0;
    double newlati = 0;
    double newlongi = 0;
    //最初の座標と細心の座標の差の変数
    double minlati, minlongi;
    //差の指定(±この数値超えたら画面変わる)
    double optmin = 0.00007;
    //ダイアログは一回しか出さない
    int once = 0;

    @Override
    public void onLocationChanged(Location location) {

        //初期座標が0のときだけ初期座標を取得
        if ((inilati == 0) && (inilongi == 0)) {
            inilati = location.getLatitude();
            inilongi = location.getLongitude();
        }

        //最新の座標は最初からとり続ける
        newlati = location.getLatitude();
        newlongi = location.getLongitude();

        //最新座標と初期座標の差
        minlati = newlati - inilati;
        minlongi = newlongi - inilongi;


        //ダイアログがまだなくて指定の数値以上座標がずれたら
        if ((once == 0) && ((minlati >= optmin) || (minlati <= -optmin) ||
                (minlongi >= optmin) || (minlongi <= -optmin))) {

            //ダイアログを出さないようにプラスする
            once++;
            buildNotification();
            notitu = GO_HOUSE;

            //ここでタイマーとめないとうまくいかない
            if (CountTimer != null) {
                CountTimer.cancel();
            } else if (PassTimer != null) {
                PassTimer.cancel();
            }


//            Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
//            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//
//
//            //簡単なやりかた
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
//
//            builder.setSmallIcon(R.mipmap.ic_launcher);
//            builder.setContentTitle("家から出発しました!");
//            builder.setContentText("");
//            //                                builder.setSubText("Sub text");
//            //                                builder.setContentInfo("Info");
//            builder.setWhen(System.currentTimeMillis());
//
//            //クリックされたらIntentへ飛ばす
//            builder.setContentIntent(pendingIntent);
//
//            //バイブレーションがなる
//            final Vibrator v2 = (Vibrator) getSystemService(VIBRATOR_SERVICE);
//            final long[] patternG = {0, 300, 100, 300};
//            v2.vibrate(patternG, -1);
//
//            //LEDライト LEDは画面がOFFの時に出力してる
//            builder.setDefaults(Notification.DEFAULT_LIGHTS); //この行を削除すると点滅しつづける。
//            builder.setLights(Color.GREEN, 1000, 2000); //発光色,発光時間,消灯時間
//
//            //通知の音とバイブ
//            builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
//
//            builder.setAutoCancel(true);//クリックで通知バーから削除
//
//            NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
//            manager.notify(0, builder.build());



            //家から出たときのダイアログメッセージ
            AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
            builder2.setMessage("家から出た!!")
                    .setPositiveButton("結果へ", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //時間過ぎてなかったら
                            if (CountTimer != null) {
                                //その時用のアクティビティに飛ぶ
                                Intent intent = new Intent(getApplication(), ResResultActivity.class);
                                //データを渡す
                                intent.putExtra("ResHour", reshour);
                                intent.putExtra("ResMinute", resminute);
                                intent.putExtra("ResSecond", ressecond);
                                startActivity(intent);

                                //リセット
                                reshour = -5;
                                resminute = -5;
                                ressecond = -5;
                                CountTimer.cancel();
                                CountTimer = null;

                                if (endnoti == 0){
                                    notitu = END_NOTI;
                                    buildNotification();
                                    endnoti = 1;

                                }

                                //時間過ぎてたら
                            } else if (PassTimer != null) {
                                //その時用のアクティビティに飛ぶ
                                Intent intent = new Intent(getApplication(), PassResultActivity.class);
                                intent.putExtra("PassMinute", passminute);
                                intent.putExtra("PassSecond", passsecond);
                                startActivity(intent);

                                reshour = -5;
                                resminute = -5;
                                ressecond = -5;
                                CountTimer.cancel();
                                CountTimer = null;
                                PassTimer.cancel();
                                PassTimer = null;

                                if (endnoti == 0){
                                    notitu = END_NOTI;
                                    buildNotification();
                                    endnoti = 1;

                                }

                            }


                        }
                    });

            builder2.show();


        }

    }


    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Disable Back key
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }

}
