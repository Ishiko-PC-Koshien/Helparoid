package com.stonetech.mezamamamama211;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class SubActivity extends AppCompatActivity {

    private static final int bid1 = 1;
    private static final int bid2 = 2;

    int sleephour, sleepminute;

    public int year, month, date, hour, minute, second, msecond;

    public int year_h, month_h, date_h, hour_h, minute_h;       //比較用

    public String readString,readString2;

    //EditText editText,editText2;

    TextView WakeUpHourText, WakeUpMinuteText, GoHomeHourText, GoHomeMinuteText;

    Button setTimeWakeUpButton, setTimeGoHomeButton;

    // 時刻設定ダイアログのインスタンスを格納する変数
    private TimePickerDialog.OnTimeSetListener WakeUpsetTime;
    // 時刻設定ダイアログのインスタンスを格納する変数
    private TimePickerDialog.OnTimeSetListener GoHomesetTime;

    int hourX = 5;
    int minuteX = 30;
    //EditText eT3,eT4;
    private static String KEY_CURRENT_STATE = "CurrentState";
    private static int currentState;

    private int time, time2;

    SharedPreferences preferences;

    @Override
    public  void onBackPressed(){
        super.onBackPressed();


//        SharedPreferences.Editor editor0 = preferences.edit();
//        editor0.putInt("HourOfDay", time);
//        editor0.putInt("Minute", time2);

        Intent intent1 = new Intent(getApplication(), MainActivity.class);
        startActivity(intent1);
        overridePendingTransition(R.animator.slide_in_left, R.animator.slide_out_right);
        close();
        Toast.makeText(getApplicationContext(), "キャンセルしました", Toast.LENGTH_SHORT).show();

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        preferences = getSharedPreferences("preferenceSample", MODE_PRIVATE);

        final SharedPreferences.Editor editor1 = preferences.edit();

        Button buton2 = (Button)this.findViewById(R.id.button_sub0);

        buton2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent1 = new Intent(getApplication(), MainActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.animator.slide_in_left, R.animator.slide_out_right);
                close();
                Toast.makeText(getApplicationContext(), "キャンセルしました", Toast.LENGTH_SHORT).show();
            }
        });

        Calendar cal = Calendar.getInstance();

        final TextView textView = (TextView)findViewById(R.id.textView);
        // 協定世界時 (UTC)です適宜設定してください
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH); // 4=>5月
        date = cal.get(Calendar.DAY_OF_MONTH);;
//        hour = 21;
//        minute = 20;
        second = 0;
        msecond = 0;

        //     final int hourOfDayaaaaa = preferences.getInt("HourOfDay", 0);
        //      final int minuteaaaa = preferences.getInt("Minute", 0);

        //editText = (EditText)findViewById(R.id.editText);
        //editText2 = (EditText)findViewById(R.id.editText2);
        yomikomi();
        ///editText.setText(readString);
       //editText2.setText(readString2);

        WakeUpHourText = (TextView)findViewById(R.id.wakeUpHourText);
        WakeUpHourText.setText(readString);
        WakeUpMinuteText = (TextView)findViewById(R.id.wakeUpMinuteText);
        WakeUpMinuteText.setText(readString2);

        time = preferences.getInt("HourOfDay", 8);
        time2 = preferences.getInt("Minute", 30);

        GoHomeHourText = (TextView)findViewById(R.id.goHomeHourText);
        GoHomeHourText.setText(String.format("%1$02d", time));
        GoHomeMinuteText = (TextView)findViewById(R.id.goHomeMinuteText);
        GoHomeMinuteText.setText(String.format("%1$02d", time2));


        WakeUpsetTime = new TimePickerDialog.OnTimeSetListener(){
            // 時刻設定ダイアログの[OK]ボタンがクリックされたときの処理
            @Override
            public void onTimeSet(TimePicker view1, int hourr, int minutee) {

                WakeUpHourText.setText(String.format("%1$02d", hourr));
                WakeUpMinuteText.setText(String.format("%1$02d", minutee));

                editor1.putInt("Hourr", hourr);
                editor1.putInt("Minutee", minutee);
                editor1.commit();

                //Toast.makeText(getApplicationContext(), hourr + ":" + minutee, Toast.LENGTH_SHORT).show();
            }
        };

        GoHomesetTime = new TimePickerDialog.OnTimeSetListener(){
            // 時刻設定ダイアログの[OK]ボタンがクリックされたときの処理
            @Override
            public void onTimeSet(TimePicker view2, int hourrr, int minuteee) {

                time = hourrr;
                time2 = minuteee;

                editor1.putInt("HourOfDay", time);
                editor1.putInt("Minute", time2);
                editor1.commit();

                GoHomeHourText.setText(String.format("%1$02d", time));
                GoHomeMinuteText.setText(String.format("%1$02d", time2));
                
            }
        };

        setTimeWakeUpButton = (Button) findViewById(R.id.timePickerWakeUp);

        setTimeWakeUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int stint1 = Integer.parseInt(readString);
                int stint2 = Integer.parseInt(readString2);

                TimePickerDialog timeDialog = new TimePickerDialog(
                        SubActivity.this,
                        WakeUpsetTime,
                        stint1,stint2,
                        false);
                timeDialog.show();
            }
        });

        setTimeGoHomeButton = (Button) findViewById(R.id.timePickerGoHome);

        setTimeGoHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time = Integer.parseInt(GoHomeHourText.getText().toString());
                time2 = Integer.parseInt(GoHomeMinuteText.getText().toString());
                TimePickerDialog timeDialog = new TimePickerDialog(
                        SubActivity.this,
                        GoHomesetTime,
                        time,time2,
                        false);
                timeDialog.show();
            }
        });


//        eT3.setText(hourOfDayaaaaa);
//        eT4.setText(minuteaaaa);

        // 日時を指定したアラーム
        Button button3 = (Button)this.findViewById(R.id.button_sub3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//               hour = Integer.parseInt(editText.getText().toString());
//               minute = Integer.parseInt(editText2.getText().toString());

                hour = Integer.parseInt(WakeUpHourText.getText().toString());
                minute = Integer.parseInt(WakeUpMinuteText.getText().toString());

                time();
                dateData();

                sleepnotifi();

                syuto();

                Calendar calendar2 = Calendar.getInstance();
                // 過去の時間は即実行されます
                calendar2.set(Calendar.YEAR, year);
                calendar2.set(Calendar.MONTH, month);
                calendar2.set(Calendar.DATE, date);
                calendar2.set(Calendar.HOUR_OF_DAY, hour);
                calendar2.set(Calendar.MINUTE, minute);
                calendar2.set(Calendar.SECOND, second);
                calendar2.set(Calendar.MILLISECOND, msecond);

                Intent intent = new Intent(getApplicationContext(), AlarmBroadcastReceiver.class);
                intent.putExtra("intentId", 2);
                PendingIntent pending = PendingIntent.getBroadcast(getApplicationContext(), bid2, intent, 0);

                // アラームをセットする
                AlarmManager am = (AlarmManager) SubActivity.this.getSystemService(ALARM_SERVICE);
                am.set(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), pending);
                // Toast.makeText(getApplicationContext(), "ALARM 2", Toast.LENGTH_SHORT).show();

                String setTime = "設定時間(UTC)：" + year + "/" + (month + 1) + "/" + date + " " + hour + ":" + minute + ":" + second + "." + msecond;
                textView.setText(setTime);          //イか月ずれてる

                kakikomi();
                kakikomi2();

                Intent intent1 = new Intent(getApplication(), MainActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.animator.slide_in_under, R.animator.slide_out_ue);
                yomikomi();

                // Write
                {
                    try {
                        FileOutputStream fileOutputStream2 = openFileOutput("toggle.txt", MODE_PRIVATE);
                        String writeString2 = "1";
                        fileOutputStream2.write(writeString2.getBytes());
                    } catch (FileNotFoundException e2) {
                    } catch (IOException e2) {
                    }
                }
                Toast.makeText(getApplicationContext(), (date - date_h) + "日後" + readString + ":" + readString2 + "に時間を設定しました", Toast.LENGTH_SHORT).show();
                close();
            }
        });
    }

    public  void syuto(){
//        eT3 =  (EditText) this.findViewById(R.id.editText3);
//        eT4 =  (EditText) this.findViewById(R.id.editText4);

        time = Integer.parseInt(GoHomeHourText.getText().toString());
        time2 = Integer.parseInt(GoHomeMinuteText.getText().toString());

        SharedPreferences.Editor editor2 = preferences.edit();
        editor2.putInt("HourOfDay", time);
        editor2.putInt("Minute", time2);
        currentState = -10;
        editor2.putInt(KEY_CURRENT_STATE, currentState);
        editor2.commit();
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

    public void kakikomi2(){        //初期値
        // Write
        {
            try {
                FileOutputStream fileOutputStream2 = openFileOutput("minute_S.txt", MODE_PRIVATE);
                String writeString2 = String.valueOf(minute);
                fileOutputStream2.write(writeString2.getBytes());
            } catch (FileNotFoundException e2) {
            } catch (IOException e2) {
            }
        }

        // Write
        {
            try {
                FileOutputStream fileOutputStream2 = openFileOutput("hour_S.txt", MODE_PRIVATE);
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
    }



    public void sleepnotifi(){

        yomikomi();
        int hoursl = preferences.getInt("Hourr", 0);
        int minutesl = preferences.getInt("Minutee", 0);
        int seconddd = 1;

        if (hoursl >= 0 && hoursl <= 6){
            sleephour = hoursl + 24 - 7;
            sleepminute = minutesl;
        }else {
            sleephour = hoursl - 7;
            sleepminute = minutesl;
        }

        Calendar calll = Calendar.getInstance();
        calll.set(Calendar.HOUR_OF_DAY, sleephour);
        calll.set(Calendar.MINUTE, sleepminute );
        calll.set(Calendar.SECOND, seconddd);

        Intent intenttt = new Intent(getApplicationContext(), BroadcastReceiverSleep.class);

        PendingIntent pendingg = PendingIntent.getBroadcast(getApplicationContext(), 300, intenttt, 0);

        // アラームをセットする
        AlarmManager amm = (AlarmManager)SubActivity.this.getSystemService(ALARM_SERVICE);
        amm.set(AlarmManager.RTC_WAKEUP, calll.getTimeInMillis(), pendingg);

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


    private void close(){
        finish();
    }

}

