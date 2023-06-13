package com.kwj.project1001;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PjIntroActivity extends AppCompatActivity {
    TextView tvPhone;

    TextView tvMail;

    String tel, send;

    Uri number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.w_introduce);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        findViewById(R.id.btnClose).setOnClickListener(mClickListener);

    }

    public void onTelClicked(View v) {
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tel = (String) tvPhone.getText();      // textView를 string 형식으로 가져옴

        number = Uri.parse("tel:" + tel);
        Intent intent = new Intent(Intent.ACTION_DIAL, number); // intent에 전화번호 정보를 담아서
        startActivity(intent);  // 클릭 시 이벤트 발생
    }

    public void onMailClicked(View v) {

        tvMail = (TextView) findViewById(R.id.tvMail);
        send = (String) tvMail.getText();      // textView를 string 형식으로 가져옴

        Intent email = new Intent(Intent.ACTION_SEND);
        email.setType("plain/text");
        String[] address = {"email@address.com"};
        email.putExtra(Intent.EXTRA_EMAIL, address);
        email.putExtra(Intent.EXTRA_SUBJECT, "test@test");
        email.putExtra(Intent.EXTRA_TEXT, "내용 미리보기 (미리적을 수 있음)");
        startActivity(email);

    }

    Button.OnClickListener mClickListener = new Button.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnClose:
                    Intent intent1 =
                            new Intent(PjIntroActivity.this, PjProgramMainActivity.class);
                    startActivity(intent1);
                    break;


            }
        }
    };
}

