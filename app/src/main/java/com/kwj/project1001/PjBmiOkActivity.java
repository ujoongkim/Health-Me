package com.kwj.project1001;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PjBmiOkActivity extends AppCompatActivity {
    SQLiteDatabase db;
    MySQLiteOpenHelperBmi helper;
    Button btnOk, btnDel;

    TextView tvAge,tvTall, tvWeight, tvBmi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.w_bmi_ok);

        helper = new MySQLiteOpenHelperBmi(
                PjBmiOkActivity.this, // 현재 화면의 context
                "bmi.db", // 파일명
                null, // 커서 팩토리
                1); // 버전 번호


        btnOk = (Button)findViewById(R.id.btnOk);
        btnDel = (Button)findViewById(R.id.btnDel);

        tvAge = (TextView) findViewById(R.id.tvAge);
        tvTall = (TextView) findViewById(R.id.tvTall);
        tvWeight = (TextView) findViewById(R.id.tvWeight);
        tvBmi = (TextView) findViewById(R.id.tvBmi);

        btnOk.setOnClickListener(mClickListener);
        btnDel.setOnClickListener(mClickListener);

        //MemberList();
    }

    Button.OnClickListener mClickListener = new Button.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnOk:
                    Intent intent1 =
                            new Intent(PjBmiOkActivity.this, PjBmiActivity.class);
                    startActivity(intent1);
                    break;

                case R.id.btnDel:

                    tvAge.setText("");
                    tvTall.setText("");
                    tvWeight.setText("");
                    tvBmi.setText("");

            }
        }
    };
//    public void MemberList() {
//        // 1) db의 데이터를 읽어와서, 2) 결과 저장, 3)해당 데이터를 꺼내 사용
//        db = helper.getReadableDatabase(); // db객체를 얻어온다. 읽기 전용
//        Cursor c = db.rawQuery("SELECT * FROM bmi", null);
//
//        String Result = "";
//        while (c.moveToNext()) {
//            int idx = c.getInt(0);
//            String age = c.getString(1);
//            String tall = c.getString(2);
//            String weight = c.getString(3);
//            String bmi = c.getString(3);
//
//            Result += "   "+age+" | "+tall+" | "+weight+" | "+bmi+"\n";
//            Log.d("BMI 리스트",Result);
//
//        }
//        tvList.setText(Result);
//        c.close();
//        db.close();
//    }

    }


