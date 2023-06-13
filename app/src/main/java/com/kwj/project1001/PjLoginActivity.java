package com.kwj.project1001;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PjLoginActivity extends AppCompatActivity {

    EditText etId, etPw;
    CheckBox autoLogin;
    SharedPreferences setting;
    SharedPreferences.Editor editor;

    TextView findPw, findId;

    //SQLITE 데이타베이스 관련변수
    SQLiteDatabase db;
    MySQLiteOpenHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.w_main);

        helper = new MySQLiteOpenHelper(
                PjLoginActivity.this, // 현재 화면의 context
                "member.db", // 파일명
                null, // 커서 팩토리
                1); // 버전 번호


        etId = (EditText)findViewById(R.id.etId);
        etPw = (EditText)findViewById(R.id.etPw);
        autoLogin = (CheckBox) findViewById(R.id.autoLogin);

        findViewById(R.id.btnLogin).setOnClickListener(mClick);
        findViewById(R.id.btnJoin).setOnClickListener(mClick);

        /* 자동로그인을 위한 SharedPreference 설정 */
        setting = getSharedPreferences("Login", 0);   // key 값 = Login
        editor = setting.edit();

        /* 자동로그인이 다음 접속때도 계속 설정되도록 하는 기능 */
        if(setting.getBoolean("autoLogin", false)){
            etId.setText(setting.getString("id", ""));
            etPw.setText(setting.getString("pw", ""));
            autoLogin.setChecked(true);
        }

        /* 자동 로그인 설정 */
        autoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {     // 자동로그인이 체크되면
                    String userID = etId.getText().toString();
                    String password = etPw.getText().toString();
                    Toast.makeText(PjLoginActivity.this, "자동 로그인 체크",  Toast.LENGTH_SHORT).show();
                    /* preference에 메일과 비밀번호 저장, 자동로그인 체크 */
                    editor.putString("id",userID);
                    editor.putString("pw",password);
                    editor.putBoolean("autoLogin",true);
                    editor.commit();

                }else {
                    Toast.makeText(PjLoginActivity.this, "자동 로그인 해제",  Toast.LENGTH_SHORT).show();
                    /* 체크가 해제되면 기존 저장 정보 삭제 */
                    editor.remove("id");
                    editor.remove("pw");
                    editor.remove("autoLogin");
                    editor.clear();
                    editor.commit();
                }
            }
        });
    }

    View.OnClickListener mClick = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            switch(v.getId())
            {
                case R.id.btnLogin:
                    String id = etId.getText().toString();//화면에 입력한 아이디 가져오기
                    String pw = etPw.getText().toString();//화면에 입력한 패스워드 가져오기
                    if(id.equals("")){
                        Toast.makeText(PjLoginActivity.this, "아이디를 입력하세요.",  Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(pw.equals("")){
                        Toast.makeText(PjLoginActivity.this, "비밀번호를 입력하세요.",  Toast.LENGTH_SHORT).show();
                        return;
                    }
                    boolean loginCheck = false;

                    //디비 테이블에 id, pw 보내서 로그인 처리
                    loginCheck = dbLoginCheck(id,pw);


                    if(loginCheck == true)
                    {
                        Toast.makeText(getApplication(), "로그인 완료",
                                Toast.LENGTH_SHORT).show();
                        Intent login = new Intent(PjLoginActivity.this, PjProgramMainActivity.class);
                        startActivity(login);
                    }

                   else if (id.equals("admin") && pw.equals("1004")) {
                        Toast.makeText(getApplication(), "로그인 완료",
                                Toast.LENGTH_SHORT).show();
                        Intent admin = new Intent(getApplicationContext(), PjAdminActivity.class);
                        startActivity(admin);
                    }
                    else {
                        Toast.makeText(PjLoginActivity.this, "로그인 실패",  Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btnJoin:
                    Intent join = new Intent(PjLoginActivity.this, PjJoinActivity.class);
                    startActivity(join);
                    break;
            }

        }
    };

    public boolean dbLoginCheck(String loginId, String loginPw) {

        db = helper.getReadableDatabase(); // db객체를 얻어온다. 읽기 전용
        Cursor c = db.rawQuery("SELECT * FROM member", null);

        while (c.moveToNext()) {

            int idx = c.getInt(0);
            String id = c.getString(1);
            String pw = c.getString(2);
            String name = c.getString(3);
            String hp = c.getString(4);
            String addr = c.getString(5);

            Log.d("로그인", "idx: " + idx + ", id : " + id + ", name : " + name
                    + ", hp : " + hp+ ", addr : " + addr);
            if(loginId.equals(id))
            {
                if(loginPw.equals(pw))
                {
                    //아이디 패스워드를 최종적으로 잘 맞을경우 다 닫고 true값 리턴
                    c.close();
                    db.close();
                    Log.d("로그인-성공", "idx: " + idx + ", id : " + id + ", name : " + name
                            + ", hp : " + hp+ ", addr : " + addr);
                    return true;
                }
            }


        }
        c.close();
        db.close();

        return false;
    }

    public void onFindClicked1(View v) {
        findPw = (TextView) findViewById(R.id.findPw);
        Intent intent1 = new Intent(PjLoginActivity.this, PjFindPw.class);
        startActivity(intent1);  // 클릭 시 이벤트 발생
    }

    public void onFindClicked2(View v) {
        findId = (TextView) findViewById(R.id.findId);
        Intent intent2 = new Intent(PjLoginActivity.this, PjFindId.class);
        startActivity(intent2);  // 클릭 시 이벤트 발생
    }

}