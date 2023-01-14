package bg.sofia.uni.fmi.mjt.newsfeed.model;

public class News {

    private String status;
    private int totalResults;
    private Article[] articles;

    public News(String status, int totalResults, Article[] articles) {

        this.status = status;
        this.totalResults = totalResults;
        this.articles = articles;
    }



}
