package com.example.rscheme_interpreter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class HelpDialog  extends FragmentActivity {
    private ListView listView;

    MainActivity mainActivity = (MainActivity) MainActivity.mainActivity;
    HelpListAdapter helpListAdapter;
    private String activity_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // setTheme(android.R.style.Theme_NoTitleBar);
        super.onCreate(savedInstanceState);

        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_help);

        Intent intent = getIntent();
        activity_name = intent.getStringExtra("activity_name");

        helpListAdapter = new HelpListAdapter(HelpDialog.this,activity_name);

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(helpListAdapter);

        helpListAdapter.addItem("and",
                "주어진 모든 표현식이 참인 경우에만 참을 반환"+
                        "\n\n사용방법: (and expression-1 expression-2 ...)" +
                        "\nexpression-n: 평가될 표현식",
                "(and )");
        helpListAdapter.addItem("begin",
                "주어진 표현식을 순차적으로 평가" +
                        "\n\n사용방법: (begin\n" +
                        " expression-1\n" +
                        " expression-2\n" +
                        " ...)" +
                        "\nexpression-n: 실행될 표현식",
                "(begin )");
        helpListAdapter.addItem("car",
                "리스트의 첫 번째 요소를 반환" +
                        "\n\n사용방법: (car lst)" +
                        "\nlst: 리스트",
                "(car )");
        helpListAdapter.addItem("cdr",
                "리스트에서 첫 번째 요소를 제외한 나머지 부분을 반환" +
                        "\n\n사용방법: (cdr lst)" +
                        "\nlst: 리스트",
                "(display )");
        helpListAdapter.addItem("cond",
                "주어진 여러 조건을 순차적으로 평가하여, 참인 첫 번째 조건에 해당하는 표현식을 실행하고 결과 반환" +
                        "\n\n사용방법: (cond\n" +
                        " (condition-1 expression-1)\n" +
                        " (condition-2 expression-2)\n" +
                        " ...\n" +
                        " (else default-expression))\n" +
                        "\ncondition: 조건을 평가하는 표현식\nexpression-n: 조건이 참일 때 실행되는 표현식\nelse: 모든 조건이 거짓인 경우 실행되는 표현식\ndefault-expression: 모든 조건이 거짓인 경우 실행되는 표현식",
                "(cond )");
        helpListAdapter.addItem("define",
                "변수에 값을 바인딩하는 데 사용" +
                        "\n\n사용방법: (define variable value)" +
                        "\nvariable: 변수의 이름을 나타내는 식별자\nvalue: 변수에 할당될 값 또는 표현식",
                "(define )");
        helpListAdapter.addItem("display",
                "주어진 표현식을 출력장치에 표시" +
                        "\n\n사용방법: (display expr)" +
                        "\nexpr: 출력하고자 하는 표현식",
                "(display )");
        helpListAdapter.addItem("if",
                "주어진 조건에 따라 다른 동작 실행" +
                        "\n\n사용방법: (if condition then-expression else-expression)" +
                        "\ncondition: 조건을 평가하는 표현식\nthen-expression: 조건이 참일 때 실행되는 표현식\nelse-expression: 조건이 거짓일 때 실행되는 표현식",
                "(if )");
        helpListAdapter.addItem("lambda",
                "함수를 생성"+
                        "\n\n사용방법: (lambda (parameters) body)" +
                        "\nparameters: 함수의 매개변수 목록\nbody: 함수가 수행할 표현식",
                "(lambda )");
        helpListAdapter.addItem("let",
                "지역 변수를 도입"+
                        "\n\n사용방법: (let ((variable-1 value-1)\n" +
                        "  (variable-2 value-2)\n" +
                        "  ...)\n" +
                        " body)\n" +
                        "\nvariable-n: 변수 이름\nvalue-n: 변수들에 할당될 값 또는 표현식\nbody: 지역 변수가 유효한 범위 내에서 실행될 표현식",
                "(let )");
        helpListAdapter.addItem("list",
                "리스트를 생성하고 조작" +
                        "\n\n사용방법: (list expr-1 expr-2 ...)" +
                        "\nexpr-n: 리스트에 포함될 요소",
                "(list )");
        helpListAdapter.addItem("newline",
                "새로운 줄을 생성하여 출력장치에 표시" +
                        "\n\n사용방법: (newline)",
                "(newline)");
        helpListAdapter.addItem("or",
                "주어진 여러 조건 중 하나라도 참이면 참을 반환"+
                        "\n\n사용방법: (or expression-1 expression-2 ...)" +
                        "\nexpression-n: 평가될 표현식",
                "(display )");
        helpListAdapter.addItem("set!",
                "변수의 값을 변경" +
                        "\n\n사용방법: (set! variable value)" +
                        "\nvariable: 이미 정의된 변수의 이름을 나타내는 식별자\nvalue: 변수에 새로 할당될 값 또는 표현식",
                "(set! )");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
