package com.example.simplescrap;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

public class NewsView  extends AppCompatActivity {

    TextView pubDate, title, description, link;
    Button scrapButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsview);
        getSupportActionBar().hide();

        pubDate = findViewById(R.id.pubDate);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        link = findViewById(R.id.link);
        scrapButton = findViewById(R.id.scrapButton);

        Intent intent = getIntent();
        pubDate.setText(intent.getStringExtra("pubDate"));
        title.setText(intent.getStringExtra("title"));
        description.setText(intent.getStringExtra("description"));
        link.setText(intent.getStringExtra("link"));

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent linkIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(intent.getStringExtra("link")));
                startActivity(linkIntent);
            }
        });

        scrapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent scrapIntent = new Intent(getApplicationContext(), ScrapWrite.class);
                scrapIntent.putExtra("newsTitle", intent.getStringExtra("title"));
                scrapIntent.putExtra("newsLink", intent.getStringExtra("link"));
                startActivity(scrapIntent);
                finish();
            }
        });
    }
}