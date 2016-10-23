package com.stonetech.mezamamamama211;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by syuto on 2016/10/09.
 */

public class BackAlarm extends AppCompatActivity {

    private static final int bid2 = 2;

    public int year, month, date, hour, minute, second, msecond;
    String readString,readString2,readString3,readString4,readSt,readSt2;
    public int year_h, month_h, date_h, hour_h, minute_h;       //比較用


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alarm();

    }


    public void alarm() {

        yomikomi();

        Calendar cal = Calendar.getInstance();
        // 協定世界時 (UTC)です適宜設定してください
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH); // 4=>5月
        date = cal.get(Calendar.DAY_OF_MONTH);
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
        AlarmManager am = (AlarmManager)BackAlarm.this.getSystemService(ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), pending);
        // Toast.makeText(getApplicationContext(), "ALARM 2", Toast.LENGTH_SHORT).show();

        //イか月ずれてる

        kakikomi();
        yomikomi();

        toggle();

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

        Toast.makeText(getApplicationContext(), (date-date_h)+"日後"+readSt+":"+readSt2+"に時間を設定しました", Toast.LENGTH_SHORT).show();

        toggle();

        finish();
    }
    NotificationManager nm = null;

    public void toggle(){
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);

        Notification notification = new Notification.Builder(this)
                .setContentTitle("アラーム設定")
                .setContentText(date-date_h+"日後"+readSt+":"+readSt2+"にタイマーが作動します")
                .setContentIntent(pi)
                .setSmallIcon(R.drawable.time_icon)
                .setAutoCancel(true)
                .build();

        notification.flags = Notification.FLAG_ONGOING_EVENT;

        nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(1, notification); // 設定したNotificationを通知する
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
