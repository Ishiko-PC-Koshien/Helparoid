package com.stonetech.mezamamamama211;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public int year, month, date, hour, minute, second, msecond;
    public int year_h, month_h, date_h, hour_h, minute_h;       //比較用

    String readToggle;
    String readString,readString2,readString3,readString4,readSt,readSt2;
    private static final int bid1 = 1;
    private static final int bid2 = 2;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  bagnaosi();
        onOff();

        final FrameLayout btn = (FrameLayout) findViewById(R.id.frame);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), SubActivity.class);
                startActivity(intent);

                overridePendingTransition(R.animator.slide_in_right, R.animator.slide_out_left);

                finish();
            }
        });

        yomikomi();

        textView = (TextView)findViewById(R.id.textView3);
        // textView.setText(" "+readString4+"日"+readString+":"+readString2);

        ToggleButton ttbuton= (ToggleButton)findViewById(R.id.toggleButton);

        System.out.println("["+readString3+"]");

        ttbuton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sendNotification();
                    System.out.println("toggleButtonをon:1");

                    toggleRead();
                    if(Integer.parseInt(readToggle) == 2) {
                        timeOn();
                    }
                    textView.setText(" "+readString4+"日の"+readString+":"+readString2);
                    //タイマー起動
                    // Write
                    {
                        try {
                            FileOutputStream fileOutputStream2 = openFileOutput("toggle.txt", MODE_PRIVATE);
                            String writeString2 = "1";
                            fileOutputStream2.write(writeString2.getBytes());
                            System.out.println("toggleButtonをon書き込み:1");
                        } catch (FileNotFoundException e2) {
                        } catch (IOException e2) {
                        }
                    }

                } else {

                    textView.setText(" "+readString+":"+readString2);
                    System.out.println("toggleButtonをoff:2");

                    Intent indent = new Intent(getApplicationContext(), AlarmBroadcastReceiver.class);
                    PendingIntent pending = PendingIntent.getBroadcast(getApplicationContext(), bid2, indent, 0);

                    // アラームを解除する
                    AlarmManager am = (AlarmManager)MainActivity.this.getSystemService(ALARM_SERVICE);
                    am.cancel(pending);

                    Intent indentt = new Intent(getApplicationContext(), BroadcastReceiverSleep.class);
                    PendingIntent pendingg = PendingIntent.getBroadcast(getApplicationContext(), 300, indentt, 0);

                    AlarmManager amm = (AlarmManager)MainActivity.this.getSystemService(ALARM_SERVICE);
                    amm.cancel(pendingg);
                    Toast.makeText(getApplicationContext(), "アラームをOFFにしました", Toast.LENGTH_SHORT).show();
                    // Write
//                    //タイマーをキャンセル
//
//                    AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE) ;
//                    Intent intent = new Intent(getApplicationContext(),AlarmBroadcastReceiver.class);
//                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0,intent,0);
//                    pendingIntent.cancel();
//                    alarmManager.cancel(pendingIntent);


                    {
                        try {
                            FileOutputStream fileOutputStream2 = openFileOutput("toggle.txt", MODE_PRIVATE);
                            String writeString2 = "2";
                            fileOutputStream2.write(writeString2.getBytes());
                            System.out.println("toggleButtonをoff書き込み:2");
                        } catch (FileNotFoundException e2) {
                        } catch (IOException e2) {
                        }
                    }
                    cancel();
                }
            }
        });

        if (Integer.parseInt(readString3) == 1){
            ttbuton.setChecked(true);
            System.out.println("on　初期値");
        }else if(Integer.parseInt(readString3) == 2){
            ttbuton.setChecked(false);
            System.out.println("off 初期値");
        }
    }

    NotificationManager nm = null;

    private void sendNotification() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
        Calendar calender_2 = Calendar.getInstance();
        int nowTime = calender_2.get(Calendar.DAY_OF_MONTH);
        Notification notification = new Notification.Builder(this)
                .setContentTitle("アラーム設定")
                .setContentText((Integer.parseInt(readString4)-nowTime)+"日後の"+readString+":"+readString2+"にタイマーが作動します")
                .setContentIntent(pi)
                .setSmallIcon(R.drawable.time_icon)
                .setAutoCancel(true)
                .build();

        notification.flags = Notification.FLAG_ONGOING_EVENT;

        nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(1, notification); // 設定したNotificationを通知する


    }

    public void cancel(){
        nm.cancel(1);

    }


    public void yomikomi(){

        // Read
        {
            try {
                FileInputStream fileInputStream;
                fileInputStream = openFileInput("date.txt");
                byte[] readBytes = new byte[fileInputStream.available()];
                fileInputStream.read(readBytes);
                readString4 = new String(readBytes);
                Log.v("readString", readString4);
            } catch (FileNotFoundException e) {

                // Write
                {
                    try {
                        FileOutputStream fileOutputStream = openFileOutput("date.txt", MODE_PRIVATE);
                        String writeString = "17";
                        fileOutputStream.write(writeString.getBytes());
                    } catch (FileNotFoundException e2) {
                    } catch (IOException e2) {
                    }
                }


            } catch (IOException e) {
            }
        }

        // Read
        {
            try {
                FileInputStream fileInputStream;
                fileInputStream = openFileInput("hour.txt");
                byte[] readBytes = new byte[fileInputStream.available()];
                fileInputStream.read(readBytes);
                readString = new String(readBytes);
                Log.v("readString", readString);
            } catch (FileNotFoundException e) {

                // Write
                {
                    try {
                        FileOutputStream fileOutputStream = openFileOutput("hour.txt", MODE_PRIVATE);
                        String writeString = "7";
                        fileOutputStream.write(writeString.getBytes());
                    } catch (FileNotFoundException e2) {
                    } catch (IOException e2) {
                    }
                }


            } catch (IOException e) {
            }
        }

        // Read
        {
            try {
                FileInputStream fileInputStream;
                fileInputStream = openFileInput("minute.txt");
                byte[] readBytes = new byte[fileInputStream.available()];
                fileInputStream.read(readBytes);
                readString2 = new String(readBytes);
                Log.v("readString", readString);
            } catch (FileNotFoundException e) {

                // Write
                {
                    try {
                        FileOutputStream fileOutputStream2 = openFileOutput("minute.txt", MODE_PRIVATE);
                        String writeString2 = "30";
                        fileOutputStream2.write(writeString2.getBytes());
                    } catch (FileNotFoundException e2) {
                    } catch (IOException e2) {
                    }
                }


            } catch (IOException e) {
            }
        }

        // Read
        {
            try {
                FileInputStream fileInputStream;
                fileInputStream = openFileInput("hour.txt");
                byte[] readBytes = new byte[fileInputStream.available()];
                fileInputStream.read(readBytes);
                readString = new String(readBytes);
                Log.v("readString", readString);
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }
        }

        // Read
        {
            try {
                FileInputStream fileInputStream;
                fileInputStream = openFileInput("minute.txt");
                byte[] readBytes = new byte[fileInputStream.available()];
                fileInputStream.read(readBytes);
                readString2 = new String(readBytes);
                Log.v("readString", readString2);
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }
        }

        // Read
        {
            try {
                FileInputStream fileInputStream;
                fileInputStream = openFileInput("date.txt");
                byte[] readBytes = new byte[fileInputStream.available()];
                fileInputStream.read(readBytes);
                readString4 = new String(readBytes);
                Log.v("readString", readString4);
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }
        }
    }

    public void onOff(){
        System.out.println("onOf_void");

        // Read
        {
            try {
                FileInputStream fileInputStream;
                fileInputStream = openFileInput("toggle.txt");
                byte[] readBytes = new byte[fileInputStream.available()];
                fileInputStream.read(readBytes);
                readString3 = new String(readBytes);
                Log.v("readString", readString3);
            } catch (FileNotFoundException e) {

                // Write
                {
                    try {
                        FileOutputStream fileOutputStream2 = openFileOutput("toggle.txt", MODE_PRIVATE);
                        String writeString2 = "2";
                        fileOutputStream2.write(writeString2.getBytes());
                    } catch (FileNotFoundException e2) {
                    } catch (IOException e2) {
                    }
                }

            } catch (IOException e) {
            }
        }

    }





    public void timeOn(){
        yomi();
        Calendar cal = Calendar.getInstance();
        // 協定世界時 (UTC)です適宜設定してください
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH); // 4=>5月
        date = cal.get(Calendar.DAY_OF_MONTH);;
        hour = Integer.parseInt(readSt);
        minute = Integer.parseInt(readSt2);
        second = 0;
        msecond = 0;

        timenituite();
        // dateDay();

        Calendar calendar2 = Calendar.getInstance();
        // 過去の時間は即実行されます
        calendar2.set(Calendar.YEAR, year);
        calendar2.set(Calendar.MONTH, month);
        calendar2.set(Calendar.DATE, date);
        calendar2.set(Calendar.HOUR_OF_DAY, hour);
        calendar2.set(Calendar.MINUTE, minute );
        calendar2.set(Calendar.SECOND, second);
        calendar2.set(Calendar.MILLISECOND, msecond);

        Intent intent = new Intent(getApplicationContext(), AlarmBroadcastReceiver.class);
        intent.putExtra("intentId", 2);
        PendingIntent pending = PendingIntent.getBroadcast(getApplicationContext(), bid2, intent, 0);

        // アラームをセットする
        AlarmManager am = (AlarmManager)MainActivity.this.getSystemService(ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), pending);
        // Toast.makeText(getApplicationContext(), "ALARM 2", Toast.LENGTH_SHORT).show();

        String setTime = "設定時間(UTC)："+year+"/"+(month+1)+"/"+date+" "+hour+":"+minute+":"+second+"."+msecond;
        textView.setText(setTime);          //イか月ずれてる

        kaki();
        Intent intent1 = new Intent(getApplication(), MainActivity.class);
        startActivity(intent1);
        yomi();

        Toast.makeText(getApplicationContext(), (date-date_h)+"日後の"+readString+":"+readString2+"に時間を設定しました", Toast.LENGTH_SHORT).show();
    }

    public void kaki(){
        // Write
        {
            try {
                FileOutputStream fileOutputStream2 = openFileOutput("minute.txt", MODE_PRIVATE);
                String writeString2 = String.valueOf(minute);
                fileOutputStream2.write(writeString2.getBytes());
            } catch (FileNotFoundException e2) {
            } catch (IOException e2) {
            }
        }

        // Write
        {
            try {
                FileOutputStream fileOutputStream2 = openFileOutput("hour.txt", MODE_PRIVATE);
                String writeString2 = String.valueOf(hour);
                fileOutputStream2.write(writeString2.getBytes());
            } catch (FileNotFoundException e2) {
            } catch (IOException e2) {
            }
        }
    }

    public void yomi(){
        // Read
        {
            try {
                FileInputStream fileInputStream;
                fileInputStream = openFileInput("hour.txt");
                byte[] readBytes = new byte[fileInputStream.available()];
                fileInputStream.read(readBytes);
                readSt = new String(readBytes);
                Log.v("readString", readSt);
            } catch (FileNotFoundException e) {

            } catch (IOException e) {
            }
        }
        // Read
        {
            try {
                FileInputStream fileInputStream;
                fileInputStream = openFileInput("minute.txt");
                byte[] readBytes = new byte[fileInputStream.available()];
                fileInputStream.read(readBytes);
                readSt2 = new String(readBytes);
                Log.v("readString", readSt2);
            } catch (FileNotFoundException e) {

            } catch (IOException e) {
            }
        }
    }

//    public void dateDay(){
//        // Write
//        {
//            try {
//                FileOutputStream fileOutputStream = openFileOutput("date.txt", MODE_PRIVATE);
//                String writeString = String.valueOf(date);
//                fileOutputStream.write(writeString.getBytes());
//            } catch (FileNotFoundException e2) {
//            } catch (IOException e2) {
//            }
//        }
//    }

    public void timenituite(){
        Calendar calender_2 = Calendar.getInstance();
        year_h = calender_2.get(Calendar.YEAR);
        month_h = calender_2.get(Calendar.MONTH);
        date_h = calender_2.get(Calendar.DAY_OF_MONTH);
        hour_h = calender_2.get(Calendar.HOUR_OF_DAY);
        minute_h = calender_2.get(Calendar.MINUTE);

        System.out.println(year_h+"  "+month_h+"  "+date_h+"   "+hour_h+"  "+minute_h);
        System.out.println(year+"  "+month+"  "+date+"   "+hour+"  "+minute);

        if(hour_h == hour ) {         //現在時刻より前
            if (minute_h >= minute) {
                if (month == 0) {         //１月のとき           month 0-11
                    if (date == 31) {      //日付が３１日のとき
                        month += 1;      //月をプラス１
                        date = 1;       //日付を１に戻す
                    } else {              //それ以外　日付プラス１
                        date += 1;
                    }
                } else if (month == 1) {         //2月のとき           month 0-11
                    if (date == 29) {
                        month += 1;
                        date = 1;
                    } else {
                        date += 1;
                    }
                } else if (month == 2) {
                    if (date == 31) {
                        month += 1;
                        date = 1;
                    } else {
                        date += 1;
                    }
                } else if (month == 3) {
                    if (date == 30) {
                        month += 1;
                        date = 1;
                    } else {
                        date += 1;
                    }

                } else if (month == 4) {
                    if (date == 31) {
                        month += 1;
                        date = 1;
                    } else {
                        date += 1;
                    }
                } else if (month == 5) {
                    if (date == 30) {
                        month += 1;
                        date = 1;
                    } else {
                        date += 1;
                    }
                } else if (month == 6) {
                    if (date == 31) {
                        month += 1;
                        date = 1;
                    } else {
                        date += 1;
                    }
                } else if (month == 7) {
                    if (date == 31) {
                        month += 1;
                        date = 1;
                    } else {
                        date += 1;
                    }
                } else if (month == 8) {
                    if (date == 30) {
                        month += 1;
                        date = 1;
                    } else {
                        date += 1;
                    }
                } else if (month == 9) {
                    if (date == 31) {
                        month += 1;
                        date = 1;
                    } else {
                        date += 1;
                    }
                } else if (month == 10) {
                    if (date == 30) {
                        month += 1;
                        date = 1;
                    } else {
                        date += 1;
                    }
                } else if (month == 11) {
                    if (date == 31) {
                        year += 1;
                        month = 1;
                        date = 1;
                    } else {
                        date += 1;
                    }
                }
            }
        }

        if(hour_h > hour){
            if (month == 0) {         //１月のとき           month 0-11
                if (date == 31) {      //日付が３１日のとき
                    month += 1;      //月をプラス１
                    date = 1;       //日付を１に戻す
                } else {              //それ以外　日付プラス１
                    date += 1;
                }
            } else if (month == 1) {         //2月のとき           month 0-11
                if (date == 29) {
                    month += 1;
                    date = 1;
                } else {
                    date += 1;
                }
            } else if (month == 2) {
                if (date == 31) {
                    month += 1;
                    date = 1;
                } else {
                    date += 1;
                }
            } else if (month == 3) {
                if (date == 30) {
                    month += 1;
                    date = 1;
                } else {
                    date += 1;
                }

            } else if (month == 4) {
                if (date == 31) {
                    month += 1;
                    date = 1;
                } else {
                    date += 1;
                }
            } else if (month == 5) {
                if (date == 30) {
                    month += 1;
                    date = 1;
                } else {
                    date += 1;
                }
            } else if (month == 6) {
                if (date == 31) {
                    month += 1;
                    date = 1;
                } else {
                    date += 1;
                }
            } else if (month == 7) {
                if (date == 31) {
                    month += 1;
                    date = 1;
                } else {
                    date += 1;
                }
            } else if (month == 8) {
                if (date == 30) {
                    month += 1;
                    date = 1;
                } else {
                    date += 1;
                }
            } else if (month == 9) {
                if (date == 31) {
                    month += 1;
                    date = 1;
                } else {
                    date += 1;
                }
            } else if (month == 10) {
                if (date == 30) {
                    month += 1;
                    date = 1;
                } else {
                    date += 1;
                }
            } else if (month == 11) {
                if (date == 31) {
                    year += 1;
                    month = 1;
                    date = 1;
                } else {
                    date += 1;
                }
            }
        }

        System.out.println(year+"  "+month+"  "+date+"   "+hour+"  "+minute);
    }

    public void toggleRead(){
        // Read
        {
            try {
                FileInputStream fileInputStream;
                fileInputStream = openFileInput("toggle.txt");
                byte[] readBytes = new byte[fileInputStream.available()];
                fileInputStream.read(readBytes);
                readToggle = new String(readBytes);
                Log.v("readString", readToggle);
            } catch (FileNotFoundException e) {

            } catch (IOException e) {
            }
        }
    }

    public void bagnaosi(){
        // Write
        Calendar calender_20 = Calendar.getInstance();
        int datedada = calender_20.get(Calendar.DAY_OF_MONTH);
        String mi ="30";
        String ho = "5";
        String da = String.valueOf(datedada);

        // Write
        {
            try {
                FileOutputStream fileOutputStream2 = openFileOutput("minute.txt", MODE_PRIVATE);
                String writeString2 = mi;
                fileOutputStream2.write(writeString2.getBytes());
            } catch (FileNotFoundException e2) {
            } catch (IOException e2) {
            }
        }

        // Write
        {
            try {
                FileOutputStream fileOutputStream2 = openFileOutput("hour.txt", MODE_PRIVATE);
                String writeString2 = ho;
                fileOutputStream2.write(writeString2.getBytes());
            } catch (FileNotFoundException e2) {
            } catch (IOException e2) {
            }
        }
        // Write
        {
            try {
                FileOutputStream fileOutputStream2 = openFileOutput("date.txt", MODE_PRIVATE);
                String writeString2 = da;
                fileOutputStream2.write(writeString2.getBytes());
            } catch (FileNotFoundException e2) {
            } catch (IOException e2) {
            }
        }

    }
}
