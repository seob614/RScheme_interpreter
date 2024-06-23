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
import android.widget.Toast;

import java.util.ArrayList;

public class HelpListAdapter  extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<HelpListItem> helpListItemArrayList = new ArrayList<HelpListItem>();

    MainActivity mainActivity = (MainActivity) MainActivity.mainActivity;
    LearnActivity learnActivity = (LearnActivity) LearnActivity.learnActivity;

    Activity activity;
    String activity_name;
    public HelpListAdapter(Activity activity,String activity_name) {
        this.activity = activity;
        this.activity_name = activity_name;
    }

    @Override
    public int getCount() {
        return helpListItemArrayList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item_help, parent, false);
        }

        TextView titleTextView = (TextView) convertView.findViewById(R.id.textView1) ;
        Button bt_set = (Button) convertView.findViewById(R.id.bt_set);
        Button bt_exp = (Button) convertView.findViewById(R.id.bt_exp);

        HelpListItem listViewItem = helpListItemArrayList.get(position);

        titleTextView.setText(listViewItem.getTitle());

        bt_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity_name.equals("mainActivity")){
                    mainActivity.setFunc(listViewItem.getSet());
                }else if(activity_name.equals("learnActivity")){
                    learnActivity.setFunc(listViewItem.getSet());
                }

                activity.finish();
            }
        });

        bt_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ExpDialog.class);
                intent.putExtra("title",listViewItem.getTitle());
                intent.putExtra("exp",listViewItem.getExp());
                activity.startActivity(intent);
            }
        });

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public Object getItem(int position) {
        return helpListItemArrayList.get(position) ;
    }

    public void addItem(String title, String exp, String set) {
        HelpListItem item = new HelpListItem();
        item.setTitle(title);
        item.setExp(exp);
        item.setSet(set);

        helpListItemArrayList.add(item);
    }
}