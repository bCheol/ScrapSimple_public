package com.example.simplescrap;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface Service {
    @GET("news.json")
    Call<GetData> getData(@Query("query") String query,
                          @Query("display") int count,
                          @Query("sort") String sort,
                          @Header("X-Naver-Client-Id") String id,
                          @Header("X-Naver-Client-Secret") String pw);
}