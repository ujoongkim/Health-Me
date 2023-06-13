package com.kwj.project1001;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PjJoinOkActivity extends AppCompatActivity {

    String joinId, joinPw, joinName, joinHp, joinAddr;
    Button btnJoinOk;
    TextView tvId, tvPw, tvName, tvHp, tvAddr;


    String TAG ="회원가입확인";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.w_join_ok);

        //Intent로 전달받은 값 받기.
        Intent getData = getIntent();
        joinId = getData.getStringExtra("id");
        joinPw = getData.getStringExtra("pw");
        joinName = getData.getStringExtra("name");
        joinHp = getData.getStringExtra("hp");
        joinAddr = getData.getStringExtra("addr");

        btnJoinOk = (Button)findViewById(R.id.btnJoinOk);

        tvId = (TextView) findViewById(R.id.tvId);
        tvPw = (TextView)findViewById(R.id.tvPw);
        tvName = (TextView)findViewById(R.id.tvName);
        tvHp = (TextView)findViewById(R.id.tvHp);
        tvAddr = (TextView)findViewById(R.id.tvAddr);

        //가져온 데이타를 textview에 넣기
        tvId.setText(joinId);
        tvPw.setText(joinPw);
        tvName.setText(joinName);
        tvHp.setText(joinHp);
        tvAddr.setText(joinAddr);


        btnJoinOk.setOnClickListener(mClickListener);



    }

    Button.OnClickListener mClickListener = new Button.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnJoinOk:
                    finish();
                    break;
            }
        }
    };
}