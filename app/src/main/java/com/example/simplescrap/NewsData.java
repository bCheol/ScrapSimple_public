package com.example.simplescrap;

public class NewsData {
    String title;
    String link;
    String description;
    String pubDate;

    public NewsData(String title, String link, String description, String pubDate) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.pubDate = pubDate;
    }

    public String getTitle() {
        return title.replace("<b>","").replace("</b>","")
                .replace("&quot;","\"").replace("&nbsp;"," ").replace("&lt;","<")
                .replace("&gt;",">").replace("&amp;","&").replace("&#035;;","#")
                .replace("&#039;","'");
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description.replace("<b>","").replace("</b>","")
                .replace("&quot;","\"").replace("&nbsp;"," ").replace("&lt;","<")
                .replace("&gt;",">").replace("&amp;","&").replace("&#035;;","#")
                .replace("&#039;","'");
    }

    public String getPubDate() {
        return pubDate;
    }
}
