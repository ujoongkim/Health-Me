package com.kwj.project1001;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PjFindId extends AppCompatActivity {

    SQLiteDatabase db;
    MySQLiteOpenHelper helper;

    EditText etName, etHp, etAddr, etId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.w_findid);

        helper = new MySQLiteOpenHelper(
                PjFindId.this, // 현재 화면의 context
                "member.db", // 파일명
                null, // 커서 팩토리
                1); // 버전 번호

        etName = (EditText) findViewById(R.id.etName);
        etHp = (EditText) findViewById(R.id.etHp);
        etAddr = (EditText) findViewById(R.id.etAddr);
        etId = (EditText) findViewById(R.id.etId);
        findViewById(R.id.btnFind).setOnClickListener(mClick);
    }

    View.OnClickListener mClick = new View.OnClickListener() {
        public void onClick(View v) {

            switch(v.getId()) {

                case R.id.btnFind:

                    String findName = etName.getText().toString();
                    String findHp = etHp.getText().toString();
                    String findAddr = etAddr.getText().toString();

                    if(findName.equals(""))
                    {
                        Toast.makeText(PjFindId.this, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(findHp.equals(""))
                    {
                        Toast.makeText(PjFindId.this, "전화번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(findAddr.equals(""))
                    {
                        Toast.makeText(PjFindId.this, "주소를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    searchId(findName);
                    break;
            }
        }


    };
    public void searchId(String findId) {

        // 1) db의 데이터를 읽어와서, 2) 결과 저장, 3)해당 데이터를 꺼내 사용
        db = helper.getReadableDatabase(); // db객체를 얻어온다. 읽기 전용
        Cursor c = db.rawQuery("SELECT * FROM member where name='"+findId+"'", null);
        String Result = "";
        boolean check=false;
        while (c.moveToNext()) {
            int idx = c.getInt(0);
            String id = c.getString(1);
            String pw = c.getString(2);
            String name = c.getString(3);
            String hp = c.getString(4);

            etId.setText(id);
            etName.setText(name);
            etHp.setText(hp);
            etAddr.setText(pw);
            check=true;
        }
        if(check==false)
        {
            etName.setText("");
            etHp.setText("");
            etAddr.setText("");
            Toast.makeText(PjFindId.this, "찾는 대상이 없습니다.", Toast.LENGTH_SHORT).show();
        }
        c.close();
        db.close();
    }
}
