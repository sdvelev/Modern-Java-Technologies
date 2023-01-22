package bg.sofia.uni.fmi.mjt.newsfeed.model;

import java.util.Arrays;
import java.util.Objects;

public record News(String status, int totalResults, Article[] articles) {

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

    /*@Override
    public String toString() {
        return "News{" +
            "status='" + status + '\'' +
            ", totalResults=" + totalResults +
            ", articles=" + Arrays.toString(articles) +
            '}';
    }*/
}
