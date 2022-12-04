package bg.sofia.uni.fmi.mjt.netflix;

import java.util.ArrayList;
import java.util.List;

public record Content(String id, String title, ContentType type, String description, int releaseYear, int runtime,
                      List<String> genres, int seasons, String imdbId, double imdbScore, double imdbVotes) {

    public static final int ID_POSITION = 0;
    public static final int TITLE_POSITION = 1;
    public static final int TYPE_POSITION = 2;
    public static final int DESCRIPTION_POSITION = 3;
    public static final int RELEASE_YEAR_POSITION = 4;
    public static final int RUNTIME_POSITION = 5;
    public static final int GENRES_POSITION = 6;
    public static final int SEASONS_POSITION = 7;
    public static final int IMDB_ID_POSITION = 8;
    public static final int IMDB_SCORE_POSITION = 9;
    public static final int IMDB_VOTES_POSITION = 10;

    public static final String CONTENT_ATTRIBUTE_DELIMITER = ",";

    private final static double COEFFICIENT_M = 10_000.0;

    private static ContentType getContentTypeFromString(String contentType) {

        if (contentType.equals("MOVIE")) {

            return ContentType.MOVIE;
        }

        return ContentType.SHOW;
    }

    private static List<String> getGenresFromString(String genres) {

        List<String> result = new ArrayList<>();

        // Remove leading and ending brackets
        genres = genres.strip().replaceFirst("\\[", "").strip().replaceFirst("]", "");

        String[] genresAsString = genres.split(";");

        for (int i = 0; i < genresAsString.length; i++) {

            genresAsString[i] = genresAsString[i].strip();
            genresAsString[i] = new StringBuilder(genresAsString[i]).deleteCharAt(0).reverse()
                .deleteCharAt(0).reverse().toString();
            genresAsString[i] = genresAsString[i].strip();
        }

        for (int i = 0; i < genresAsString.length; i++) {

            result.add(genresAsString[i]);
        }

        return result;
    }

    public static Content of(String line) {

        String[] fields  = line.split(CONTENT_ATTRIBUTE_DELIMITER);

        String id = fields[ID_POSITION];

        String title = fields[TITLE_POSITION];

        ContentType type = getContentTypeFromString(fields[TYPE_POSITION]);

        String description = fields[DESCRIPTION_POSITION];

        int releaseYear = Integer.parseInt(fields[RELEASE_YEAR_POSITION]);

        int runtime = Integer.parseInt(fields[RUNTIME_POSITION]);

        List<String> genres = getGenresFromString(fields[GENRES_POSITION]);

        int seasons = Integer.parseInt(fields[SEASONS_POSITION]);

        String imdbId = fields[IMDB_ID_POSITION];

        double imdbScore = Double.parseDouble(fields[IMDB_SCORE_POSITION]);

        double imdbVotes = Double.parseDouble(fields[IMDB_VOTES_POSITION]);

        return new Content(id, title, type, description, releaseYear, runtime, genres, seasons, imdbId,
            imdbScore, imdbVotes);
    }

    public double weighedRating(NetflixRecommender netflixRecommender) {

        double v = this.imdbVotes();

        double m = COEFFICIENT_M;

        double coefficientR = this.imdbScore();

        double coefficientC = netflixRecommender.getAverageIMDBScore();

        // (v ÷ (v + m)) × R + (m ÷ (v + m)) × C
        return (v / (v + m)) * coefficientR + (m / (v + m)) * coefficientC;
    }

    public int getSimilarity(Content content) {

        int counter = 0;

        for (String genre : this.genres) {

            if (content.genres.contains(genre)) {
                ++counter;
            }
        }

        return counter;
    }
}
