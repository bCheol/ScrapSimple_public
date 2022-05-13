package com.example.simplescrap;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ScrapView extends AppCompatActivity {

    TextView newsTitle, newsLink, scrapTitle, scrapBody, scrapDate;
    Button changeButton, deleteButton;
    MainActivity.myDBHelper myDBHelper;
    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrapview);
        getSupportActionBar().hide();

        myDBHelper = new MainActivity.myDBHelper(getApplicationContext());

        newsTitle = findViewById(R.id.newsTitle);
        newsLink = findViewById(R.id.newsLink);
        scrapTitle = findViewById(R.id.scrapTitle);
        scrapBody = findViewById(R.id.scrapBody);
        scrapDate = findViewById(R.id.scrapDate);
        changeButton = findViewById(R.id.changeButton);
        deleteButton = findViewById(R.id.deleteButton);

        Intent intent = getIntent();
        newsTitle.setText(intent.getStringExtra("newsTitle"));
        newsLink.setText(intent.getStringExtra("newsLink"));
        scrapTitle.setText(intent.getStringExtra("scrapTitle"));
        scrapBody.setText(intent.getStringExtra("scrapBody"));
        scrapDate.setText(intent.getStringExtra("scrapDate"));

        newsLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent linkIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(intent.getStringExtra("newsLink")));
                startActivity(linkIntent);
            }
        });
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent changeIntent = new Intent(getApplicationContext(), ScrapChange.class);
                changeIntent.putExtra("id",intent.getIntExtra("id",0));
                changeIntent.putExtra("newsTitle",intent.getStringExtra("newsTitle"));
                changeIntent.putExtra("newsLink",intent.getStringExtra("newsLink"));
                changeIntent.putExtra("scrapTitle",intent.getStringExtra("scrapTitle"));
                changeIntent.putExtra("scrapBody",intent.getStringExtra("scrapBody"));
                changeIntent.putExtra("scrapDate",intent.getStringExtra("scrapDate"));
                startActivity(changeIntent);
                finish();
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqLiteDatabase = myDBHelper.getWritableDatabase();
                String sql = "Delete from scrapTable Where id = " + intent.getIntExtra("id",0) + ";" ;
                sqLiteDatabase.execSQL(sql);
                sqLiteDatabase.close();
                finish();
                Toast.makeText(getApplicationContext(),"스크랩을 삭제했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}