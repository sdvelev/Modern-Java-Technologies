package bg.sofia.uni.fmi.mjt.newsfeed;

import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.IncorrectRequestException;
import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.NewsFeedException;
import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.ServerError;
import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.TooManyRequestsException;
import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.UnauthorizedException;
import bg.sofia.uni.fmi.mjt.newsfeed.model.Article;
import bg.sofia.uni.fmi.mjt.newsfeed.model.News;
import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewsFeed {

    private static final String API_KEY = "39a9b7c91568455daf456c7a21900063";
    private static final String API_ENDPOINT_SCHEME = "https";
    private static final String API_ENDPOINT_HOST = "newsapi.org";
    private static final String API_ENDPOINT_PATH = "/v2/top-headlines";
    private static final String API_ENDPOINT_QUERY = "q=%s&units=metric&lang=bg&appid=%s";

    private static final int PAGE_SIZE = 10;
    private static final int MAX_PAGES = 4;

    private static final Gson GSON = new Gson();

    private final HttpClient newsFeedHttpClient;
    private final String apiKey;

    public NewsFeed(HttpClient  newsFeedHttpClient) {
        this(newsFeedHttpClient, API_KEY);
    }

    public NewsFeed(HttpClient newsFeedHttpClient, String apiKey) {
        this.newsFeedHttpClient = newsFeedHttpClient;
        this.apiKey = apiKey;
    }

    /*public static void getNewsFeed(QueryData queryData) throws Exception {
        ExecutorService executor = Executors.newCachedThreadPool();
        HttpClient client =
            HttpClient.newBuilder().executor(executor).build(); // configure custom executor or use the default

        URI uri = constructURI(queryData);
        System.out.println(uri.toString());

        HttpRequest request = HttpRequest.newBuilder().uri(uri).build();
        System.out.println("Thread calling sendAsync(): " + Thread.currentThread().getName());

        CompletableFuture<String> future = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(x -> {
                System.out.println("Thread executing thenApply(): " + Thread.currentThread().getName());
                return x.body();
            });
        future.thenAcceptAsync(x -> {
            System.out.println("Thread executing thenAccept(): " + Thread.currentThread().getName());
            System.out.println(x);
        }, executor);
        System.out.println("The HTTP call is fired. Performing some other work...");

        // wait the async HTTP call which is executed in daemon thread
        future.join();

        executor.shutdown();
    }*/

    public List<News> getNewsFeed(QueryData queryData) throws IncorrectRequestException, UnauthorizedException,
        TooManyRequestsException, ServerError {

        List<News> result = new ArrayList<>();
        for (int i = 1; i <= MAX_PAGES; i++) {

            result.add(getNewsFeed(queryData, i));
        }

        return result;
    }

    private void validateStatusCode(int statusCode) throws IncorrectRequestException, UnauthorizedException,
        TooManyRequestsException, ServerError {

        switch (statusCode) {

            case 400 -> throw new IncorrectRequestException("The request was unacceptable, often due to a missing or " +
                "misconfigured parameter");
            case 401 -> throw new UnauthorizedException("Your API key was missing from the request, or wasn't " +
                "correct.");
            case 429 -> throw new TooManyRequestsException("You made too many requests within a window of time and " +
                "have been rate limited.");
            case 500 -> throw new ServerError("Something went wrong.");
        }

        throw new NewsFeedException("Unexpected response code and behaviour from news feed service.");
    }

    private News getNewsFeed(QueryData queryData, int pageNumber) throws IncorrectRequestException,
        UnauthorizedException, TooManyRequestsException, ServerError {

        HttpResponse<String> response;

        try {
            URI uri = constructURI(queryData, pageNumber);
            System.out.println(uri.toString());
            HttpRequest request = HttpRequest.newBuilder().uri(uri).build();

            response = this.newsFeedHttpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new IncorrectRequestException("Could not retrieve news feed", e);
        }

        if (response.statusCode() != 200) {

            validateStatusCode(response.statusCode());
        }

        return GSON.fromJson(response.body(), News.class);
    }

    private static StringBuffer appendData(List<String> keywords, String category, String country, int pageNumber,
                                           StringBuffer apiEndpointQuery) {

        if (keywords != null && !keywords.isEmpty()) {

            apiEndpointQuery.append("q=");
            for (String currentKeyword : keywords) {
                apiEndpointQuery.append((currentKeyword));
            }
            apiEndpointQuery.append("&");
        }

        if (category != null && !category.isEmpty() && !category.isBlank()) {
            apiEndpointQuery.append("category=" + category + "&");
        }

        if (country != null && !country.isEmpty() && !country.isBlank()) {
            apiEndpointQuery.append("country=" + country + "&");
        }

        apiEndpointQuery.append("pageSize=" + PAGE_SIZE + "&page=" + pageNumber + "&apiKey=" + API_KEY);
        return apiEndpointQuery;
    }

    private static URI constructURI(QueryData queryData, int pageNumber) {

        List<String> keywords = queryData.getKeywords();
        String category = queryData.getCategory();
        String country = queryData.getCountry();

        StringBuffer apiEndpointQuery = new StringBuffer();

        apiEndpointQuery = appendData(keywords, category, country, pageNumber, apiEndpointQuery);

        try {
            URI toReturn = new URI(API_ENDPOINT_SCHEME, API_ENDPOINT_HOST, API_ENDPOINT_PATH,
                apiEndpointQuery.toString(), null);

            return toReturn;
        } catch (Exception e) {

            throw new RuntimeException("There is an exception while creating URI.", e);
        }
    }

    public static void main(String[] args) throws Exception {

        HttpClient client = HttpClient.newBuilder().build();
        NewsFeed n = new NewsFeed(client, API_KEY);

        List<News> returned = n.getNewsFeed(QueryData.builder("president").build());

        for (News currentNews : returned) {
            System.out.println(currentNews);
        }
    }

}
