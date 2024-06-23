package com.example.rscheme_interpreter;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FileListActivity extends Activity {

    private ListView listView;
    private Button bt_open;
    private Button bt_delete;

    MainActivity mainActivity = (MainActivity) MainActivity.mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);

        listView = (ListView) findViewById(R.id.listView);
        bt_open = (Button) findViewById(R.id.bt_open);
        bt_delete = (Button) findViewById(R.id.bt_delete);

        // 빈 데이터 리스트 생성.
        ArrayList<String> items = new ArrayList<String>() ;
        // ArrayAdapter 생성. 아이템 View를 선택(single choice)가능하도록 만듦.
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, items) ;

        // listview 생성 및 adapter 지정.
        listView.setAdapter(adapter) ;

        String[] fileList= getBaseContext().fileList();
        for (int a =0;a<fileList.length;a++){
            items.add(fileList[a]);
        }
        bt_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count, checked ;
                count = adapter.getCount() ;

                if (count > 0) {
                    // 현재 선택된 아이템의 position 획득.
                    checked = listView.getCheckedItemPosition();

                    if (checked > -1 && checked < count) {
                        // 아이템 삭제
                        try {
                            mainActivity.setEditText(readFromFile(items.get(checked)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        finish();
                    }
                }
            }
        });
        bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count, checked ;
                count = adapter.getCount() ;

                if (count > 0) {
                    // 현재 선택된 아이템의 position 획득.
                    checked = listView.getCheckedItemPosition();

                    if (checked > -1 && checked < count) {
                        // 아이템 삭제
                        try {

                            mainActivity.setEditText(readFromFile(items.get(checked)));
                            getBaseContext().deleteFile(items.get(checked));
                            items.remove(checked) ;
                            // listview 선택 초기화.
                            listView.clearChoices();

                            // listview 갱신.
                            adapter.notifyDataSetChanged();
                            Toast.makeText(getBaseContext(),"삭제 완료",Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getBaseContext(),"삭제 실패(관리자에게 문의하세요.)",Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }
        });
    }

    //파일을 읽기위한 메소드
    public String readFromFile(String name) throws Exception {
        //2.(읽기) 받아온 이름경로 설정 하고
        FileInputStream fileInputStream = openFileInput(name);
        //3.(읽기) 버퍼에 연동해주기
        BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
        //4.(읽기) 스트링 버퍼 생성
        StringBuffer stringBuffer = new StringBuffer();

        String content = null; // 4.(읽기) 리더에서 라인을 받아오는데 받아올게 없을떄까지 반복
        while ((content = reader.readLine()) != null) {
            stringBuffer.append(content + "\n");
        }
        //사용한것들은 종료
        reader.close();
        fileInputStream.close();
        //5.(읽기)받아온 정보를 다시 리턴해준다
        return stringBuffer.toString();
    }
}