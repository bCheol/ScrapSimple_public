package com.example.simplescrap;

public class ScrapData {
    int id;
    String newsTitle;
    String newsLink;
    String scrapTitle;
    String scrapBody;
    String scrapDate;

    public ScrapData(int id, String newsTitle, String newsLink, String scrapTitle, String scrapBody, String scrapDate) {
        this.id = id;
        this.newsTitle = newsTitle;
        this.newsLink = newsLink;
        this.scrapTitle = scrapTitle;
        this.scrapBody = scrapBody;
        this.scrapDate = scrapDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsLink() {
        return newsLink;
    }

    public String getScrapTitle() {
        return scrapTitle;
    }

    public String getScrapBody() {
        return scrapBody;
    }

    public String getScrapDate() {
        return scrapDate;
    }
}
