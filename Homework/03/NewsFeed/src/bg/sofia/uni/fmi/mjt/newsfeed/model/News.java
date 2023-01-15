package bg.sofia.uni.fmi.mjt.newsfeed.model;

import java.util.Arrays;
import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        News news = (News) o;
        return totalResults == news.totalResults && status.equals(news.status) &&
            Arrays.equals(articles, news.articles);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(status, totalResults);
        result = 31 * result + Arrays.hashCode(articles);
        return result;
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
