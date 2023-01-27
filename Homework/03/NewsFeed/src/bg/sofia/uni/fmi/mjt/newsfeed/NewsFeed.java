package bg.sofia.uni.fmi.mjt.newsfeed;

import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.IncorrectRequestException;
import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.NewsFeedException;
import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.ServerErrorException;
import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.TooManyRequestsException;
import bg.sofia.uni.fmi.mjt.newsfeed.exceptions.UnauthorizedException;
import bg.sofia.uni.fmi.mjt.newsfeed.model.News;
import com.google.gson.Gson;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;


public class NewsFeed {

    private static final String API_KEY = "39a9b7c91568455daf456c7a21900063";
    private static final String API_ENDPOINT_SCHEME = "https";
    private static final String API_ENDPOINT_HOST = "newsapi.org";
    private static final String API_ENDPOINT_PATH = "/v2/top-headlines";
    private final static String KEYWORDS_BEGIN = "q=";
    private final static String KEYWORDS_SEPARATOR = "+";
    private final static String CATEGORY_BEGIN = "category=";
    private final static String GENERAL_SEPARATOR = "&";
    private final static String COUNTRY_BEGIN = "country=";
    private final static String PAGE_SIZE_BEGIN = "pageSize=";
    private final static String PAGE_BEGIN = "page=";
    private final static String API_KEY_BEGIN = "apiKey=";

    private static final int PAGE_SIZE = 50;
    private static final int MAX_PAGES = 3;
    private final static int TOO_MANY_REQUESTS = 429;

    private static final Gson GSON = new Gson();

    private final HttpClient newsFeedHttpClient;
    private final String apiKey;

    public NewsFeed(HttpClient newsFeedHttpClient) {
        this(newsFeedHttpClient, API_KEY);
    }

    public NewsFeed(HttpClient newsFeedHttpClient, String apiKey) {
        this.newsFeedHttpClient = newsFeedHttpClient;
        this.apiKey = apiKey;
    }

    /**
     * @param queryData QueryData created in builder pattern; list of keywords for searching is
     *                  mandatory; country and category are optional
     * @return list of the news returned by the NewsAPI (considering page size and max pages)
     * @throws IllegalArgumentException   if the parameter is null
     * @throws IncorrectRequestException  if the request is unacceptable, e.g. a missing or misconfigured parameter
     * @throws UnauthorizedException      if API key is missing from the request, or is incorrect
     * @throws TooManyRequestsException   if too many requests within a window of time hava been made
     * @throws ServerErrorException       if server is inaccessible or not working properly
     */
    public List<News> getNewsFeed(QueryData queryData) throws UnauthorizedException,
        TooManyRequestsException, ServerErrorException, IncorrectRequestException {

        validateNull(queryData);

        List<News> result = new ArrayList<>();
        for (int i = 1; i <= MAX_PAGES; i++) {

            News receivedNews = getNewsFeed(queryData, i);
            result.add(receivedNews);

            if (receivedNews.totalResults() < i * PAGE_SIZE) {
                break;
            }
        }

        return result;
    }

    private void validateNull(QueryData queryData) {

        if (queryData == null) {
            throw new IllegalArgumentException("The given argument cannot be null.");
        }
    }

    private boolean validateStringArguments(String parameter) {

        return (parameter != null && !parameter.isEmpty() && !parameter.isBlank());
    }

    private void validateStatusCode(int statusCode) throws IncorrectRequestException, UnauthorizedException,
        TooManyRequestsException, ServerErrorException {

        switch (statusCode) {

            case HTTP_BAD_REQUEST -> throw new IncorrectRequestException("The request was unacceptable, " +
                "often due to a missing or misconfigured parameter");
            case HTTP_UNAUTHORIZED -> throw new UnauthorizedException("Your API key was missing " +
                "from the request, or wasn't correct.");
            case TOO_MANY_REQUESTS -> throw new TooManyRequestsException("You made too many requests " +
                "within a window of time and have been rate limited.");
            case HTTP_INTERNAL_ERROR -> throw new ServerErrorException("Something went wrong with connection.");
        }

        throw new NewsFeedException("Unexpected response code and behaviour from news feed service.");
    }

    private News getNewsFeed(QueryData queryData, int pageNumber)
        throws IncorrectRequestException, TooManyRequestsException, UnauthorizedException, ServerErrorException {

        HttpResponse<String> response;

        try {
            URI uri = constructURI(queryData, pageNumber);
            HttpRequest request = HttpRequest.newBuilder().uri(uri).build();

            response = this.newsFeedHttpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new IncorrectRequestException("Could not retrieve news feed", e);
        }

        if (response.statusCode() != HTTP_OK) {

            validateStatusCode(response.statusCode());
        }

        return GSON.fromJson(response.body(), News.class);
    }

    private void appendData(List<String> keywords, String category, String country, int pageNumber,
                            StringBuffer apiEndpointQuery) {

        if (keywords != null && !keywords.isEmpty()) {

            apiEndpointQuery.append(KEYWORDS_BEGIN);
            for (String currentKeyword : keywords) {
                apiEndpointQuery.append((currentKeyword));
                apiEndpointQuery.append(KEYWORDS_SEPARATOR);
            }
        }
        apiEndpointQuery.deleteCharAt(apiEndpointQuery.length() - 1);
        apiEndpointQuery.append(GENERAL_SEPARATOR);

        if (validateStringArguments(category)) {
            apiEndpointQuery.append(CATEGORY_BEGIN).append(category).append(GENERAL_SEPARATOR);
        }

        if (validateStringArguments(country)) {
            apiEndpointQuery.append(COUNTRY_BEGIN).append(country).append(GENERAL_SEPARATOR);
        }

        apiEndpointQuery.append(PAGE_SIZE_BEGIN + PAGE_SIZE + GENERAL_SEPARATOR + PAGE_BEGIN).append(pageNumber)
            .append(GENERAL_SEPARATOR).append(API_KEY_BEGIN).append(this.apiKey);
    }

    private URI constructURI(QueryData queryData, int pageNumber) {

        List<String> keywords = queryData.getKeywords();
        String category = queryData.getCategory();
        String country = queryData.getCountry();

        StringBuffer apiEndpointQuery = new StringBuffer();

        appendData(keywords, category, country, pageNumber, apiEndpointQuery);


        try {


            return new URI(API_ENDPOINT_SCHEME, API_ENDPOINT_HOST, API_ENDPOINT_PATH,
                apiEndpointQuery.toString(), null);
        } catch (URISyntaxException e) {

            throw new RuntimeException("There is an exception while creating URI.", e);
        }
    }
}
