package com.kwj.project1001;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;

public class PjProgramMainActivity extends AppCompatActivity implements View.OnClickListener {
    static String msg;
    static String[] exerList;
    static int[] layoutList;
    TextView exercise_text;
    static ImageView exercise_image;
    Button Progarm_btn, Alarm_btn, Intro_btn;
    AlarmManager mAlarmMgr;
    Button[] exerBtn = new Button[6];

    NotificationManager manager;
    NotificationCompat.Builder builder;

    private static String CHANNEL_ID = "channel1";
    private static String CHANEL_NAME = "Channel1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.w_progarm_main);

        // 버튼 선언
        Progarm_btn = (Button) findViewById(R.id.btn_program);
        Alarm_btn = (Button) findViewById(R.id.btn_alarm);
        Intro_btn = (Button) findViewById(R.id.btn_intro);
        exercise_text = (TextView) findViewById(R.id.main_text);
        exercise_image = (ImageView) findViewById(R.id.main_image);

        exerBtn[0] = (Button) findViewById(R.id.btn_exerA);
        exerBtn[1] = (Button) findViewById(R.id.exerA_more);

        exerBtn[2] = (Button) findViewById(R.id.btn_exerB);
        exerBtn[3] = (Button) findViewById(R.id.exerB_more);

        exerBtn[4] = (Button) findViewById(R.id.btn_exerC);
        exerBtn[5] = (Button) findViewById(R.id.exerC_more);

        /* 알람 매니저 사용을 위한 mAlarmMgr 선언 */
        mAlarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);

        /* 프로그램 액티비티 출력 */
        Progarm_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplication(), "BMI 프로그램으로 이동합니다.",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getBaseContext(), PjBmiActivity.class);
                startActivity(intent);
            }
        });

        /* 알람 기능 시작 */
        Alarm_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBtnAlarm1();
            }
        });

        /* 앱 소개 화면 출력 */
        Intro_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), PjIntroActivity.class);
                startActivity(intent);
            }
        });

        Intent pintent = this.getIntent();  // 프로그램 선택에서 넘어온 intent 받기
        msg = pintent.getStringExtra("msg");

        // 프로그램별 다른 버튼 text 출력을 위한 기능
        if (msg != null) {
            exerList = getIntent().getStringArrayExtra("exerList");
            layoutList = getIntent().getIntArrayExtra("layoutList");
            for (int i = 0; i < 6; i++) {
                if ((i % 2) == 0)
                    exerBtn[i].setText(exerList[i]);
                exerBtn[i].setTag(i);
                exerBtn[i].setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View v) {
        Button btn = (Button) v;

        for (Button tmpBtn : exerBtn) {
            if (tmpBtn == btn) {
                final int s = (Integer) v.getTag();

                try {
                    if ((s % 2) == 0) {
                        try {
                            exercise_text.setText(exerList[s + 1]); Glide.with(PjProgramMainActivity.this).load(R.drawable.buffy_ep).into(exercise_image);// 지울지 말지 고민
                        } catch (NumberFormatException e) {
                            exercise_text.setText("운동 정보 없음");
                        }
                    }

                    else {
                        try {
                            Context mContext = getApplicationContext(); // 현재 앱의 context

                            //레이아웃 플레터를 통해 팝업 창 구현
                            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

                            // 팝업을 띄울 layout 설정
                            View layout = inflater.inflate(layoutList[s / 2], (ViewGroup) findViewById(R.id.exerPopup));
                            AlertDialog.Builder aDialog = new AlertDialog.Builder(PjProgramMainActivity.this);

                            aDialog.setView(layout);   // 출력할 뷰를 위에 설정한 layout으로 설정

                            AlertDialog ad = aDialog.create();
                            ad.show();      // 팝업 출력
                        } catch (Exception e) {
                            Toast.makeText(getApplication(), "부연설명 없음",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplication(), "click Error", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /* 알람 기능 구현 */
    public void onBtnAlarm1() {

        // 수행할 동작 생성
        Intent intent = new Intent(this, PjProgramMainActivity.class);

        // 휴대폰 상단 state 바에 알람 추가

        showNoti();

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pIntent = PendingIntent.getActivity(PjProgramMainActivity.this, 0, intent, 0);

        // 1회 알람 시작
        mAlarmMgr.set(AlarmManager.RTC, System.currentTimeMillis() + 10000, pIntent);   // 테스트를 위해 10초로 구현
        Toast.makeText(getApplicationContext(), "알람 등록이 완료되었습니다. (10초)", Toast.LENGTH_SHORT).show();// 알람 등록이 성공하면 메세지 출력

        //10초 뒤에 화면 복귀 시 토스트 메세지
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(PjProgramMainActivity.this, "운동을 종료합니다.", Toast.LENGTH_SHORT).show();
            }
        }, 10000);

    }

    private void showNoti() {

        builder = null;

        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            manager.createNotificationChannel(
                    new NotificationChannel(CHANNEL_ID, CHANEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            );

            builder = new NotificationCompat.Builder(this,CHANNEL_ID);

            //하위 버전일 경우
        }else{
            builder = new NotificationCompat.Builder(this);
        }

        //알림창 제목
        builder.setContentTitle("알림");

        //알림창 메시지
        builder.setContentText("10초 뒤, Health & me 의 메인화면으로 이동합니다.");

        //알림창 아이콘
        builder.setSmallIcon(R.drawable.man);

        Notification notification = builder.build();

        //알림창 실행
        manager.notify(1,notification);
    }
}