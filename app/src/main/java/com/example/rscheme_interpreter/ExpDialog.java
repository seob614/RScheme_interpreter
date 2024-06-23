package com.example.rscheme_interpreter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

public class ExpDialog extends FragmentActivity {
    private TextView tx_title;
    private TextView tx_exp;

    private String title;
    private String exp;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // setTheme(android.R.style.Theme_NoTitleBar);
        super.onCreate(savedInstanceState);

        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_exp);

        tx_title = (TextView) findViewById(R.id.tx_title);
        tx_exp = (TextView) findViewById(R.id.tx_exp);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        exp = intent.getStringExtra("exp");

        tx_title.setText(title);
        tx_exp.setText(exp);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}

