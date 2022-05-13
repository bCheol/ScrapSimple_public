package com.example.simplescrap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    myDBHelper myDBHelper;
    SQLiteDatabase sqLiteDatabase;
    static int screenCheck = 0 ;
    NewsSearchScreen newsSearchScreen;
    ScrapListScreen scrapListScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("뉴스 검색");

        myDBHelper = new myDBHelper(this);
        sqLiteDatabase = myDBHelper.getWritableDatabase();
        myDBHelper.onCreate(sqLiteDatabase);

        linearLayout = findViewById(R.id.linearLayout);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.screen_news,linearLayout,true);
        newsSearchScreen = new NewsSearchScreen();
        scrapListScreen = new ScrapListScreen();
        newsSearchScreen.start();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(screenCheck == 1){
            scrapListScreen.start();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(R.id.newsSearch == item.getItemId()){
            setTitle("뉴스 검색");
            screenCheck = 0;
            linearLayout.removeAllViews();
            LayoutInflater newsInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            newsInflater.inflate(R.layout.screen_news,linearLayout,true);
            if(newsSearchScreen.newsAdapter.getItemCount()!=0){
                newsSearchScreen.reStart();
            }else {
                newsSearchScreen.start();
            }
        }
        else{
            setTitle("스크랩 목록");
            screenCheck = 1;
            linearLayout.removeAllViews();
            LayoutInflater scrapInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            scrapInflater.inflate(R.layout.screen_scrap, linearLayout, true);
            scrapListScreen.start();
        }
        return super.onOptionsItemSelected(item);
    }

    public static class myDBHelper extends SQLiteOpenHelper {
        public myDBHelper(Context context) {
            super(context, "scrapDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE if not exists scrapTable (id integer PRIMARY KEY AUTOINCREMENT, newsTitle text, newsLink text,scrapTitle text, scrapBody text,scrapDate text)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            String sql = "DROP TABLE IF EXISTS scrapTable";
            db.execSQL(sql);
            onCreate(db);
        }
    }
    //뉴스 검색 화면 관련
    class NewsSearchScreen{
        NewsAdapter newsAdapter;
        Retrofit retrofit;
        Service apiService;
        public NewsSearchScreen() {
            newsAdapter = new NewsAdapter();
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://openapi.naver.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            apiService = retrofit.create(Service.class);
        }
        //뉴스 검색 화면 (데이터가 있을 때)
        void reStart(){
            EditText searchEditText = linearLayout.findViewById(R.id.searchEditText);
            searchEditText.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    return keyCode == 66;
                }
            });

            RecyclerView searchRecyclerView = linearLayout.findViewById(R.id.searchRecyclerView);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            searchRecyclerView.setLayoutManager(layoutManager);
            searchRecyclerView.setAdapter(newsAdapter);
            newsAdapter.setOnItemClickListener(new OnNewsItemClickListener() {
                @Override
                public void onItemClick(NewsAdapter.ViewHolder holder, View view, int position) {
                    NewsData newsData = newsAdapter.getItem(position);
                    Intent intent = new Intent(getApplicationContext(), NewsView.class);
                    intent.putExtra("title", newsData.getTitle());
                    intent.putExtra("link", newsData.getLink());
                    intent.putExtra("description", newsData.getDescription());
                    intent.putExtra("pubDate", newsData.getPubDate());
                    startActivity(intent);
                }
            });

            Spinner spinner = linearLayout.findViewById(R.id.spinner);
            Button searchButton = linearLayout.findViewById(R.id.searchButton);

            String[] spinnerItem = {"유사도순","날짜순"};
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, spinnerItem);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerAdapter);
            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(searchEditText.getText().toString().equals("")){
                        newsAdapter.clear();
                        RecyclerView searchRecyclerView = linearLayout.findViewById(R.id.searchRecyclerView);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                        searchRecyclerView.setLayoutManager(layoutManager);
                        searchRecyclerView.setAdapter(newsAdapter);
                        Toast.makeText(getApplicationContext(), "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }else {
                        if(spinner.getSelectedItem().toString().equals("유사도순")){
                            searchStart(searchEditText.getText().toString(),"sim");
                        }else{
                            searchStart(searchEditText.getText().toString(),"date");
                        }
                    }
                }
            });
        }
        //뉴스 검색 화면 (데이터가 없을 때)
        void start(){
            EditText searchEditText = linearLayout.findViewById(R.id.searchEditText);
            searchEditText.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    return keyCode == 66;
                }
            });

            Spinner spinner = linearLayout.findViewById(R.id.spinner);
            Button searchButton = linearLayout.findViewById(R.id.searchButton);

            String[] spinnerItem = {"유사도순","날짜순"};
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, spinnerItem);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerAdapter);

            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(searchEditText.getText().toString().equals("")){
                        newsAdapter.clear();
                        RecyclerView searchRecyclerView = linearLayout.findViewById(R.id.searchRecyclerView);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                        searchRecyclerView.setLayoutManager(layoutManager);
                        searchRecyclerView.setAdapter(newsAdapter);
                        Toast.makeText(getApplicationContext(), "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }else {
                        if(spinner.getSelectedItem().toString().equals("유사도순")){
                            searchStart(searchEditText.getText().toString(),"sim");
                        }else{
                            searchStart(searchEditText.getText().toString(),"date");
                        }
                    }
                }
            });
        }
        //뉴스 검색 함수
        void searchStart(String searchText, String option){
            RecyclerView searchRecyclerView = linearLayout.findViewById(R.id.searchRecyclerView);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            searchRecyclerView.setLayoutManager(layoutManager);

            //clientId, clientSecret 비공개 (네이버에서 발급)
            String clientId = "" ;
            String clientSecret = "" ;
            Call<GetData> call = apiService.getData(searchText,20, option,clientId, clientSecret);
            newsAdapter.clear();
            call.enqueue(new Callback<GetData>() {
                @Override
                public void onResponse(@NonNull Call<GetData> call, @NonNull Response<GetData> response) {
                    if (response.isSuccessful()) {
                        GetData getData = response.body();
                        if (getData != null) {
                            if (getData.getItem().size() != 0) {
                                for (int i = 0; i < getData.getItem().size(); i++) {
                                    String title = getData.getItem().get(i).getTitle();
                                    String link = getData.getItem().get(i).getLink();
                                    String description = getData.getItem().get(i).getDescription();
                                    String pubDate = getData.getItem().get(i).getPubDate();
                                    newsAdapter.addItem(new NewsData(title, link, description, pubDate));
                                }
                                searchRecyclerView.setAdapter(newsAdapter);
                                newsAdapter.setOnItemClickListener(new OnNewsItemClickListener() {
                                    @Override
                                    public void onItemClick(NewsAdapter.ViewHolder holder, View view, int position) {
                                        NewsData newsData = newsAdapter.getItem(position);
                                        Intent intent = new Intent(getApplicationContext(), NewsView.class);
                                        intent.putExtra("title", newsData.getTitle());
                                        intent.putExtra("link", newsData.getLink());
                                        intent.putExtra("description", newsData.getDescription());
                                        intent.putExtra("pubDate", newsData.getPubDate());
                                        startActivity(intent);
                                    }
                                });
                            }else{
                                Toast.makeText(getApplicationContext(),"찾는 뉴스가 없습니다.",Toast.LENGTH_SHORT).show();
                                newsAdapter.clear();
                                searchRecyclerView.setAdapter(newsAdapter);
                            }
                        }
                    }
                }
                @Override
                public void onFailure(@NonNull Call<GetData> call, @NonNull Throwable t) {
                    Toast.makeText(getApplicationContext(),"통신에 실패했습니다.",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    //스크랩 목록 화면 관련
    class ScrapListScreen{
        ScrapAdapter scrapAdapter;
        public ScrapListScreen() {
            scrapAdapter = new ScrapAdapter();
        }
        //스크랩 목록 보기
        void start(){
            RecyclerView scrapRecyclerView = linearLayout.findViewById(R.id.scrapRecyclerView);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            scrapRecyclerView.setLayoutManager(layoutManager);

            scrapAdapter.clear();
            sqLiteDatabase = myDBHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("Select * from scrapTable Order by scrapDate DESC",null);
            int count = cursor.getCount();
            for(int i=0; i<count; i++){
                cursor.moveToNext();
                int id = cursor.getInt(0);
                String newsTitle = cursor.getString(1);
                String newsLink = cursor.getString(2);
                String scrapTitle = cursor.getString(3);
                String scrapBody = cursor.getString(4);
                String scrapDate = cursor.getString(5);
                scrapAdapter.addItem(new ScrapData(id, newsTitle,newsLink,scrapTitle,scrapBody,scrapDate));
            }
            cursor.close();
            sqLiteDatabase.close();
            scrapRecyclerView.setAdapter(scrapAdapter);
            scrapAdapter.setOnItemClickListener(new OnScrapItemClickListener() {
                @Override
                public void onItemClick(ScrapAdapter.ViewHolder holder, View view, int position) {
                    ScrapData scrapData = scrapAdapter.getItem(position);
                    Intent intent = new Intent(getApplicationContext(), ScrapView.class);
                    intent.putExtra("id", scrapData.getId());
                    intent.putExtra("newsTitle", scrapData.getNewsTitle());
                    intent.putExtra("newsLink", scrapData.getNewsLink());
                    intent.putExtra("scrapTitle", scrapData.getScrapTitle());
                    intent.putExtra("scrapBody", scrapData.getScrapBody());
                    intent.putExtra("scrapDate", scrapData.getScrapDate());
                    startActivity(intent);
                }
            });
        }
    }
}