package com.stonetech.helparoid;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class Main2Activity extends AppCompatActivity implements LocationListener {

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

    //バイブレーションのやつ
    int vib5 = 1;
    int vib2 = 1;
    int vib1 = 1;
    int vib0 = 1;

    int hourOfDay, minute, second;

    //今の時間と、佐野時間
    int nowhour, nowminute, nowsecond, reshour, resminute, ressecond;

    ////ロケーションマネージャー
    //LocationManager mLocationManager;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent intent = getIntent();
        hourOfDay = intent.getIntExtra("HourOfDay", 0);
        minute = intent.getIntExtra("Minute", 0);
        second = intent.getIntExtra("Second", 0);

        //テキストやボタンたち
        TimeText = (TextView) findViewById(R.id.timetext);
        CountText = (TextView) findViewById(R.id.counttext);
        PassText = (TextView) findViewById(R.id.passtext);
        GoButton = (Button) findViewById(R.id.gobutton);
        //バイブレーターのパターンとか
        final Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        final long[] pattern1 = {0, 500};
        final long[] pattern2 = {0, 500, 100, 500};
        final long[] pattern3 = {0, 500, 100, 500, 100, 500};

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
            vib5 = 1;
            vib2 = 1;
            vib1 = 1;
            vib0 = 1;
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

                        //各残り時間ごとの言葉
                        if ((reshour >= 1) || (resminute >= 30)) {
                            PassText.setText("まだまだ余裕ですね");
                        } else if ((reshour == 0) && (resminute < 30) && (resminute > 5)) {
                            PassText.setText("時間が近づいてきました");
                            //ここからバイもなる
                        } else if ((reshour == 0) && (resminute <= 4) && (resminute > 2)) {
                            PassText.setText("時間がありません。\n急いでください!");
                            if (vib5 == 1) {
                                v.vibrate(pattern1, -1);
                                vib5 = 0;
                            }

                        } else if ((reshour == 0) && (resminute <= 2) && (resminute > 0)) {
                            PassText.setText("まだですか?急げ!!");
                            if (vib2 == 1) {
                                v.vibrate(pattern2, -1);
                                vib2 = 0;
                            }

                        } else if ((reshour == 0) && (resminute == 0)) {
                            PassText.setText("急げ!急げ!急げ!!");
                            if (vib1 == 1) {
                                v.vibrate(pattern3, -1);
                                vib1 = 0;
                            }

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

                                    //タイマーリセット
                                    CountTimer.cancel();
                                    CountTimer = null;

                                    //バイブレーションのやつもリセット
                                    vib5 = 1;
                                    vib2 = 1;
                                    vib1 = 1;
                                    vib0 = 1;

                                    //時間が過ぎていたら
                                } else if (PassTimer != null) {
                                    //その時用のアクティビティに飛ぶ
                                    Intent intent = new Intent(getApplication(), PassResultActivity.class);
                                    intent.putExtra("PassMinute", passminute);
                                    intent.putExtra("PassSecond", passsecond);
                                    startActivity(intent);

                                    PassTimer.cancel();
                                    PassTimer = null;

                                    vib5 = 1;
                                    vib2 = 1;
                                    vib1 = 1;
                                    vib0 = 1;

                                }

                            }
                        });

                        //全部ゼロになったら
                        if ((reshour == 0) && (resminute == 0) && (ressecond == 0)) {

                            //バイブレーションを1秒鳴らす
                            v.vibrate(1000);

                            //いろいろいリセット
                            CountTimer.cancel();
                            CountTimer = null;
                            vib5 = 1;
                            vib2 = 1;
                            vib1 = 1;
                            vib0 = 1;

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
                    }
                });
            }
        }, 0, 1000);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


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
    double optmin = 0.00003;
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
        if ((once == 0) && ((minlati >= optmin ) || (minlati <= -optmin)  ||
                (minlongi >= optmin) || (minlongi <= -optmin))){

            //ダイアログを出さないようにプラスする
            once ++;

            //ここでタイマーとめないとうまくいかない
            if (CountTimer != null){
                CountTimer.cancel();
            }else if (PassTimer != null) {
                PassTimer.cancel();
            }


            //家から出たときのダイアログメッセージ
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("家から出た!!")
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

                                //一応リセット
                                CountTimer.cancel();
                                CountTimer = null;

                                vib5 = 1;
                                vib2 = 1;
                                vib1 = 1;
                                vib0 = 1;

                                //時間過ぎてたら
                            } else if (PassTimer != null) {
                                //その時用のアクティビティに飛ぶ
                                Intent intent = new Intent(getApplication(), PassResultActivity.class);
                                intent.putExtra("PassMinute", passminute);
                                intent.putExtra("PassSecond", passsecond);
                                startActivity(intent);

                                PassTimer.cancel();
                                PassTimer = null;

                                vib5 = 1;
                                vib2 = 1;
                                vib1 = 1;
                                vib0 = 1;

                            }


                        }
                    });

            builder.show();


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

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
