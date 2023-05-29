package com.example.myapplication10;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SubActivity extends AppCompatActivity implements View.OnClickListener {
    public EditText etNcs;
    public EditText etgenuine;
    public EditText etNum;
    public TextView teforce;
    public TextView teName;
    public TextView teType;
    public TextView teTrial;
    public Button btStart;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        etNcs = (EditText) findViewById(R.id.edNCS);
        etgenuine = (EditText) findViewById(R.id.edG);
        etNum = (EditText) findViewById(R.id.edNUM);
        etgenuine.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                String filteredInput = input.replaceAll("[^a-zA-Z]", ""); // 영어만 허용
                if (!input.equals(filteredInput)) {
                    etgenuine.setText(filteredInput);
                    etgenuine.setSelection(filteredInput.length());
                }
            }
        });

        etNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                String filteredInput = input.replaceAll("[^0-9]", ""); // 숫자만 허용
                if (!input.equals(filteredInput)) {
                    etNum.setText(filteredInput);
                    etNum.setSelection(filteredInput.length());
                }
            }
        });



        teforce = (TextView)findViewById(R.id.textView);
        teName = (TextView)findViewById(R.id.textView2);
        teType = (TextView)findViewById(R.id.textView3);
        teTrial = (TextView)findViewById(R.id.textView4);

        btStart = (Button) findViewById(R.id.button);

        btStart.setOnClickListener(this);

        // 현재 화면이 내부 화면인지 외부 화면인지 확인하여 버튼의 가로 크기 조절
        boolean isInnerScreen = getResources().getConfiguration().isScreenRound();
        if (isInnerScreen) {
            // 내부 화면인 경우, 버튼의 가로 크기를 스마트폰 화면의 가로 크기의 일정 비율로 설정
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) btStart.getLayoutParams();
            layoutParams.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.8);
            btStart.setLayoutParams(layoutParams);
        } else {
            // 외부 화면인 경우, 버튼의 가로 크기를 wrap_content로 설정
            ViewGroup.LayoutParams layoutParams = btStart.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            btStart.setLayoutParams(layoutParams);
        }
    }






    @Override
    public void onClick(View v) {
        String ncs = etNcs.getText().toString();
        String genuine = etgenuine.getText().toString();
        String num = etNum.getText().toString();

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("ncs", ncs);
        intent.putExtra("genuine", genuine);
        intent.putExtra("num", num);
        startActivity(intent);
    }
}