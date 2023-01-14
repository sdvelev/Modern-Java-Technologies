package bg.sofia.uni.fmi.mjt.newsfeed.model;

import java.util.Arrays;

public class News {

    private String status;
    private int totalResults;
    private Article[] articles;

    public News(String status, int totalResults, Article[] articles) {

        this.status = status;
        this.totalResults = totalResults;
        this.articles = articles;
    }

    public String getStatus() {
        return status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public Article[] getArticles() {
        return articles;
    }

    @Override
    public String toString() {
        return "News{" +
            "status='" + status + '\'' +
            ", totalResults=" + totalResults +
            ", articles=" + Arrays.toString(articles) +
            '}';
    }
}
