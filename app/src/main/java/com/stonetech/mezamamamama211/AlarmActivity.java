package com.stonetech.mezamamamama211;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity {

    private static final int bid2 = 2;

    public int year, month, date, hour, minute, second, msecond;

    public int year_h, month_h, date_h, hour_h, minute_h;       //比較用

    String readString,readString2,readString3,readString4,readSt,readSt2;

    MediaPlayer med;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        Window window = getWindow();

        sound();

        Button button = (Button)this.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hikaku();
                susi();

            }
        });





        window.setFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED,WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.setFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD,WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
    }

    //ボタンを押したときの時間を取得。
    //指定した時間を引く
    //その時間が５分以上か確認
    //以上だったら、txtにプラスする

    int hour_A,minute_A;
    int hour_B,minute_B;

    int A,B,C;

    int D; //-1sita,hour

    int syoA,syoB;      //A hour b minute

    public void hikaku(){
        System.out.println(hour_A+":"+minute_A+"____"+ hour_B+":"+minute_B+"_____"+A+"_"+B+"_"+C+"_"+D);

        timeee();
        timuuuu();
        System.out.println(hour_A+":"+minute_A+"____"+ hour_B+":"+minute_B+"_____"+A+"_"+B+"_"+C+"_"+D);
        if(hour_B == hour_A){
            System.out.println(hour_A+":"+minute_A+"____"+ hour_B+":"+minute_B+"_____"+A+"_"+B+"_"+C+"_"+D);
            A = minute_B - minute_A;
            System.out.println(hour_A+":"+minute_A+"____"+ hour_B+":"+minute_B+"_____"+A+"_"+B+"_"+C+"_"+D);

            if(A > 1){
                B = minute_A - A;
                System.out.println(hour_A+":"+minute_A+"____"+ hour_B+":"+minute_B+"_____"+A+"_"+B+"_"+C+"_"+D);
                D = hour_A;
                if(B <= 0){
                    C = 60 + B;
                    System.out.println(hour_A+":"+minute_A+"____"+ hour_B+":"+minute_B+"_____"+A+"_"+B+"_"+C+"_"+D);

                    B = C;

                    D = hour_A -1;
                    System.out.println(hour_A+":"+minute_A+"____"+ hour_B+":"+minute_B+"_____"+A+"_"+B+"_"+C+"_"+D);

                    if(D == -1){
                        D = 23;            System.out.println(hour_A+":"+minute_A+"____"+ hour_B+":"+minute_B+"_____"+A+"_"+B+"_"+C+"_"+D);

                    }
                    System.out.println(hour_A+":"+minute_A+"____"+ hour_B+":"+minute_B+"_____"+A+"_"+B+"_"+C+"_"+D);
                }

                kakikomeee();       //hourをマイナス１する

            }else if(A < 1){
                syokiti_read();
                syokiti_write();
                System.out.println(hour_A+":"+minute_A+"____"+ hour_B+":"+minute_B+"_____"+A+"_"+B+"_"+C+"_"+D);

            }
        }
    }

    public void syokiti_write(){
        // Write
        {
            try {
                FileOutputStream fileOutputStream2 = openFileOutput("minute.txt", MODE_PRIVATE);
                String writeString2 = String.valueOf(syoB);
                fileOutputStream2.write(writeString2.getBytes());
            } catch (FileNotFoundException e2) {
            } catch (IOException e2) {
            }
        }

        // Write
        {
            try {
                FileOutputStream fileOutputStream2 = openFileOutput("hour.txt", MODE_PRIVATE);
                String writeString2 = String.valueOf(syoA);
                fileOutputStream2.write(writeString2.getBytes());
            } catch (FileNotFoundException e2) {
            } catch (IOException e2) {
            }
        }
    }

    public void syokiti_read(){
        // Read
        {
            try {
                FileInputStream fileInputStream;
                fileInputStream = openFileInput("hour_S.txt");
                byte[] readBytes = new byte[fileInputStream.available()];
                fileInputStream.read(readBytes);
                syoA = Integer.parseInt(new String(readBytes));
            } catch (FileNotFoundException e) {

            } catch (IOException e) {
            }
        }
        // Read
        {
            try {
                FileInputStream fileInputStream;
                fileInputStream = openFileInput("minute_S.txt");
                byte[] readBytes = new byte[fileInputStream.available()];
                fileInputStream.read(readBytes);
                syoB= Integer.parseInt(new String(readBytes));
            } catch (FileNotFoundException e) {

            } catch (IOException e) {
            }
        }
    }

    public void kakikomeee(){
        // Write
        {
            try {
                FileOutputStream fileOutputStream2 = openFileOutput("minute.txt", MODE_PRIVATE);
                String writeString2 = String.valueOf(B);
                fileOutputStream2.write(writeString2.getBytes());
            } catch (FileNotFoundException e2) {
            } catch (IOException e2) {
            }
        }

        // Write
        {
            try {
                FileOutputStream fileOutputStream2 = openFileOutput("hour.txt", MODE_PRIVATE);
                String writeString2 = String.valueOf(D);
                fileOutputStream2.write(writeString2.getBytes());
            } catch (FileNotFoundException e2) {
            } catch (IOException e2) {
            }
        }
    }

    public void timeee(){   //設定した時間
        // Read
        {
            try {
                FileInputStream fileInputStream;
                fileInputStream = openFileInput("hour.txt");
                byte[] readBytes = new byte[fileInputStream.available()];
                fileInputStream.read(readBytes);
                hour_A = Integer.parseInt(new String(readBytes));
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
                minute_A= Integer.parseInt(new String(readBytes));
            } catch (FileNotFoundException e) {

            } catch (IOException e) {
            }
        }
    }

    public void timuuuu(){  //ボタンをおした時間
        Calendar cale = Calendar.getInstance();
        // 協定世界時 (UTC)です適宜設定してください
        hour_B =  cale.get(Calendar.HOUR_OF_DAY);
        minute_B =  cale.get(Calendar.MINUTE);
    }

    public void sound(){
        med = MediaPlayer.create(this, R.raw.se_maoudamashii_onepoint20);
        med.setLooping(true);
        med.start();
    }

    public void susi(){

        med.stop();
        yomikomi();

        Calendar cal = Calendar.getInstance();
        // 協定世界時 (UTC)です適宜設定してください
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH); // 4=>5月
        date = cal.get(Calendar.DAY_OF_MONTH);;
        hour = Integer.parseInt(readSt);
        minute = Integer.parseInt(readSt2);
        second = 0;
        msecond = 0;

        time();
        dateData();

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
        AlarmManager am = (AlarmManager)AlarmActivity.this.getSystemService(ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), pending);
        // Toast.makeText(getApplicationContext(), "ALARM 2", Toast.LENGTH_SHORT).show();

        String setTime = "設定時間(UTC)："+year+"/"+(month+1)+"/"+date+" "+hour+":"+minute+":"+second+"."+msecond;
        //イか月ずれてる

        kakikomi();
        yomikomi();

        Toast.makeText(getApplicationContext(), (date-date_h)+"日後"+readSt+":"+readSt2+"に時間を設定しました", Toast.LENGTH_SHORT).show();


        System.out.print("alarmActivity");
        Intent intent3 = new Intent(getApplication(), Main2Activity.class);
        startActivity(intent3);
        finish();
    }

    public void kakikomi(){
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

    public void yomikomi(){
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

    public void time(){
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

    public void dateData(){
        // Write
        {
            try {
                FileOutputStream fileOutputStream = openFileOutput("date.txt", MODE_PRIVATE);
                String writeString = String.valueOf(date);
                fileOutputStream.write(writeString.getBytes());
            } catch (FileNotFoundException e2) {
            } catch (IOException e2) {
            }
        }

    }
}
