package com.example.rscheme_interpreter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class QuizListAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<QuizListItem> quizListItems = new ArrayList<QuizListItem>();

    LearnActivity learnActivity = (LearnActivity) LearnActivity.learnActivity;

    public QuizListAdapter() {
    }

    @Override
    public int getCount() {
        return quizListItems.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item_quiz, parent, false);
        }

        TextView titleTextView = (TextView) convertView.findViewById(R.id.textView1) ;

        QuizListItem listViewItem = quizListItems.get(position);

        titleTextView.setText(listViewItem.getTitle());

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public Object getItem(int position) {
        return quizListItems.get(position) ;
    }

    public void addItem(String title, String quiz, String hint, String answer) {
        QuizListItem item = new QuizListItem();
        item.setTitle(title);
        item.setQuiz(quiz);
        item.setHint(hint);
        item.setAnswer(answer);

        quizListItems.add(item);
    }
}