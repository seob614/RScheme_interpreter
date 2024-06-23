package com.example.rscheme_interpreter;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

public class PyActivity extends AppCompatActivity {

    private TextView tx_scheme;
    private TextView tx_py;
    
    private String scheme_code = "";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_py);

        tx_scheme = (TextView) findViewById(R.id.tx_scheme);
        tx_py = (TextView) findViewById(R.id.tx_py);

        Intent intent = getIntent();
        scheme_code = intent.getStringExtra("code");
        
        if (!scheme_code.equals("")){
            tx_scheme.setText(scheme_code);
            if (! Python.isStarted()) {
                Python.start(new AndroidPlatform(this));
            }
            Python py = Python.getInstance();
            PyObject pyObject = py.getModule("scheme");
            PyObject obj = pyObject.callAttr("run",scheme_code);
            //PyObject pyObject = py.getModule("scheme");
            //PyObject obj = pyObject.callAttr("run","(+ 1 1)");
            if (obj==null){
                Toast.makeText(getBaseContext(),"파이썬 변환기 오류 발생2",Toast.LENGTH_SHORT).show();
            }else{
                tx_py.setText(obj.toString());
            }
        }else{
            Toast.makeText(getBaseContext(),"코드를 입력하세요.",Toast.LENGTH_SHORT).show();
            finish();
        }

        
    }
}