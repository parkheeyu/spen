package com.example.myapplication10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

        teforce = (TextView)findViewById(R.id.textView);
        teName = (TextView)findViewById(R.id.textView2);
        teType = (TextView)findViewById(R.id.textView3);
        teTrial = (TextView)findViewById(R.id.textView4);

        btStart = (Button) findViewById(R.id.button);

        btStart.setOnClickListener(this);



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