package com.example.rscheme_interpreter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

public class SettingDialog  extends FragmentActivity {

    private TextView bt_set;

    private TextView bt_get;

    private TextView bt_learn;

    public static Object  settingDialog;
    private boolean reset_interpreter = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // setTheme(android.R.style.Theme_NoTitleBar);
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        reset_interpreter = intent.getBooleanExtra("reset_interpreter", false);

        settingDialog = this;

        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_setting);

        bt_set = (TextView) findViewById(R.id.bt_set);
        bt_get = (TextView) findViewById(R.id.bt_get);
        bt_learn = (TextView) findViewById(R.id.bt_learn);

        bt_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingDialog.this, SetFileDialog.class);
                startActivity(intent);
                finish();
            }
        });

        bt_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingDialog.this, FileListActivity.class);
                startActivity(intent);

                finish();
            }
        });

        bt_learn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingDialog.this, LearnActivity.class);
                intent.putExtra("reset_interpreter",reset_interpreter);
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
