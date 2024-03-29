package bg.sofia.uni.fmi.mjt.newsfeed;

import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.IncorrectRequestException;
import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.NewsFeedException;
import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.ServerErrorException;
import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.TooManyRequestsException;
import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.UnauthorizedException;
import bg.sofia.uni.fmi.mjt.newsfeed.model.Article;
import bg.sofia.uni.fmi.mjt.newsfeed.model.News;
import bg.sofia.uni.fmi.mjt.newsfeed.model.Source;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.mockito.Mockito.when;

public class NewsFeedTest {

    private static News sampleNews;
    private static String sampleNewsJson;
    private final static int HTTP_TOO_MANY_REQUESTS = 429;
    @Mock
    private HttpClient newsFeedHttpClientMock = Mockito.mock(HttpClient.class);

    @Mock
    private HttpResponse<String> newsFeedHttpResponseMock = Mockito.mock(HttpResponse.class);

    private NewsFeed newsFeed = new NewsFeed(newsFeedHttpClientMock);

    @BeforeAll
    static void setUpClass() {

        Article article = new Article(new Source("444", "bbc"), "Ivan Ivanov", "Important Article",
            "This is an important article", "This is URL element", "Here is to be urlToImage",
            "15.01.2023", "Here is to be the content of thr article.");
        sampleNews = new News("ok", 1, new Article[] {article});
        sampleNewsJson = new Gson().toJson(sampleNews);
    }

    @BeforeEach
    void setUp() throws IOException, InterruptedException {

        when(newsFeedHttpClientMock.send(Mockito.any(HttpRequest.class),
            ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
            .thenReturn(newsFeedHttpResponseMock);

        this.newsFeed = new NewsFeed(newsFeedHttpClientMock);
    }

    @Test
    void testGetNewsFeedSuccessfully() throws TooManyRequestsException, IncorrectRequestException,
        UnauthorizedException, ServerErrorException {

        when(this.newsFeedHttpResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(this.newsFeedHttpResponseMock.body()).thenReturn(sampleNewsJson);

        var result = this.newsFeed.getNewsFeed(QueryData.builder(List.of("Important")).build());

        Assertions.assertEquals(result.get(0), sampleNews, "The actual news is not the same" +
            " as the expected");
    }

    @Test
    void testGetNewsFeedTwoNews() throws TooManyRequestsException, IncorrectRequestException, UnauthorizedException,
        ServerErrorException {

        Article articleFirst = new Article(new Source("444", "bbc"), "Ivan Ivanov", "Important Article",
            "This is an important article", "This is URL element", "Here is to be urlToImage",
            "15.01.2023", "Here is to be the content of thr article.");

        Article articleSecond = new Article(new Source("200", "btv"), "Georgi Markov",
            "Another Article", "Sample Description", "Sample URL element",
            "Sample urlToImage", "17.01.2023", "Sample content of article.");
        News sampleNewsLocal = new News("ok", 2, new Article[] {articleFirst, articleSecond});
        String sampleNewsJsonLocal = new Gson().toJson(sampleNewsLocal);

        when(this.newsFeedHttpResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(this.newsFeedHttpResponseMock.body()).thenReturn(sampleNewsJsonLocal);

        var result = this.newsFeed.getNewsFeed(QueryData.builder(List.of("Article"))
            .setCategory("general").setCountry("gb").build());

        Assertions.assertEquals(2, result.get(0).totalResults(),
            "Actual received results are not the same as the expected");
        Assertions.assertEquals("ok", result.get(0).status(), "Actual status is not the same as the expected");
        Assertions.assertEquals("Ivan Ivanov", result.get(0).articles()[0].author(),
            "The author of the first article is not the same as the expected.");
    }

    @Test
    void testGetNewsFeedSixtyNews() throws TooManyRequestsException, IncorrectRequestException, UnauthorizedException,
        ServerErrorException {

        Article article = new Article(new Source("444", "bbc"), "Ivan Ivanov", "Important Article",
            "This is an important article", "This is URL element", "Here is to be urlToImage",
            "15.01.2023", "Here is to be the content of thr article.");

        Article[] articles = new Article[60];

        for (int i = 0; i < 60; i++) {
            articles[i] = article;
        }

        News sampleNewsLocal = new News("ok", articles.length, articles);
        String sampleNewsJsonLocal = new Gson().toJson(sampleNewsLocal);

        when(this.newsFeedHttpResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(this.newsFeedHttpResponseMock.body()).thenReturn(sampleNewsJsonLocal);

        var result = this.newsFeed.getNewsFeed(QueryData.builder(List.of("Article"))
            .setCategory("general").setCountry("gb").build());

        Assertions.assertEquals(60, result.get(0).totalResults(),
            "Actual received results are not the same as the expected");
        Assertions.assertEquals("ok", result.get(0).status(), "Actual status is not the same as the expected");
        Assertions.assertEquals("Ivan Ivanov", result.get(0).articles()[59].author(),
            "The author of the last article is not the same as the expected.");
    }

    @Test
    void testGetNewsFeedMissingAPIKey() {

        when(this.newsFeedHttpResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_UNAUTHORIZED);

        Assertions.assertThrows(UnauthorizedException.class, () ->
                this.newsFeed.getNewsFeed(QueryData.builder(List.of("Education")).build()),
            "UnauthorizedException is expected but not thrown");
    }

    @Test
    void testGetNewsFeedMissingIncorrectRequest() {

        when(this.newsFeedHttpResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_BAD_REQUEST);

        Assertions.assertThrows(IncorrectRequestException.class, () ->
                this.newsFeed.getNewsFeed(QueryData.builder(List.of("Education")).build()),
            "IncorrectRequestException is expected but not thrown");
    }

    @Test
    void testGetNewsFeedMissingTooManyRequests() {

        when(this.newsFeedHttpResponseMock.statusCode()).thenReturn(HTTP_TOO_MANY_REQUESTS);

        Assertions.assertThrows(TooManyRequestsException.class, () ->
                this.newsFeed.getNewsFeed(QueryData.builder(List.of("Education")).build()),
            "TooManyRequestsException is expected but not thrown");
    }

    @Test
    void testGetNewsFeedServerError() {

        when(this.newsFeedHttpResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_INTERNAL_ERROR);

        Assertions.assertThrows(ServerErrorException.class, () ->
                this.newsFeed.getNewsFeed(QueryData.builder(List.of("Education")).build()),
            "ServerErrorException is expected but not thrown");
    }

    @Test
    void testGetNewsFeedNewsFeedException() {

        when(this.newsFeedHttpResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_NOT_FOUND);

        Assertions.assertThrows(NewsFeedException.class, () ->
                this.newsFeed.getNewsFeed(QueryData.builder(List.of("Education")).build()),
            "NewsFeedException is expected but not thrown");
    }

    @Test
    void testGetNewsFeedWithNullParameter() {

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                this.newsFeed.getNewsFeed(null),
            "Given argument cannot be null");
    }
}
