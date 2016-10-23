package com.stonetech.mezamamamama211;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PassResultActivity extends AppCompatActivity {

    TextView PastResultText;
    Button PastBackButton;
    int passminute = 0;
    int passsecond = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_result);

        Intent intent = getIntent();
        passminute = intent.getIntExtra("PassMinute", 0);
        passsecond = intent.getIntExtra("PassSecond", 0);


        PastResultText = (TextView) findViewById(R.id.pastresulttext);
        PastResultText.setText(String.format("%1$02d:%2$02d",
                passminute, passsecond) + "過ぎています。\n"
                + "次はもっと早くしましょう!!");

        PastBackButton = (Button) findViewById(R.id.pastbackbutton);
        PastBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(),EndActivity.class);
                startActivity(intent);
                overridePendingTransition(R.animator.slide_in_right, R.animator.slide_out_left);
            }
        });

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
