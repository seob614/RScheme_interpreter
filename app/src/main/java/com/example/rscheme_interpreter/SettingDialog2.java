package com.example.rscheme_interpreter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

public class SettingDialog2  extends FragmentActivity {

    private TextView bt_quiz_list;

    public static Object  settingDialog2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // setTheme(android.R.style.Theme_NoTitleBar);
        super.onCreate(savedInstanceState);

        settingDialog2 = this;

        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_setting2);

        bt_quiz_list = (TextView) findViewById(R.id.bt_quiz_list);

        bt_quiz_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingDialog2.this, QuizListActivity.class);
                startActivity(intent);

                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
