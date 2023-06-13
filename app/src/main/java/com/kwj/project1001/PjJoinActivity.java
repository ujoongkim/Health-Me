package com.kwj.project1001;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PjJoinActivity extends AppCompatActivity {


    MySQLiteOpenHelper helper;

    String id,pw,name,hp,addr; //입력값을 변수에 저장해서 insert처리할변수

    Button btnJoin;
    EditText etId, etPw, etHp, etName, etAddr;
    int viewNumber;

    String TAG ="회원가입 예제";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.w_join);

        //데이베이스 생성.
        helper = new MySQLiteOpenHelper(
                PjJoinActivity.this, // 현재 화면의 context
                "member.db", // 파일명
                null, // 커서 팩토리
                1); // 버전 번호



        btnJoin = (Button)findViewById(R.id.btnJoin);

        etId = (EditText)findViewById(R.id.etId);
        etPw = (EditText)findViewById(R.id.etPw);
        etHp = (EditText)findViewById(R.id.etHp);
        etName = (EditText)findViewById(R.id.etName);
        etAddr = (EditText)findViewById(R.id.etAddr);

        btnJoin.setOnClickListener(mClickListener);

    }

    Button.OnClickListener mClickListener = new Button.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnJoin:
                    //공백체크
                    if(etId.getText().toString().equals(""))
                    {
                        Toast.makeText(PjJoinActivity.this, "아이디를 입력하세요.",  Toast.LENGTH_SHORT).show();
                        break;
                    }
                    //공백없이 입력잘되었을경우 변수에 저장
                    id = etId.getText().toString();

                    if(etPw.getText().toString().equals(""))
                    {
                        Toast.makeText(PjJoinActivity.this, "비번을 입력하세요.",  Toast.LENGTH_SHORT).show();
                        break;
                    }
                    //공백없이 입력잘되었을경우 변수에 저장
                    pw = etPw.getText().toString();

                    if(etName.getText().toString().equals(""))
                    {
                        Toast.makeText(PjJoinActivity.this, "이름을 입력하세요.",  Toast.LENGTH_SHORT).show();
                        break;
                    }
                    //공백없이 입력잘되었을경우 변수에 저장
                    name = etName.getText().toString();

                    if(etHp.getText().toString().equals(""))
                    {
                        Toast.makeText(PjJoinActivity.this, "연락처를 입력하세요.",  Toast.LENGTH_SHORT).show();
                        break;
                    }

                    //공백없이 입력잘되었을경우 변수에 저장
                    hp = etHp.getText().toString();

                    if(etAddr.getText().toString().equals(""))
                    {
                        Toast.makeText(PjJoinActivity.this, "주소를 입력하세요.",  Toast.LENGTH_SHORT).show();
                        break;
                    }

                    //공백없이 입력잘되었을경우 변수에 저장
                    addr = etAddr.getText().toString();

                    // 디비에 삽입..
                    insert(id, pw, name, hp, addr);

                    //회원가입 후 저장정보 확인하기.
                    Intent intentJoinOk = new Intent(PjJoinActivity.this, PjJoinOkActivity.class);
                    intentJoinOk.putExtra("id", id);
                    intentJoinOk.putExtra("pw", pw);
                    intentJoinOk.putExtra("name", name);
                    intentJoinOk.putExtra("hp", hp);
                    intentJoinOk.putExtra("addr", addr);
                    startActivity(intentJoinOk);
                    finish();
                    break;
            }
        }
    };


    //회원 가입 처리
    public void insert(String id, String pw, String name, String hp, String addr) {

        SQLiteDatabase db = helper.getWritableDatabase(); // db 객체를 얻어옴
        ContentValues values = new ContentValues();
        // db.insert의 매개변수인 values가 ContentValues 변수이므로 그에 맞춤
        // 데이터의 삽입은 put을 이용한다.
        values.put("id", id);
        values.put("pw", pw);
        values.put("name", name);
        values.put("hp", hp);
        values.put("addr", addr);
        db.insert("member", null, values);

        db.close();
        Toast.makeText(getApplicationContext(), name+"님 회원 가입 완료.", Toast.LENGTH_LONG).show();

        Log.d(TAG, id+"/"+pw+"/"+name+"/"+hp+"/"+addr+" 의 정보로 디비저장완료.");
    }







}