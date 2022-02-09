package com.example.simplescrap;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ScrapWrite extends AppCompatActivity {

    TextView newsTitle, newsLink;
    EditText scrapTitle, scrapBody;
    Button scrapSave;
    MainActivity.myDBHelper myDBHelper;
    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrapwrite);
        getSupportActionBar().hide();

        myDBHelper = new MainActivity.myDBHelper(getApplicationContext());

        newsTitle = findViewById(R.id.newsTitle);
        newsLink = findViewById(R.id.newsLink);
        scrapTitle = findViewById(R.id.scrapTitle);
        scrapTitle.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return keyCode == 66;
            }
        });
        scrapBody = findViewById(R.id.scrapBody);
        scrapSave = findViewById(R.id.scrapSave);

        Intent intent = getIntent();
        newsTitle.setText(intent.getStringExtra("newsTitle"));
        newsLink.setText(intent.getStringExtra("newsLink"));
        newsLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent clickIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(intent.getStringExtra("newsLink")));
                startActivity(clickIntent);
            }
        });

        scrapSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(scrapTitle.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else if(scrapBody.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss", Locale.KOREA);
                    String getTime = simpleDateFormat.format(date);

                    sqLiteDatabase = myDBHelper.getWritableDatabase();
                    String sql = "Insert into scrapTable (newsTitle, newsLink, scrapTitle, scrapBody, scrapDate) Values('"
                            + intent.getStringExtra("newsTitle") + "','"
                            + intent.getStringExtra("newsLink") + "','"
                            + scrapTitle.getText().toString() + "','"
                            + scrapBody.getText().toString() + "','"
                            + getTime + "');" ;
                    sqLiteDatabase.execSQL(sql);
                    sqLiteDatabase.close();
                    finish();
                    Toast.makeText(getApplicationContext(), "스크랩을 저장했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}