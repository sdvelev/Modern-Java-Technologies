package bg.sofia.uni.fmi.mjt.netflix;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class NetflixRecommenderTest {

    private NetflixRecommender netflixRecommender;

    @BeforeEach
    void setTests() throws IOException {

        String line = "id,title,type,description,release_year,runtime,genres,seasons,imdb_id,imdb_score,imdb_votes\n" +
            "tm84618,Taxi Driver,MOVIE,A mentally unstable Vietnam War veteran works as a night-time taxi driver in New York City.,1976,114,['drama'; 'crime'],-1,tt0075314,8.2,808582.0\n" +
            "tm154986,Deliverance,MOVIE,Intent on seeing the Cahulawassee River before it's turned into one huge lake.,1972,109,['drama'; 'action'; 'thriller'; 'european'],-1,tt0068473,7.7,107673.0\n" +
            "tm127384,Monty Python and the Holy Grail,MOVIE,King Arthur; accompanied by his squire; recruits his Knights of the Round Table.,1975,91,['fantasy'; 'action'; 'comedy'],-1,tt0071853,8.2,534486.0\n" +
            "tm120801,The Dirty Dozen,MOVIE,12 American military prisoners in World War II are ordered to infiltrate a well-guarded enemy château and kill the Nazi officers vacationing there. The soldiers; most of whom are facing death sentences for a variety of violent crimes; agree to the mission and the possible commuting of their sentences.,1967,150,['war'; 'action'],-1,tt0061578,7.7,72662.0\n";
        var readerToPass = new StringReader(line);
        this.netflixRecommender = new NetflixRecommender(readerToPass);

    }

    @Test
    void testGetAllContent() {

        var expected = List.of(new Content("tm84618", "Taxi Driver", ContentType.MOVIE,
            "A mentally unstable Vietnam War veteran works as a night-time taxi driver in New York City.",
            1976, 114, List.of("drama", "crime"), -1, "tt0075314", 8.2, 808582.0),
            new Content("tm154986", "Deliverance", ContentType.MOVIE,
                "Intent on seeing the Cahulawassee River before it's turned into one huge lake.",
                1972, 109, List.of("drama", "action", "thriller", "european"), -1, "tt0068473", 7.7, 107673.0),
            new Content("tm127384", "Monty Python and the Holy Grail", ContentType.MOVIE,
                "King Arthur; accompanied by his squire; recruits his Knights of the Round Table.",
                1975, 91, List.of("fantasy", "action", "comedy"), -1, "tt0071853", 8.2, 534486.0),
            new Content("tm120801", "The Dirty Dozen", ContentType.MOVIE,
                "12 American military prisoners in World War II are ordered to infiltrate a well-guarded enemy château and kill the Nazi officers vacationing there. The soldiers; most of whom are facing death sentences for a variety of violent crimes; agree to the mission and the possible commuting of their sentences.",
                1967, 150, List.of("war", "action"), -1, "tt0061578", 7.7, 72662.0)
            );

            assertTrue(expected.containsAll(this.netflixRecommender.getAllContent()) &&
                    this.netflixRecommender.getAllContent().containsAll(expected) ,
                "Actual content is not the same as the expected");
    }

    @Test
    void testGetAllGenres() {

        List<String> expected = List.of("drama", "action", "thriller", "european", "fantasy", "comedy", "war", "crime");

        assertTrue(expected.containsAll(this.netflixRecommender.getAllGenres()) &&
            this.netflixRecommender.getAllGenres().containsAll(expected),
            "Actual list of genres is not the same as expected");
    }

    @Test
    void testGetTheLongestMovie() {

        Content expected = new Content("tm120801", "The Dirty Dozen", ContentType.MOVIE,
            "12 American military prisoners in World War II are ordered to infiltrate a well-guarded enemy château and kill the Nazi officers vacationing there. The soldiers; most of whom are facing death sentences for a variety of violent crimes; agree to the mission and the possible commuting of their sentences.",
            1967, 150, List.of("war", "action"), -1, "tt0061578", 7.7, 72662.0);

        assertEquals(expected, this.netflixRecommender.getTheLongestMovie(), "Actual longest movie is not the expected");
    }

    @Test
    void  testGroupContentByType() {

        Map<ContentType, Set<Content>> expected = new HashMap<>();

        var expectedSet = Set.of(new Content("tm84618", "Taxi Driver", ContentType.MOVIE,
                "A mentally unstable Vietnam War veteran works as a night-time taxi driver in New York City.",
                1976, 114, List.of("drama", "crime"), -1, "tt0075314", 8.2, 808582.0),
            new Content("tm154986", "Deliverance", ContentType.MOVIE,
                "Intent on seeing the Cahulawassee River before it's turned into one huge lake.",
                1972, 109, List.of("drama", "action", "thriller", "european"), -1, "tt0068473", 7.7, 107673.0),
            new Content("tm127384", "Monty Python and the Holy Grail", ContentType.MOVIE,
                "King Arthur; accompanied by his squire; recruits his Knights of the Round Table.",
                1975, 91, List.of("fantasy", "action", "comedy"), -1, "tt0071853", 8.2, 534486.0),
            new Content("tm120801", "The Dirty Dozen", ContentType.MOVIE,
                "12 American military prisoners in World War II are ordered to infiltrate a well-guarded enemy château and kill the Nazi officers vacationing there. The soldiers; most of whom are facing death sentences for a variety of violent crimes; agree to the mission and the possible commuting of their sentences.",
                1967, 150, List.of("war", "action"), -1, "tt0061578", 7.7, 72662.0)
        );

        expected.put(ContentType.MOVIE, expectedSet);

        assertTrue(this.netflixRecommender.groupContentByType().containsKey(ContentType.MOVIE) &&
            this.netflixRecommender.groupContentByType().containsValue(expectedSet),
            "Grouping content is not the same as expected.");
    }

    @Test
    void testGetTopNRatedContent() {

        Map<ContentType, Set<Content>> expected = new HashMap<>();

        var expectedList = List.of(new Content("tm84618", "Taxi Driver", ContentType.MOVIE,
                "A mentally unstable Vietnam War veteran works as a night-time taxi driver in New York City.",
                1976, 114, List.of("drama", "crime"), -1, "tt0075314", 8.2, 808582.0),
            new Content("tm127384", "Monty Python and the Holy Grail", ContentType.MOVIE,
                "King Arthur; accompanied by his squire; recruits his Knights of the Round Table.",
                1975, 91, List.of("fantasy", "action", "comedy"), -1, "tt0071853", 8.2, 534486.0)
        );

        assertIterableEquals(expectedList, this.netflixRecommender.getTopNRatedContent(2),
            "Actual rated content leaderboard is not the same as the expected.");
    }

    @Test
    void testGetTopNRatedContentWithNegativeN() {
        
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.netflixRecommender.getTopNRatedContent(-1),
            "IllegalArgumentException is expected but not thrown.");
    }

    @Test
    void testGetTopNRatedContentWithZeroN() {

        Assertions.assertIterableEquals(new ArrayList<>(), this.netflixRecommender.getTopNRatedContent(0),
            "Empty list is expected but the actual is not empty");
    }

    @Test
    void testGetSimilarContent() {

        List<Content> expectedEither = List.of(new Content("tm84618", "Taxi Driver", ContentType.MOVIE,
                "A mentally unstable Vietnam War veteran works as a night-time taxi driver in New York City.",
                1976, 114, List.of("drama", "crime"), -1, "tt0075314", 8.2, 808582.0),
            new Content("tm154986", "Deliverance", ContentType.MOVIE,
                "Intent on seeing the Cahulawassee River before it's turned into one huge lake.",
                1972, 109, List.of("drama", "action", "thriller", "european"), -1, "tt0068473", 7.7, 107673.0),
           new Content("tm127384", "Monty Python and the Holy Grail", ContentType.MOVIE,
                "King Arthur; accompanied by his squire; recruits his Knights of the Round Table.",
                1975, 91, List.of("fantasy", "action", "comedy"), -1, "tt0071853", 8.2, 534486.0),
            new Content("tm120801", "The Dirty Dozen", ContentType.MOVIE,
                "12 American military prisoners in World War II are ordered to infiltrate a well-guarded enemy château and kill the Nazi officers vacationing there. The soldiers; most of whom are facing death sentences for a variety of violent crimes; agree to the mission and the possible commuting of their sentences.",
                1967, 150, List.of("war", "action"), -1, "tt0061578", 7.7, 72662.0)
        );

        List<Content> expectedOr = List.of(new Content("tm84618", "Taxi Driver", ContentType.MOVIE,
                "A mentally unstable Vietnam War veteran works as a night-time taxi driver in New York City.",
                1976, 114, List.of("drama", "crime"), -1, "tt0075314", 8.2, 808582.0),
            new Content("tm154986", "Deliverance", ContentType.MOVIE,
                "Intent on seeing the Cahulawassee River before it's turned into one huge lake.",
                1972, 109, List.of("drama", "action", "thriller", "european"), -1, "tt0068473", 7.7, 107673.0),
            new Content("tm120801", "The Dirty Dozen", ContentType.MOVIE,
                "12 American military prisoners in World War II are ordered to infiltrate a well-guarded enemy château and kill the Nazi officers vacationing there. The soldiers; most of whom are facing death sentences for a variety of violent crimes; agree to the mission and the possible commuting of their sentences.",
                1967, 150, List.of("war", "action"), -1, "tt0061578", 7.7, 72662.0),
            new Content("tm127384", "Monty Python and the Holy Grail", ContentType.MOVIE,
                "King Arthur; accompanied by his squire; recruits his Knights of the Round Table.",
                1975, 91, List.of("fantasy", "action", "comedy"), -1, "tt0071853", 8.2, 534486.0)
        );

        Content toPass = new Content("tm84618", "Taxi Driver", ContentType.MOVIE,
            "A mentaly City.",
            1976, 114, List.of("drama", "crime"), -1, "tt0075314", 8, 8085);


        assertTrue(this.netflixRecommender.getSimilarContent(toPass).equals(expectedEither) ||
            this.netflixRecommender.getSimilarContent(toPass).equals(expectedOr),
            "The actual similar content is not the same as the expected.");
    }

    @Test
    void testGetContentByKeywords() {

        var expected = List.of(new Content("tm154986", "Deliverance", ContentType.MOVIE,
                "Intent on seeing the Cahulawassee River before it's turned into one huge lake.",
                1972, 109, List.of("drama", "action", "thriller", "european"), -1, "tt0068473", 7.7, 107673.0));

            assertIterableEquals(expected, this.netflixRecommender.getContentByKeywords("RIVER"),
            "Actual content is not the same as the expected.");
    }
}
