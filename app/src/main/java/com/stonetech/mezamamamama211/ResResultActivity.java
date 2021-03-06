package com.stonetech.mezamamamama211;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResResultActivity extends AppCompatActivity {

    TextView ResResultText;
    Button ResBackButton;
    int reshour = 0;
    int resminute = 0;
    int ressecond = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res_result);

        Intent intent = getIntent();

        reshour = intent.getIntExtra("ResHour", 0);
        resminute = intent.getIntExtra("ResMinute", 0);
        ressecond = intent.getIntExtra("ResSecond", 0);

        ResResultText = (TextView) findViewById(R.id.resresulttext);
        ResResultText.setText(String.format("%1$02d:%2$02d:%3$02d",
                reshour, resminute, ressecond) + "余りました。\n"
                + "次もこの調子でいきましょう!");

        ResBackButton = (Button) findViewById(R.id.resbackbutton);
        ResBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), EndActivity.class);
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
