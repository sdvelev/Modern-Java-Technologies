package bg.sofia.uni.fmi.mjt.newsfeed.model;

public record Article(String[] source, String author, String title, String description, String url, String urlToImage,
                      String publishedAt, String content) {

}
