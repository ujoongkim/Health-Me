package com.kwj.project1001;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class PjAdminActivity extends AppCompatActivity {
    SQLiteDatabase db;
    MySQLiteOpenHelper helper;

    LinearLayout frame1, frame2, frame3;
    //회원목록
    TextView tvMemberList;

    //회원수정
    EditText etId, etPw, etName, etHp,etAddr;
    //회원삭제
    EditText etDelId;

    TextView tvDelList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.w_admin_main);
        helper = new MySQLiteOpenHelper(
                PjAdminActivity.this, // 현재 화면의 context
                "member.db", // 파일명
                null, // 커서 팩토리
                1); // 버전 번호

        // 삭제 모래시계 애니메이션ㅅ

        //프레임레이아웃 화면 id연동
        frame1 = (LinearLayout) findViewById(R.id.frame1);
        frame2 = (LinearLayout) findViewById(R.id.frame2);
        frame3 = (LinearLayout) findViewById(R.id.frame3);

        //회원목록
        tvMemberList = (TextView)findViewById(R.id.tvMemberList);
        //회원수정
        etId = (EditText)findViewById(R.id.etId);
        etPw = (EditText)findViewById(R.id.etPw);
        etName = (EditText)findViewById(R.id.etName);
        etHp = (EditText)findViewById(R.id.etHp);
        etAddr = (EditText)findViewById(R.id.etAddr);
        //회원삭제
        etDelId = (EditText)findViewById(R.id.etDelId);
        tvDelList = (TextView)findViewById(R.id.tvDelList);

        findViewById(R.id.btnAct1).setOnClickListener(mClick);
        findViewById(R.id.btnAct2).setOnClickListener(mClick);
        findViewById(R.id.btnAct3).setOnClickListener(mClick);

        findViewById(R.id.btnEditFind).setOnClickListener(mClick);
        findViewById(R.id.btnEdit).setOnClickListener(mClick);

        findViewById(R.id.btnDelFind).setOnClickListener(mClick);
        findViewById(R.id.btnDel).setOnClickListener(mClick);

        //첫화면 회원리스트라서 한번 호출
        selectMemberList();
    }

    View.OnClickListener mClick = new View.OnClickListener()
    {
        public void onClick(View v)
        {

            frame1.setVisibility(View.INVISIBLE);
            frame2.setVisibility(View.INVISIBLE);
            frame3.setVisibility(View.INVISIBLE);
            switch(v.getId())
            {
                case R.id.btnAct1://회원목록
                    frame1.setVisibility(View.VISIBLE);
                    selectMemberList();
                    break;
                case R.id.btnAct2://회원수정
                    frame2.setVisibility(View.VISIBLE);
                    break;
                case R.id.btnAct3://회원삭제
                    frame3.setVisibility(View.VISIBLE);
                    break;
                case R.id.btnDelFind:
                    frame3.setVisibility(View.VISIBLE);
                    String delId = etDelId.getText().toString();
                    if(delId.equals(""))
                    {
                        Toast.makeText(PjAdminActivity.this, "삭제할 아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    searchDelId(delId);

                    break;
                case R.id.btnDel:
                    frame3.setVisibility(View.VISIBLE);
                    String delIdOk = etDelId.getText().toString();
                    if(delIdOk.equals(""))
                    {
                        Toast.makeText(PjAdminActivity.this, "삭제할 아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    delete(delIdOk);

                    break;
                case R.id.btnEditFind:
                    frame2.setVisibility(View.VISIBLE);
                    String editId = etId.getText().toString();
                    if(editId.equals(""))
                    {
                        Toast.makeText(PjAdminActivity.this, "수정할 아이디를 입력해주세요!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    searchEditId(editId);

                    break;
                case R.id.btnEdit:
                    frame2.setVisibility(View.VISIBLE);
                    String id = etId.getText().toString();
                    String pw = etPw.getText().toString();
                    String name = etName.getText().toString();
                    String hp = etHp.getText().toString();
                    String addr = etAddr.getText().toString();
                    if(id.equals(""))
                    {
                        Toast.makeText(PjAdminActivity.this, "수정할 아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(pw.equals(""))
                    {
                        Toast.makeText(PjAdminActivity.this, "수정할 패스워드를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(name.equals(""))
                    {
                        Toast.makeText(PjAdminActivity.this, "수정할 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(hp.equals(""))
                    {
                        Toast.makeText(PjAdminActivity.this, "수정할 연락처를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(addr.equals(""))
                    {
                        Toast.makeText(PjAdminActivity.this, "수정할 주소를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    update(id,pw,name,hp,addr);

                    break;

            }

        }
    };

    // 회원 정보 수정
    public void update(String editId,String pw,String name, String hp, String addr) {
        db = helper.getWritableDatabase(); //db 객체를 얻어온다. 쓰기가능

        ContentValues values = new ContentValues();
        values.put("pw", pw);
        values.put("name", name);
        values.put("hp", hp);
        values.put("addr", addr);
        db.update("member", values, "id='"+editId+"'", null);

        db.close();
        Toast.makeText(getApplicationContext(), editId+"의 정보가 수정되었습니다.", Toast.LENGTH_SHORT).show();
        //수정완료후 초기화
        etId.setText("");
        etPw.setText("");
        etName.setText("");
        etHp.setText("");
        etAddr.setText("");
    }

    // 수정 아이디 찾기
    public void searchEditId(String editId) {

        // 1) db의 데이터를 읽어와서, 2) 결과 저장, 3)해당 데이터를 꺼내 사용
        db = helper.getReadableDatabase(); // db객체를 얻어온다. 읽기 전용
        Cursor c = db.rawQuery("SELECT * FROM member where id='"+editId+"'", null);

        String Result = "";
        boolean check=false;
        while (c.moveToNext()) {
            int idx = c.getInt(0);
            String id = c.getString(1);
            String pw = c.getString(2);
            String name = c.getString(3);
            String hp = c.getString(4);
            String addr = c.getString(5);

            etId.setText(id);
            etPw.setText(pw);
            etName.setText(name);
            etHp.setText(hp);
            etAddr.setText(addr);
            check=true;
        }
        if(check==false)
        {
            etPw.setText("");
            etName.setText("");
            etHp.setText("");
            etAddr.setText("");
            Toast.makeText(PjAdminActivity.this, "찾는 대상이 없습니다!", Toast.LENGTH_SHORT).show();
        }
        c.close();
        db.close();
    }
    // 회원 정보 삭제
    public void delete(String delId) {
        db = helper.getWritableDatabase();
        db.delete("member", "id='"+delId+"'", null);
        Log.d("db", delId + "가 정상적으로 삭제 되었습니다.");
        db.close();

        //삭제후처리
        etDelId.setText("");
        tvDelList.setText("대상이 삭제되었습니다.");
    }
    public void searchDelId(String delId) {
        // 1) db의 데이터를 읽어와서, 2) 결과 저장, 3)해당 데이터를 꺼내 사용
        db = helper.getReadableDatabase(); // db객체를 얻어온다. 읽기 전용
        Cursor c = db.rawQuery("SELECT * FROM member where id = '"+delId+"'", null);

        String Result = "";
        while (c.moveToNext()) {
            int idx = c.getInt(0);
            String id = c.getString(1);
            //String pw = c.getString(2);
            String name = c.getString(3);
            String hp = c.getString(4);
            String addr = c.getString(5);

            Result += id+" | "+name+" | "+hp+" | "+addr;
            Log.d("삭제대상정보",Result);

        }
        tvDelList.setText(Result);
        c.close();
        db.close();
    }
    public void selectMemberList() {
        // 1) db의 데이터를 읽어와서, 2) 결과 저장, 3)해당 데이터를 꺼내 사용
        db = helper.getReadableDatabase(); // db객체를 얻어온다. 읽기 전용
        Cursor c = db.rawQuery("SELECT * FROM member", null);

        String Result = "";
        while (c.moveToNext()) {
            int idx = c.getInt(0);
            String id = c.getString(1);
            //String pw = c.getString(2);
            String name = c.getString(3);
            String hp = c.getString(4);
            String addr = c.getString(5);

            Result += "   "+id+" | "+name+" | "+hp+" | "+addr+"\n";
            Log.d("회원리스트",Result);

        }
        tvMemberList.setText(Result);
        c.close();
        db.close();
    }
}