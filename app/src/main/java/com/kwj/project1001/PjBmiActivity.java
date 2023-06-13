package com.kwj.project1001;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

public class PjBmiActivity extends AppCompatActivity {
    SQLiteDatabase db;
    MySQLiteOpenHelperBmi helper;

    public static final String TAG = "PjBMIActivity";
    private EditText BMI_view;
    private EditText e_age, e_tall, e_weight;
    private ToggleButton tgl_sex;
    private ImageView Image;

    TextView tvSave;

    static TextView tvList;
    Button BMI_count, show_program;
    String age, tall, weight, bmi;
    double temp_tall, temp_weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.w_bmi);

        BMI_view = (EditText) findViewById(R.id.BMI_view);
        e_age = (EditText) findViewById(R.id.bmi_edit_age);
        e_tall = (EditText) findViewById(R.id.bmi_edit_tall);
        e_weight = (EditText) findViewById(R.id.bmi_edit_weight);
        tgl_sex = (ToggleButton) findViewById(R.id.bmi_toggle);
        BMI_count = (Button) findViewById(R.id.bmi_count);
        show_program = (Button) findViewById(R.id.bmi_btn_program);
        Image = (ImageView) findViewById(R.id.bmi_image);

        //데이베이스 생성.
        helper = new MySQLiteOpenHelperBmi(
                PjBmiActivity.this, // 현재 화면의 context
                "bmi.db", // 파일명
                null, // 커서 팩토리
                1); // 버전 번호

        BMI_count.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    age = e_age.getText().toString();
                    tall = e_tall.getText().toString();
                    weight = e_weight.getText().toString();
                    bmi = BMI_view.getText().toString();

                    temp_tall = Double.valueOf(tall).doubleValue(); // 키를 Double 형식으로 변형
                    temp_weight = Double.valueOf(weight).doubleValue(); // 몸무게를 Double 형식으로 변형

                    double temp_BMI = bmi_calcul(temp_tall, temp_weight);
                    String BMI = String.format("%.2f", temp_BMI);   // BMI 소수점 2자리까지 출력

                    //BMI에 따른 이미지 출력//
                    if(temp_BMI < 18.5) {
                        Image.setImageResource(R.drawable.thin);  Toast.makeText(getApplicationContext(), "계산이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                    else if(temp_BMI >= 18.5 && temp_BMI <25.0) {
                        Image.setImageResource(R.drawable.normal); Toast.makeText(getApplicationContext(), "계산이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                    else if(temp_BMI >= 25.0 && temp_BMI <30.0) {
                        Image.setImageResource(R.drawable.over);  Toast.makeText(getApplicationContext(), "계산이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Image.setImageResource(R.drawable.heavy);  Toast.makeText(getApplicationContext(), "계산이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    }

                    BMI_view.setText(BMI);  // BMI 결과창에 입력한 신체 정보의 BMI 계산 결과 출력
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "계산할 수 없습니다.", Toast.LENGTH_SHORT).show();
                    BMI_view.setText("0");
                }

            }
        });
//데이타베이스 메서드 처리  //

        show_program.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(getApplicationContext(), PjProgramActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /* BMI 계산 ==> 몸무게 나누기 키의 제곱 */
    public double bmi_calcul(double tall_, double weight_) {
        double bmi_ = weight_ /tall_ / tall_ * 10000;
        return bmi_;
    }

    public void onSaveClicked(View v) {
        tvSave = (TextView) findViewById(R.id.tvSave);
        insert(age, tall, weight, bmi);

        Toast.makeText(getApplicationContext(), "결과가 저장되었습니다.", Toast.LENGTH_SHORT).show();
    }
    public void insert(String age, String tall, String weight, String bmi) {

        SQLiteDatabase db = helper.getWritableDatabase(); // db 객체를 얻어온다. 쓰기 가능

        //값들을 컨트롤 하려고 클래스 생성
        ContentValues values = new ContentValues();

        // db.insert의 매개변수인 values가 ContentValues 변수이므로 그에 맞춤
        // 데이터의 삽입은 put을 이용한다.
        values.put("age", age);
        values.put("tall", tall);
        values.put("weight", weight);
        values.put("bmi", bmi);
        db.insert("bmi", null, values);
        // tip : 마우스를 db.insert에 올려보면 매개변수가 어떤 것이 와야 하는지 알 수 있다.
        db.close();
        Log.d(TAG, age+"/"+tall+"/"+weight+"/"+bmi+" 의 정보로 디비저장완료.");
    }

    public void onBmiClicked(View v) {
        Image = (ImageView) findViewById(R.id.bmi_image);
        Intent intent1 = new Intent(PjBmiActivity.this, PjBmiOkActivity.class);
        startActivity(intent1);  // 클릭 시 이벤트 발생

    }

}

