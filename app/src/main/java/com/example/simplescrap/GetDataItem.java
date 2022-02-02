package com.example.simplescrap;

public class GetDataItem {
    String title;   //개별 검색 결과
    String link;    //검색 결과 문서의 제공 네이버 하이퍼텍스트 link
    String description; //검색 결과 문서의 내용을 요약한 패시지 정보
    String pubDate; //검색 결과 문서가 네이버에 제공된 시간

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getPubDate() {
        return pubDate;
    }
}