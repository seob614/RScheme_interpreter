package com.example.rscheme_interpreter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class SetFileDialog extends FragmentActivity {

    private EditText et_title;

    private Button confirm_btn;

    private Button cancel_btn;

    public static Object  setFileDialog;

    MainActivity mainActivity = (MainActivity) MainActivity.mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // setTheme(android.R.style.Theme_NoTitleBar);
        super.onCreate(savedInstanceState);

        setFileDialog = this;

        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_set_file);

        et_title = (EditText) findViewById(R.id.et_title);

        confirm_btn = (Button) findViewById(R.id.confirm_btn);
        cancel_btn = (Button) findViewById(R.id.cancel_btn);

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //에디트 텍스트에 파일 이름 받아오기
                String name = et_title.getText().toString();
                //에디트 텍스트에서 내용 받아오기
                String content = mainActivity.getEditText();
                try {
                    //1.(쓰기) 메소드 호출과 동시에 에디트 택스트에서 받은것들 넘겨주기
                    writeToFile(name, content);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(),"저장 실패(관리자에게 문의하세요.)",Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    //파일을 쓰기위한 메소드
    public void writeToFile(String name, String content) throws Exception {
        //2.(쓰기) 자바랑은 다르게 openFileOutput(name, MODE_PRIVATE) 이렇게 사용하는데
        // 받아온 파일이름넣어주고 쉐어드프리퍼런드때 사용자만 사용하게 하는 모드이다.
        FileOutputStream outputStream = openFileOutput(name, MODE_PRIVATE);
        //3.(쓰기) OutputStreamWriter 여기에 위에서 파일 이름 설정을 해줌
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);
        //실제 내용으로 작성하기
        writer.write(content);
        //모든것들 종료 해줌
        writer.flush();
        writer.close();
        outputStream.close();
        Toast.makeText(getBaseContext(),"저장 완료",Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}

