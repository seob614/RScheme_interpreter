package com.example.rscheme_interpreter;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rscheme_interpreter.util.ZipUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipException;

public class MainActivity extends AppCompatActivity {

    private ImageView bt_help;
    private ImageView bt_setting;
    private EditText et_definitions;
    private Button bt_definitions;
    private ImageView bt_parenthesis;

    private ImageView bt_py;
    private EditText et_interactions;

    private Button bt_interactions;
    private ImageView bt_parenthesis2;

    private ScrollView scrollView;
    private TextView tx_interaction;

    private int closeNum = 0;
    private String result="";

    //결과창 결과값 색
    private CharSequence concatString ="";

    private int closeNum2 = 0;
    private String result2="";
    private TextWatcher textWatcher2;

    private Map<String,Integer> map = new HashMap<>();

    private boolean reset_interpreter = false;

    public static Object  mainActivity;
    private String activity_name = "mainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivity = MainActivity.this;

        et_definitions = (EditText) findViewById(R.id.et_definitions);
        bt_definitions = (Button) findViewById(R.id.bt_definitions);
        bt_parenthesis = (ImageView) findViewById(R.id.bt_parenthesis);
        bt_py = (ImageView) findViewById(R.id.bt_py);

        et_interactions = (EditText) findViewById(R.id.et_interactions);
        bt_interactions = (Button) findViewById(R.id.bt_interactions);
        bt_parenthesis2 = (ImageView) findViewById(R.id.bt_parenthesis2);

        scrollView = (ScrollView) findViewById(R.id.scrollView);
        tx_interaction = (TextView) findViewById(R.id.tx_interaction);

        bt_setting = (ImageView) findViewById(R.id.bt_setting);

        bt_help = (ImageView) findViewById(R.id.bt_help);

        extractZipFile(this, "sys.img",this.getFilesDir().getPath());

        try {
            writeToFile("Hello World","(display \"Hello World\")");
        } catch (Exception e) {
            e.printStackTrace();
        }

        set_schemeSyntax();

        bt_definitions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                concatString ="";

                if (et_definitions.getText().toString().equals("")){
                    return;
                }

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        Rs rs = new Rs();

                        if (reset_interpreter){
                            rs.exit();
                            rs.init(getFilesDir().getPath(), "");
                        }else{
                            rs.init(getFilesDir().getPath(), "");
                            reset_interpreter = true;
                        }

                        //괄호 자동완성 제거
                        String inputText = et_definitions.getText().toString();
                        int textLength = inputText.length();
                        tx_interaction.setText("");

                        if (closeNum!=0) {
                            String getString = inputText.substring(0,textLength-closeNum);

                            //mEditText2.setText("");
                            String ret = rs.eval(getString);
                            //ret = ret.replaceAll("\n", "");
                            if (ret.contains("********** error detected **********")){
                                ret = ret.replaceAll("\n", "");
                                String exp = ret;
                                int start = exp.indexOf("********** error detected **********");
                                int end = exp.indexOf("************************************************");
                                String result = exp.substring(start, start+36)+"\n"+exp.substring(start+36, end);
                                SpannableString spannableString = changeColor3(2,result);
                                concatString = TextUtils.concat(spannableString);
                                tx_interaction.setText(concatString);

                            }else{
                                String exp = ret;
                                concatString = TextUtils.concat(exp);
                                tx_interaction.setText(exp);

                            }
                            //Log.d(TAG, ret);
                        }else{
                            String getString = inputText;

                            String ret = rs.eval(getString);
                            //ret = ret.replaceAll("\n", "");
                            if (ret.contains("********** error detected **********")){
                                ret = ret.replaceAll("\n", "");
                                String exp = ret;
                                int start = exp.indexOf("********** error detected **********");
                                int end = exp.indexOf("************************************************");
                                String result = exp.substring(start, start+36)+"\n"+exp.substring(start+36, end);
                                SpannableString spannableString = changeColor3(2,result);
                                concatString = TextUtils.concat(spannableString);
                                tx_interaction.setText(concatString);

                            }else{
                                String exp = ret;
                                concatString = TextUtils.concat(exp);
                                tx_interaction.setText(exp);

                            }
                            //Log.d(TAG, ret);
                        }
                        scrolldown(scrollView);

                    }
                });
            }
        });

        bt_interactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_interactions.getText().toString().equals("")){
                    return;
                }

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        Rs rs = new Rs();
                        rs.init(getFilesDir().getPath(), "");

                        //괄호 자동완성 제거
                        String inputText = et_interactions.getText().toString();
                        int textLength = inputText.length();

                        if (closeNum2!=0) {
                            String getString = inputText.substring(0,textLength-closeNum2);

                            et_interactions.removeTextChangedListener(textWatcher2);
                            et_interactions.setText("");
                            et_interactions.setSelection(0); // 커서 위치 조정
                            closeNum2 = 0;
                            et_interactions.addTextChangedListener(textWatcher2);
                            //mEditText2.setText("");
                            String ret = rs.eval(getString);
                            //ret = ret.replaceAll("\n", "");
                            if (ret.contains("********** error detected **********")){
                                ret = ret.replaceAll("\n", "");
                                int start = ret.indexOf("********** error detected **********");
                                int end = ret.indexOf("************************************************");
                                String result = ret.substring(start, start+36)+"\n"+ret.substring(start+36, end);

                                if (tx_interaction.getText().toString().equals("")){
                                    SpannableString spannableString = changeColor3(2,result);
                                    concatString = TextUtils.concat(getString,"\n",spannableString);

                                    tx_interaction.setText(concatString);
                                }else{
                                    SpannableString spannableString = changeColor3(2,result);
                                    concatString = TextUtils.concat(concatString, "\n",getString,"\n",spannableString);

                                    tx_interaction.setText(concatString);
                                }

                            }else{
                                if (tx_interaction.getText().toString().equals("")){
                                    SpannableString spannableString = changeColor3(1,ret);
                                    concatString = TextUtils.concat(getString,"\n",spannableString);
                                    tx_interaction.setText(concatString);
                                }else{
                                    SpannableString spannableString = changeColor3(1,ret);
                                    concatString = TextUtils.concat(concatString,"\n",getString,"\n",spannableString);
                                    tx_interaction.setText(concatString);
                                }

                            }
                            //Log.d(TAG, ret);
                        }else{
                            String getString = inputText;

                            et_interactions.setText("");
                            String ret = rs.eval(getString);
                            //ret = ret.replaceAll("\n", "");
                            if (ret.contains("********** error detected **********")){
                                ret = ret.replaceAll("\n", "");
                                int start = ret.indexOf("********** error detected **********");
                                int end = ret.indexOf("************************************************");
                                String result = ret.substring(start, start+36)+"\n"+ret.substring(start+36, end);

                                if (tx_interaction.getText().toString().equals("")){
                                    SpannableString spannableString = changeColor3(2,result);
                                    concatString = TextUtils.concat(getString,"\n",spannableString);
                                    tx_interaction.setText(concatString);
                                }else{
                                    SpannableString spannableString = changeColor3(2,result);
                                    concatString = TextUtils.concat(concatString, "\n",getString,"\n",spannableString);
                                    tx_interaction.setText(concatString);
                                }

                            }else{
                                if (tx_interaction.getText().toString().equals("")){
                                    SpannableString spannableString = changeColor3(1,ret);
                                    concatString = TextUtils.concat(getString,"\n",spannableString);
                                    tx_interaction.setText(concatString);
                                }else{
                                    SpannableString spannableString = changeColor3(1,ret);
                                    concatString = TextUtils.concat(concatString,"\n",getString,"\n",spannableString);
                                    tx_interaction.setText(concatString);
                                }

                            }
                            //Log.d(TAG, ret);
                        }
                        et_interactions.clearFocus();
                        scrolldown(scrollView);
                    }
                });
            }
        });
        edittext_codingevent(et_definitions);
        edittext_codingevent2(et_interactions);

        bt_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HelpDialog.class);
                intent.putExtra("activity_name",activity_name);
                startActivity(intent);
            }
        });

        bt_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingDialog.class);
                intent.putExtra("reset_interpreter",reset_interpreter);
                startActivity(intent);
            }
        });

        bt_parenthesis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int syntax_cursorPosition = et_definitions.getSelectionStart();
                String get_result = et_definitions.getText().toString();
                String result1 = get_result.substring(0,syntax_cursorPosition);
                String result2 = get_result.substring(syntax_cursorPosition);
                et_definitions.setText(result1+"()"+result2);
                et_definitions.setSelection(syntax_cursorPosition+1); // 커서 위치 조정
                et_definitions.requestFocus();
                et_definitions.setCursorVisible(true);
            }
        });

        bt_py.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,PyActivity.class);
                intent.putExtra("code",et_definitions.getText().toString());
                startActivity(intent);
            }
        });

        bt_parenthesis2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int syntax_cursorPosition = et_interactions.getSelectionStart();
                String get_result = et_interactions.getText().toString();
                String result1 = get_result.substring(0,syntax_cursorPosition);
                String result2 = get_result.substring(syntax_cursorPosition);
                et_interactions.setText(result1+"()"+result2);
                et_interactions.setSelection(syntax_cursorPosition+1); // 커서 위치 조정
                et_interactions.requestFocus();
                et_interactions.setCursorVisible(true);
            }
        });
    }

    public void scrolldown(ScrollView scrollView){
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
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
    }

    public String getEditText(){
        return et_definitions.getText().toString();
    }

    public void setEditText(String s){
        et_definitions.setText(s);
    }

    private void edittext_codingevent(EditText editText){
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable editable) {
                //syntax highlighting
                SpannableString syntaxString = applySyntaxHighlighting(editable.toString());

                // EditText에 Span 적용
                editText.removeTextChangedListener(this);
                int syntax_cursorPosition = editText.getSelectionStart();
                editText.setText(syntaxString);
                editText.setSelection(syntax_cursorPosition); // 커서 위치 조정
                editText.addTextChangedListener(this);

                String inputText = editable.toString();

                //Toast.makeText(getBaseContext(),String.valueOf(closeNum),Toast.LENGTH_SHORT).show();

                int textLength = editable.length();

                if (closeNum!=0) {
                    int cursorPosition = editText.getSelectionStart();

                    String plus_close = "";
                    for (int i = 0; i < closeNum; i++){
                        plus_close = plus_close+")";
                    }

                    if (cursorPosition > textLength-closeNum) {
                        if (result.length()<textLength){
                            String get_inputText = editable.toString();
                            String newText = get_inputText.substring(cursorPosition-1,cursorPosition);
                            get_inputText = get_inputText.substring(0,get_inputText.length()-(closeNum+1))+newText+ plus_close;
                            SpannableString spannableString = changeColor(get_inputText.length(),get_inputText);
                            // EditText에 Span 적용

                            editText.removeTextChangedListener(this);
                            editText.setText(spannableString);
                            editText.setSelection(get_inputText.length()-closeNum); // 커서 위치 조정
                            editText.addTextChangedListener(this);
                            inputText = get_inputText;
                        }else if(result.length()>=textLength){
                            //이부분 (xx)에서 지우면 오류, 첫시작()에서 지우면 안지워짐, 첫부분))(안됨
                            if (textLength==1){
                                result = "";
                                //result = editable.toString();
                                editText.removeTextChangedListener(this);
                                closeNum=0;
                                editText.setText("");
                                editText.setSelection(0); // 커서 위치 조정
                                editText.addTextChangedListener(this);
                                return;
                            }else{
                                String get_inputText = editable.toString();
                                get_inputText = get_inputText.substring(0,get_inputText.length()-closeNum)+ plus_close;
                                SpannableString spannableString = changeColor(get_inputText.length(),get_inputText);
                                // EditText에 Span 적용
                                editText.removeTextChangedListener(this);

                                editText.setText(spannableString);
                                editText.setSelection(get_inputText.length()-closeNum); // 커서 위치 조정
                                editText.addTextChangedListener(this);
                                inputText = get_inputText;
                            }

                        }

                    }else{
                        SpannableString spannableString = changeColor(textLength,editable);

                        // EditText에 Span 적용
                        editText.removeTextChangedListener(this);

                        editText.setText(spannableString);
                        editText.setSelection(cursorPosition); // 커서 위치 조정
                        editText.addTextChangedListener(this);
                    }

                }
                int input_openNum = getCharNumber(inputText,'(');
                int input_closeNum = getCharNumber(inputText,')');
                textLength = inputText.length();

                if(!TextUtils.isEmpty(editable.toString()) && !editable.toString().equals(result)){
                    String get_result = result;
                    String sub_inputText = inputText;

                    //Toast.makeText(getBaseContext(),input_getText,Toast.LENGTH_SHORT).show();
                    if (input_closeNum==0){
                        if (input_openNum!=0){
                            closeNum++;
                            result = inputText+")";
                            int result_textLength = result.length();
                            SpannableString spannableString = changeColor(result_textLength,result);

                            editText.removeTextChangedListener(this);
                            int cursorPosition = editText.getSelectionStart();
                            editText.setText(spannableString);
                            editText.setSelection(cursorPosition); // 커서 위치 조정
                            editText.addTextChangedListener(this);
                        }else{
                            result = inputText;
                        }
                    }else if(input_openNum==0){
                        if (closeNum!=0){
                            closeNum =0;
                            if (inputText.length()==1){
                                result = "";
                                editText.removeTextChangedListener(this);
                                int cursorPosition = editText.getSelectionStart();
                                editText.setText("");
                                editText.setSelection(cursorPosition); // 커서 위치 조정
                                editText.addTextChangedListener(this);
                            }else{
                                //result = inputText.substring(0,inputText.indexOf(')'));

                                //closeNum!=0 면서 input_openNum==0이려면 open이 맨 끝에 위치해야함
                                result = inputText.substring(0,inputText.length()-1);

                                int result_textLength = result.length()-1;

                                SpannableString spannableString = changeColor(result_textLength,result);

                                editText.removeTextChangedListener(this);
                                int cursorPosition = editText.getSelectionStart();
                                editText.setText(spannableString);
                                editText.setSelection(cursorPosition); // 커서 위치 조정
                                editText.addTextChangedListener(this);
                            }

                        }else{
                            result = inputText;
                        }
                    } else{

                        String range_inputText = sub_inputText.substring(inputText.indexOf('('));

                        int range_openNum = getCharNumber(range_inputText,'(');

                        int range_closeNum = getCharNumber(range_inputText,')');

                        String result_range_inputText = "";

                        int result_range_openNum = 0;

                        int result_range_closeNum = 0;

                        if (getCharNumber(get_result,'(')!=0){
                            result_range_inputText = get_result.substring(get_result.indexOf('('));
                            result_range_openNum = getCharNumber(result_range_inputText,'(');
                            result_range_closeNum = getCharNumber(result_range_inputText,')');
                        }

                        if (range_openNum==result_range_openNum&&range_closeNum==result_range_closeNum){
                            result = inputText;
                        }else{
                            String getinputText = inputText.substring(0,textLength-closeNum);
                            range_inputText = range_inputText.substring(range_inputText.indexOf('('),range_inputText.length()-closeNum);

                            ArrayList<Integer> open_arrayList = new ArrayList<Integer>();
                            ArrayList<Integer> close_arrayList = new ArrayList<Integer>();
                            for (int i = 0; i < range_inputText.length(); i++) {
                                if (range_inputText.charAt(i) == '(') {
                                    open_arrayList.add(i);
                                }
                            }
                            for (int i = 0; i < range_inputText.length(); i++) {
                                if (range_inputText.charAt(i) == ')') {
                                    close_arrayList.add(i);
                                }
                            }
                            int check_num =0;
                            int check_over = 0;
                            boolean include_open = false;
                            boolean b_open_over = false;
                            for (int a = 0; a < open_arrayList.size(); a++){
                                if (close_arrayList.size()==0){
                                    check_num = open_arrayList.size();
                                    break;
                                }
                                if (check_num+1>close_arrayList.size()){
                                    if (b_open_over){
                                        check_num = (open_arrayList.size()-a)+1;
                                        check_over = a;
                                        break;
                                    }
                                    if (include_open){
                                        check_num = (open_arrayList.size()-a)+1;
                                        check_over = a;
                                        break;
                                    }else{
                                        check_num = open_arrayList.size()-a;
                                        check_over = a;
                                        break;
                                    }

                                }
                                for (int b = check_num; b < close_arrayList.size(); b++){

                                    if (open_arrayList.get(a)<close_arrayList.get(b)){

                                        if (open_arrayList.size()==a+1){
                                            check_num = 0;
                                        }else{
                                            check_num = b+1;
                                        }
                                        break;
                                    }else{
                                        check_num = b+1;
                                        if (close_arrayList.size()==b+1){
                                            include_open = true;
                                            if (open_arrayList.size()>a+1){
                                                b_open_over = true;
                                            }else if(open_arrayList.size()==a+1){
                                                b_open_over = true;
                                                check_num = (open_arrayList.size()-a);
                                                check_over = a;
                                                break;
                                            }
                                        }
                                    }

                                }
                            }
                            //Toast.makeText(getBaseContext(),String.valueOf(check_num),Toast.LENGTH_SHORT).show();
                            if (check_num>close_arrayList.size()){
                                if ((open_arrayList.size()-check_over)<=check_num){
                                    closeNum = check_num;
                                }else{
                                    closeNum = check_num-close_arrayList.size();
                                }

                                String plus_close = "";
                                for (int i = 0; i < closeNum; i++){
                                    plus_close = plus_close+")";
                                }
                                result = getinputText+plus_close;
                                int result_textLength = result.length();
                                SpannableString spannableString = changeColor(result_textLength,result);

                                editText.removeTextChangedListener(this);
                                int cursorPosition = editText.getSelectionStart();
                                editText.setText(spannableString);
                                editText.setSelection(cursorPosition); // 커서 위치 조정
                                editText.addTextChangedListener(this);
                            }else if(check_num==close_arrayList.size()){
                                if ((open_arrayList.size()-check_over)<=check_num){
                                    closeNum = check_num;
                                    String plus_close = "";
                                    for (int i = 0; i < closeNum; i++){
                                        plus_close = plus_close+")";
                                    }
                                    result = getinputText+plus_close;
                                    int result_textLength = result.length();
                                    SpannableString spannableString = changeColor(result_textLength,result);

                                    editText.removeTextChangedListener(this);
                                    int cursorPosition = editText.getSelectionStart();
                                    editText.setText(spannableString);
                                    editText.setSelection(cursorPosition); // 커서 위치 조정
                                    editText.addTextChangedListener(this);
                                }else{
                                    closeNum = 0;
                                    result = getinputText;
                                    int result_textLength = result.length();
                                    SpannableString spannableString = changeColor(result_textLength,result);

                                    editText.removeTextChangedListener(this);
                                    int cursorPosition = editText.getSelectionStart();
                                    editText.setText(spannableString);
                                    editText.setSelection(cursorPosition); // 커서 위치 조정
                                    editText.addTextChangedListener(this);
                                }
                            }else{
                                if (b_open_over){
                                    closeNum = check_num;
                                    String plus_close = "";
                                    for (int i = 0; i < closeNum; i++){
                                        plus_close = plus_close+")";
                                    }
                                    result = getinputText+plus_close;
                                    int result_textLength = result.length();
                                    SpannableString spannableString = changeColor(result_textLength,result);

                                    editText.removeTextChangedListener(this);
                                    int cursorPosition = editText.getSelectionStart();
                                    editText.setText(spannableString);
                                    editText.setSelection(cursorPosition); // 커서 위치 조정
                                    editText.addTextChangedListener(this);
                                }else{
                                    if ((open_arrayList.size()-check_over)<=check_num){
                                        closeNum = check_num;
                                        String plus_close = "";
                                        for (int i = 0; i < closeNum; i++){
                                            plus_close = plus_close+")";
                                        }
                                        result = getinputText+plus_close;
                                        int result_textLength = result.length();
                                        SpannableString spannableString = changeColor(result_textLength,result);

                                        editText.removeTextChangedListener(this);
                                        int cursorPosition = editText.getSelectionStart();
                                        editText.setText(spannableString);
                                        editText.setSelection(cursorPosition); // 커서 위치 조정
                                        editText.addTextChangedListener(this);
                                    }else{
                                        closeNum = 0;
                                        result = getinputText;
                                        int result_textLength = result.length();
                                        SpannableString spannableString = changeColor(result_textLength,result);

                                        editText.removeTextChangedListener(this);
                                        int cursorPosition = editText.getSelectionStart();
                                        editText.setText(spannableString);
                                        editText.setSelection(cursorPosition); // 커서 위치 조정
                                        editText.addTextChangedListener(this);
                                    }
                                }

                            }

                        }
                    }
                }


            }
        });
    }

    private void edittext_codingevent2(EditText editText){
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable editable) {
                //syntax highlighting
                SpannableString syntaxString = applySyntaxHighlighting(editable.toString());

                // EditText에 Span 적용
                editText.removeTextChangedListener(this);
                int syntax_cursorPosition = editText.getSelectionStart();
                editText.setText(syntaxString);
                editText.setSelection(syntax_cursorPosition); // 커서 위치 조정
                editText.addTextChangedListener(this);

                textWatcher2 = this;

                String inputText = editable.toString();

                //Toast.makeText(getBaseContext(),String.valueOf(closeNum),Toast.LENGTH_SHORT).show();

                int textLength = editable.length();

                if (closeNum2!=0) {
                    int cursorPosition = editText.getSelectionStart();

                    String plus_close = "";
                    for (int i = 0; i < closeNum2; i++){
                        plus_close = plus_close+")";
                    }

                    if (cursorPosition > textLength-closeNum2) {
                        if (result2.length()<textLength){
                            String get_inputText = editable.toString();
                            String newText = get_inputText.substring(cursorPosition-1,cursorPosition);
                            get_inputText = get_inputText.substring(0,get_inputText.length()-(closeNum2+1))+newText+ plus_close;
                            SpannableString spannableString = changeColor2(get_inputText.length(),get_inputText);
                            // EditText에 Span 적용

                            editText.removeTextChangedListener(this);
                            editText.setText(spannableString);
                            editText.setSelection(get_inputText.length()-closeNum2); // 커서 위치 조정
                            editText.addTextChangedListener(this);
                            inputText = get_inputText;
                        }else if(result2.length()>=textLength){
                            //이부분 (xx)에서 지우면 오류, 첫시작()에서 지우면 안지워짐, 첫부분))(안됨
                            if (textLength==1){
                                result2 = "";
                                //result = editable.toString();
                                editText.removeTextChangedListener(this);
                                closeNum2=0;
                                editText.setText("");
                                editText.setSelection(0); // 커서 위치 조정
                                editText.addTextChangedListener(this);
                                return;
                            }else{
                                String get_inputText = editable.toString();
                                get_inputText = get_inputText.substring(0,get_inputText.length()-closeNum2)+ plus_close;
                                SpannableString spannableString = changeColor2(get_inputText.length(),get_inputText);
                                // EditText에 Span 적용
                                editText.removeTextChangedListener(this);

                                editText.setText(spannableString);
                                editText.setSelection(get_inputText.length()-closeNum2); // 커서 위치 조정
                                editText.addTextChangedListener(this);
                                inputText = get_inputText;
                            }

                        }

                    }else{
                        SpannableString spannableString = changeColor2(textLength,editable);

                        // EditText에 Span 적용
                        editText.removeTextChangedListener(this);

                        editText.setText(spannableString);
                        editText.setSelection(cursorPosition); // 커서 위치 조정
                        editText.addTextChangedListener(this);
                    }

                }
                int input_openNum = getCharNumber(inputText,'(');
                int input_closeNum = getCharNumber(inputText,')');
                textLength = inputText.length();

                if(!TextUtils.isEmpty(editable.toString()) && !editable.toString().equals(result2)){
                    String get_result = result2;
                    String sub_inputText = inputText;

                    //Toast.makeText(getBaseContext(),input_getText,Toast.LENGTH_SHORT).show();
                    if (input_closeNum==0){
                        if (input_openNum!=0){
                            closeNum2++;
                            result2 = inputText+")";
                            int result_textLength = result2.length();
                            SpannableString spannableString = changeColor2(result_textLength,result2);

                            editText.removeTextChangedListener(this);
                            int cursorPosition = editText.getSelectionStart();
                            editText.setText(spannableString);
                            editText.setSelection(cursorPosition); // 커서 위치 조정
                            editText.addTextChangedListener(this);
                        }else{
                            result2 = inputText;
                        }
                    }else if(input_openNum==0){
                        if (closeNum2!=0){
                            closeNum2 =0;
                            if (inputText.length()==1){
                                result2 = "";
                                editText.removeTextChangedListener(this);
                                int cursorPosition = editText.getSelectionStart();
                                editText.setText("");
                                editText.setSelection(cursorPosition); // 커서 위치 조정
                                editText.addTextChangedListener(this);
                            }else{
                                //result = inputText.substring(0,inputText.indexOf(')'));

                                //closeNum!=0 면서 input_openNum==0이려면 open이 맨 끝에 위치해야함
                                result2 = inputText.substring(0,inputText.length()-1);

                                int result_textLength = result2.length()-1;

                                SpannableString spannableString = changeColor2(result_textLength,result2);

                                editText.removeTextChangedListener(this);
                                int cursorPosition = editText.getSelectionStart();
                                editText.setText(spannableString);
                                editText.setSelection(cursorPosition); // 커서 위치 조정
                                editText.addTextChangedListener(this);
                            }

                        }else{
                            result2 = inputText;
                        }
                    } else{

                        String range_inputText = sub_inputText.substring(inputText.indexOf('('));

                        int range_openNum = getCharNumber(range_inputText,'(');

                        int range_closeNum = getCharNumber(range_inputText,')');

                        String result_range_inputText = "";

                        int result_range_openNum = 0;

                        int result_range_closeNum = 0;

                        if (getCharNumber(get_result,'(')!=0){
                            result_range_inputText = get_result.substring(get_result.indexOf('('));
                            result_range_openNum = getCharNumber(result_range_inputText,'(');
                            result_range_closeNum = getCharNumber(result_range_inputText,')');
                        }

                        if (range_openNum==result_range_openNum&&range_closeNum==result_range_closeNum){
                            result2 = inputText;
                        }else{
                            String getinputText = inputText.substring(0,textLength-closeNum2);
                            range_inputText = range_inputText.substring(range_inputText.indexOf('('),range_inputText.length()-closeNum2);

                            ArrayList<Integer> open_arrayList = new ArrayList<Integer>();
                            ArrayList<Integer> close_arrayList = new ArrayList<Integer>();
                            for (int i = 0; i < range_inputText.length(); i++) {
                                if (range_inputText.charAt(i) == '(') {
                                    open_arrayList.add(i);
                                }
                            }
                            for (int i = 0; i < range_inputText.length(); i++) {
                                if (range_inputText.charAt(i) == ')') {
                                    close_arrayList.add(i);
                                }
                            }
                            int check_num =0;
                            int check_over = 0;
                            boolean include_open = false;
                            boolean b_open_over = false;
                            for (int a = 0; a < open_arrayList.size(); a++){
                                if (close_arrayList.size()==0){
                                    check_num = open_arrayList.size();
                                    break;
                                }
                                if (check_num+1>close_arrayList.size()){
                                    if (b_open_over){
                                        check_num = (open_arrayList.size()-a)+1;
                                        check_over = a;
                                        break;
                                    }
                                    if (include_open){
                                        check_num = (open_arrayList.size()-a)+1;
                                        check_over = a;
                                        break;
                                    }else{
                                        check_num = open_arrayList.size()-a;
                                        check_over = a;
                                        break;
                                    }

                                }
                                for (int b = check_num; b < close_arrayList.size(); b++){

                                    if (open_arrayList.get(a)<close_arrayList.get(b)){

                                        if (open_arrayList.size()==a+1){
                                            check_num = 0;
                                        }else{
                                            check_num = b+1;
                                        }
                                        break;
                                    }else{
                                        check_num = b+1;
                                        if (close_arrayList.size()==b+1){
                                            include_open = true;
                                            if (open_arrayList.size()>a+1){
                                                b_open_over = true;
                                            }else if(open_arrayList.size()==a+1){
                                                b_open_over = true;
                                                check_num = (open_arrayList.size()-a);
                                                check_over = a;
                                                break;
                                            }
                                        }
                                    }

                                }
                            }
                            //Toast.makeText(getBaseContext(),String.valueOf(check_num),Toast.LENGTH_SHORT).show();
                            if (check_num>close_arrayList.size()){
                                if ((open_arrayList.size()-check_over)<=check_num){
                                    closeNum2 = check_num;
                                }else{
                                    closeNum2 = check_num-close_arrayList.size();
                                }

                                String plus_close = "";
                                for (int i = 0; i < closeNum2; i++){
                                    plus_close = plus_close+")";
                                }
                                result2 = getinputText+plus_close;
                                int result_textLength = result2.length();
                                SpannableString spannableString = changeColor2(result_textLength,result2);

                                editText.removeTextChangedListener(this);
                                int cursorPosition = editText.getSelectionStart();
                                editText.setText(spannableString);
                                editText.setSelection(cursorPosition); // 커서 위치 조정
                                editText.addTextChangedListener(this);
                            }else if(check_num==close_arrayList.size()){
                                if ((open_arrayList.size()-check_over)<=check_num){
                                    closeNum2 = check_num;
                                    String plus_close = "";
                                    for (int i = 0; i < closeNum2; i++){
                                        plus_close = plus_close+")";
                                    }
                                    result2 = getinputText+plus_close;
                                    int result_textLength = result2.length();
                                    SpannableString spannableString = changeColor2(result_textLength,result2);

                                    editText.removeTextChangedListener(this);
                                    int cursorPosition = editText.getSelectionStart();
                                    editText.setText(spannableString);
                                    editText.setSelection(cursorPosition); // 커서 위치 조정
                                    editText.addTextChangedListener(this);
                                }else{
                                    closeNum2 = 0;
                                    result2 = getinputText;
                                    int result_textLength = result2.length();
                                    SpannableString spannableString = changeColor2(result_textLength,result2);

                                    editText.removeTextChangedListener(this);
                                    int cursorPosition = editText.getSelectionStart();
                                    editText.setText(spannableString);
                                    editText.setSelection(cursorPosition); // 커서 위치 조정
                                    editText.addTextChangedListener(this);
                                }
                            }else{
                                if (b_open_over){
                                    closeNum2 = check_num;
                                    String plus_close = "";
                                    for (int i = 0; i < closeNum2; i++){
                                        plus_close = plus_close+")";
                                    }
                                    result2 = getinputText+plus_close;
                                    int result_textLength = result2.length();
                                    SpannableString spannableString = changeColor2(result_textLength,result2);

                                    editText.removeTextChangedListener(this);
                                    int cursorPosition = editText.getSelectionStart();
                                    editText.setText(spannableString);
                                    editText.setSelection(cursorPosition); // 커서 위치 조정
                                    editText.addTextChangedListener(this);
                                }else{
                                    if ((open_arrayList.size()-check_over)<=check_num){
                                        closeNum2 = check_num;
                                        String plus_close = "";
                                        for (int i = 0; i < closeNum2; i++){
                                            plus_close = plus_close+")";
                                        }
                                        result2 = getinputText+plus_close;
                                        int result_textLength = result2.length();
                                        SpannableString spannableString = changeColor2(result_textLength,result2);

                                        editText.removeTextChangedListener(this);
                                        int cursorPosition = editText.getSelectionStart();
                                        editText.setText(spannableString);
                                        editText.setSelection(cursorPosition); // 커서 위치 조정
                                        editText.addTextChangedListener(this);
                                    }else{
                                        closeNum2 = 0;
                                        result2 = getinputText;
                                        int result_textLength = result2.length();
                                        SpannableString spannableString = changeColor2(result_textLength,result2);

                                        editText.removeTextChangedListener(this);
                                        int cursorPosition = editText.getSelectionStart();
                                        editText.setText(spannableString);
                                        editText.setSelection(cursorPosition); // 커서 위치 조정
                                        editText.addTextChangedListener(this);
                                    }
                                }

                            }

                        }
                    }
                }


            }
        });
    }

    public void setFunc(String set){
        int syntax_cursorPosition = et_definitions.getSelectionStart();
        String get_result = et_definitions.getText().toString();
        String result1 = get_result.substring(0,syntax_cursorPosition);
        String result2 = get_result.substring(syntax_cursorPosition);
        et_definitions.setText(result1+set+result2);
        et_definitions.setSelection(syntax_cursorPosition+set.length()-1); // 커서 위치 조정
        et_definitions.requestFocus();
        et_definitions.setCursorVisible(true);
    }

    private SpannableString changeColor(int result_textLength, CharSequence result) {
        // 다른 색으로 변환할 부분의 시작 위치 계산
        SpannableString spannableString = new SpannableString(result);

        int start = result_textLength - closeNum;

        // ForegroundColorSpan을 사용하여 글자 색상 변경
        ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(Color.BLACK);
        spannableString.setSpan(colorSpan2, 0, result_textLength, 0);

        spannableString = applySyntaxHighlighting(spannableString);

        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.LTGRAY);
        spannableString.setSpan(colorSpan, start, result_textLength, 0);
        return spannableString;
    }

    private SpannableString changeColor2(int result_textLength, CharSequence result) {
        // 다른 색으로 변환할 부분의 시작 위치 계산
        SpannableString spannableString = new SpannableString(result);

        int start = result_textLength - closeNum2;

        // ForegroundColorSpan을 사용하여 글자 색상 변경
        ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(Color.BLACK);
        spannableString.setSpan(colorSpan2, 0, result_textLength, 0);

        spannableString = applySyntaxHighlighting(spannableString);

        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.LTGRAY);
        spannableString.setSpan(colorSpan, start, result_textLength, 0);
        return spannableString;
    }

    //출력창 내부 입력: 검은색(0), 출력: 파란색(1), 에러: 빨강색(2)
    private SpannableString changeColor3(int text_color, CharSequence result) {
        // 다른 색으로 변환할 부분의 시작 위치 계산
        SpannableString spannableString = new SpannableString(result);

        if (text_color==0){
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.black));
            spannableString.setSpan(colorSpan, 0, result.length(), 0);
        }else if(text_color==1){
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.blue2));
            spannableString.setSpan(colorSpan, 0, result.length(), 0);
        }else if(text_color==2){
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.red));
            spannableString.setSpan(colorSpan, 0, result.length(), 0);
        }

        return spannableString;
    }

    private void set_schemeSyntax(){
        map.put("(", Color.RED);map.put(")", Color.RED);

        map.put("define", Color.BLUE);map.put("lambda", Color.BLUE);map.put("if", Color.BLUE);map.put("else", Color.BLUE);map.put("let", Color.BLUE);

        map.put("string", Color.GREEN);map.put("number", Color.GREEN);map.put("boolean", Color.GREEN);

        map.put("and", ContextCompat.getColor(this, R.color.darkgreen));map.put("or", ContextCompat.getColor(this, R.color.darkgreen));map.put("not", ContextCompat.getColor(this, R.color.darkgreen));

        map.put("=", Color.MAGENTA);map.put("<", Color.MAGENTA);map.put(">", Color.MAGENTA);map.put("<=", Color.MAGENTA);map.put(">=", Color.MAGENTA);

        map.put("car", Color.CYAN);map.put("cdr", Color.CYAN);map.put("cons", Color.CYAN);

        map.put("display", ContextCompat.getColor(this, R.color.blue2));map.put("newline", ContextCompat.getColor(this, R.color.blue2));
    }

    private SpannableString applySyntaxHighlighting(CharSequence result) {
        String string = result.toString();
        String[] split = string.split("\\s|\\(|\\)");

        SpannableString spannableString = new SpannableString(string);

        int startIndex = 0;

        for(int i = 0 ; i < split.length ; i++){

            String editable = split[i];
            if(map.containsKey(editable)){
                //Toast.makeText(getBaseContext(),String.valueOf(startIndex),Toast.LENGTH_SHORT).show();
                int index = string.indexOf(editable, startIndex);
                int color = map.get(editable);
                spannableString.setSpan(new ForegroundColorSpan(color),
                        index,
                        index + editable.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                startIndex = index + editable.length();
            }
            if(!map.containsKey(editable)) {
                int index = string.indexOf(editable, startIndex);

                spannableString.setSpan(new ForegroundColorSpan(Color.BLACK),
                        index,
                        index + editable.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                startIndex = index + editable.length();
            }
        }

        String[] split2 = string.split("");

        int startIndex2 = 0;

        for(int i = 0 ; i < split2.length ; i++){

            String editable = split2[i];
            if(map.containsKey(editable)){
                //Toast.makeText(getBaseContext(),String.valueOf(startIndex),Toast.LENGTH_SHORT).show();
                int index = string.indexOf(editable, startIndex2);
                int color = map.get(editable);
                spannableString.setSpan(new ForegroundColorSpan(color),
                        index,
                        index + editable.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                startIndex2 = index + editable.length();
            }

        }
        return spannableString;
    }

    private int getCharNumber(String text, char target) {
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == target) {
                count++;
            }
        }
        return count;
    }

    private void extractZipFile(Context context, String name, String filesDir) {
        String zipFileDir = filesDir + "/" + name;
        File file = new File(zipFileDir);
        if (!file.exists()) {
            try {
                String src = name + ".zip";
                InputStream is = context.getAssets().open(src);
                extract(context, src, filesDir);
            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }
        } else {
            Log.d(TAG, "Dir " + zipFileDir + " exist will not extract.");
        }
    }

    private void extract(Context context, String src, String des) {

        try {
            Log.d(TAG, "Begin extract " + src + " to " + des + ".");
            InputStream is = context.getAssets().open(src);
            ZipUtil.upZipFile(is, des);
            Log.d(TAG, "Extract " + src + " to " + des + " finished.");

        } catch (ZipException e) {
            Log.d(TAG, e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        } catch (Error e) {
            Log.d(TAG, e.getMessage());
        } catch (Throwable e) {
            Log.d(TAG, e.getMessage());
        }
    }
}