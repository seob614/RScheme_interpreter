package com.example.rscheme_interpreter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class QuizListActivity extends Activity {

    private ListView listView;
    MainActivity mainActivity = (MainActivity) MainActivity.mainActivity;
    LearnActivity learnActivity = (LearnActivity) LearnActivity.learnActivity;
    QuizListAdapter quizListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_list);

        listView = (ListView) findViewById(R.id.listView);

        quizListAdapter = new QuizListAdapter();
        final String[] scheme_quiz_arr = getResources().getStringArray(R.array.scheme_quiz);
        for (int a =0;a<scheme_quiz_arr.length;a++){
            quizListAdapter.addItem(scheme_quiz_arr[a].substring(0,scheme_quiz_arr[a].indexOf("$")),
                    scheme_quiz_arr[a].substring(scheme_quiz_arr[a].indexOf("$")+1,scheme_quiz_arr[a].indexOf("%")),
                    scheme_quiz_arr[a].substring(scheme_quiz_arr[a].indexOf("%")+1,scheme_quiz_arr[a].indexOf("^")),
                    scheme_quiz_arr[a].substring(scheme_quiz_arr[a].indexOf("^")+1));
        }
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(quizListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QuizListItem selectedItem = (QuizListItem) parent.getItemAtPosition(position);
                learnActivity.setQuiz(selectedItem.getTitle(),selectedItem.getQuiz(),selectedItem.getHint(),selectedItem.getAnswer());
                finish();
            }
        });

    }

}
