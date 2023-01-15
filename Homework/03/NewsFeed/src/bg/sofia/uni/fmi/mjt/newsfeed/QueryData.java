package bg.sofia.uni.fmi.mjt.newsfeed;

import javax.management.Query;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class QueryData {

    //Required parameters
    private final List<String> keywords;

    //optional parameters
    private String category;
    private String country;

    public List<String> getKeywords() {
        return keywords;
    }

    public String getCategory() {
        return category;
    }

    public String getCountry() {
        return country;
    }

    public static QueryDataBuilder builder(List<String> keywords) {

        return new QueryDataBuilder(keywords);
    }

    private QueryData(QueryDataBuilder builder) {

        this.keywords = builder.keywords;
        this.category = builder.category;
        this.country = builder.country;
    }

    //Builder Class
    public static class QueryDataBuilder {

        //Required parameters
        private List<String> keywords;

        //optional parameters
        private String category;
        private String country;

        private QueryDataBuilder(List<String> keywords) {

            this.keywords = keywords;
        }

        public QueryDataBuilder setCategory(String category) {

            this.category = category;
            return this;
        }

        public QueryDataBuilder setCountry(String country) {

            this.country = country;
            return this;
        }

        public QueryData build() {

            return new QueryData(this);
        }

    }

}
