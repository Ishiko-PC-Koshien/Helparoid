package com.stonetech.helparoid;

import android.Manifest;
import android.app.TimePickerDialog;
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
import android.widget.TimePicker;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    // 時刻設定ダイアログのインスタンスを格納する変数
    private TimePickerDialog.OnTimeSetListener varTimeSetListener;

    Button SelTimeButton;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SelTimeButton = (Button) findViewById(R.id.seltime);

        //たぶん時刻設定ダイアログのやつ
        varTimeSetListener
                = new TimePickerDialog.OnTimeSetListener() {

            // 時刻設定ダイアログの[OK]ボタンがクリックされたときの処理
            public void onTimeSet(
                    TimePicker view, final int hourOfDay, final int minute) {
                final int second = 0;

                Intent intent = new Intent(getApplication(), Main2Activity.class);
                intent.putExtra("HourOfDay", hourOfDay);
                intent.putExtra("Minute", minute);
                intent.putExtra("Second", second);
                startActivity(intent);





            }

        };



        //時刻設定ボタンを押したときの処理
        SelTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 現在時刻を取得
                Calendar calendar = Calendar.getInstance();
                // 時刻設定ダイアログのインスタンスを生成
                TimePickerDialog timeDialog = new TimePickerDialog(
                        MainActivity.this,
                        varTimeSetListener,
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        false);
                // 時刻設定ダイアログを表示
                timeDialog.show();
            }
        });



    }


}

