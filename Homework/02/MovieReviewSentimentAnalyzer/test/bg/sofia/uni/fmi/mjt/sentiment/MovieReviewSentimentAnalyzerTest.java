package bg.sofia.uni.fmi.mjt.sentiment;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

public class MovieReviewSentimentAnalyzerTest {

    private final static String STOPWORDS_STRING = "a\n" +
        "about" + System.lineSeparator() +
        "of" + System.lineSeparator();

    private final static String REVIEWS_STRING = "1 A series of escapades demonstrating the adage that what is good for the goose " +
        "is also good for the gander , some of which occasionally amuses but none of which amounts to much of a " +
        "story .\t" + System.lineSeparator() +
        "3 This quiet , introspective and entertaining independent is worth seeking .\t" + System.lineSeparator() +
        "1 Even fans of Ismail Merchant's work , I suspect , would have a hard time sitting " +
        "through this one .\t" + System.lineSeparator() +
        "3 A positively thrilling combination of ethnography and all the intrigue , betrayal , deceit and " +
        "murder of a Shakespearean tragedy or a juicy soap opera .\t" + System.lineSeparator() +
        "1 Aggressive self-glorification and a manipulative whitewash .\t" + System.lineSeparator() +
        "4 A comedy-drama of nearly epic proportions rooted in a sincere performance by the title character " +
        "undergoing midlife crisis .\t" + System.lineSeparator() +
        "1 Narratively , Trouble Every Day is a plodding mess .\t" + System.lineSeparator() +
        "3 The Importance of Being Earnest , so thick with wit it plays like a reading from Bartlett's " +
        "Familiar Quotations\t" + System.lineSeparator() +
        "1 But it doesn't leave you with much .\t" + System.lineSeparator() +
        "1 You could hate it for the same reason .\t" + System.lineSeparator() +
        "4 ISN'T ISN'T ISN'T ISN't isn't isn't  ISN't 11" + System.lineSeparator() +
        "4 year year year YEAR Year YeAr yeaR YEAr yEAr" + System.lineSeparator() +
        "0 envy" + System.lineSeparator();

    private MovieReviewSentimentAnalyzer movieReviewSentimentAnalyzer;

    @BeforeEach
    void setTests() {

        var stopwordsIn = new StringReader(STOPWORDS_STRING);
        var reviewsIn = new StringReader(REVIEWS_STRING);
        var reviewsOut = new StringWriter();
        reviewsOut.append(REVIEWS_STRING);

        this.movieReviewSentimentAnalyzer = new MovieReviewSentimentAnalyzer(stopwordsIn, reviewsIn, reviewsOut);
    }

    @AfterEach
    void setTestsCleaning() {

        try {

            this.movieReviewSentimentAnalyzer.getStopwordsIn().close();
            this.movieReviewSentimentAnalyzer.getReviewsIn().close();
            this.movieReviewSentimentAnalyzer.getReviewsOut().close();
        } catch(IOException e) {

            throw new RuntimeException("There is an exception in closing streams", e);
        }
    }

    @Test
    void testGetWordSentimentTestWithStopword() {

        Assertions.assertEquals(-1, this.movieReviewSentimentAnalyzer.getWordSentiment("a"), 0.0001,
            "The provided word is a stopword so -1 is expected but not returned");
    }

    @Test
    void testGetWordSentimentTestSuccessfullyInOneReview() {

        Assertions.assertEquals(1, this.movieReviewSentimentAnalyzer.getWordSentiment("leave"), 0.0001,
            "The actual word sentiment is not the same as the expected");
    }

    @Test
    void testGetWordSentimentTestSuccessfullyInOneReviewAllCapitalised() {

        Assertions.assertEquals(3, this.movieReviewSentimentAnalyzer.getWordSentiment("TRAGEDY"), 0.0001,
            "The actual word sentiment is not the same as the expected");
    }

    @Test
    void testGetWordSentimentTestSuccessfullyInFourReviewsAllCapitalised() {

        Assertions.assertEquals(2.4, this.movieReviewSentimentAnalyzer.getWordSentiment("THE"), 0.0001,
            "The actual word sentiment is not the same as the expected");
    }

    @Test
    void testGetWordSentimentTestSuccessfullyWithApostropheInOneReviewPartlyCapitalised() {

        Assertions.assertEquals(4, this.movieReviewSentimentAnalyzer.getWordSentiment("Isn'T"), 0.0001,
            "The actual word sentiment is not the same as the expected");
    }

    @Test
    void testGetWordSentimentTestOnlySymbol() {

        Assertions.assertEquals(-1, this.movieReviewSentimentAnalyzer.getWordSentiment(","),
            "The actual word sentiment is not the same as the expected");
    }

    @Test
    void testGetWordSentimentMissingWord() {

        Assertions.assertEquals(-1, this.movieReviewSentimentAnalyzer.getWordSentiment("orange"), 0.0001,
            "The actual word sentiment must be -1 since the word is missing");
    }

    @Test
    void testGetWordFrequencyWordInOneReview() {

        Assertions.assertEquals(7, this.movieReviewSentimentAnalyzer.getWordFrequency("isn't"),
            "The actual word frequency is not the same as the expected");
    }

    @Test
    void testGetWordFrequencyMissingWord() {

        Assertions.assertEquals(0, this.movieReviewSentimentAnalyzer.getWordFrequency("orange"),
            "The actual word frequency is not the same as the expected");
    }

    @Test
    void testGetReviewSentimentSuccessfullyWithTwoStopwords() {

        Assertions.assertEquals(2.3333, this.movieReviewSentimentAnalyzer.
                getReviewSentiment("A manipulative combination of ethnography"), 0.0001,
            "The actual review sentiment score is not the same as the expected");
    }

    @Test
    void testGetReviewSentimentAsNameSuccessfullyWithTwoStopwords() {

        Assertions.assertEquals("neutral", this.movieReviewSentimentAnalyzer.
                getReviewSentimentAsName("A manipulative combination of ethnography"),
            "The actual review sentiment as name is not the same as the expected");
    }

    @Test
    void testGetReviewSentimentSuccessfullyWithoutStopwords() {

        Assertions.assertEquals(1.75, this.movieReviewSentimentAnalyzer.
                getReviewSentiment("for sincere reason also"), 0.0001,
            "The actual review sentiment score is not the same as the expected");
    }

    @Test
    void testGetReviewSentimentAsNameSuccessfullyWithoutStopwords() {

        Assertions.assertEquals("neutral", this.movieReviewSentimentAnalyzer.
                getReviewSentimentAsName("for sincere reason also"),
            "The actual review sentiment score is not the same as the expected");
    }

    @Test
    void testGetReviewSentimentSuccessfullyWithoutStopwordsWithOneSymbolCharacters() {

        Assertions.assertEquals(1.75, this.movieReviewSentimentAnalyzer.
                getReviewSentiment("for sincere reason also , ? ' ."), 0.0001,
            "The actual review sentiment score is not the same as the expected");
    }

    @Test
    void testGetReviewSentimentAsNameSuccessfullyWithoutStopwordsWithOneSymbolCharacters() {

        Assertions.assertEquals("neutral", this.movieReviewSentimentAnalyzer.
                getReviewSentimentAsName("for sincere reason also , ? : 1"),
            "The actual review sentiment score is not the same as the expected");
    }

    @Test
    void testGetReviewSentimentSuccessfullyWithUnfamiliarWords() {

        Assertions.assertEquals(2.6, this.movieReviewSentimentAnalyzer.
                getReviewSentiment("amuses but rooted in reading crime novel !!"), 0.0001,
            "The actual review sentiment score is not the same as the expected");
    }

    @Test
    void testGetReviewSentimentAsNameSuccessfullyWithUnfamiliarWords() {

        Assertions.assertEquals("somewhat positive", this.movieReviewSentimentAnalyzer.
                getReviewSentimentAsName("amuses but rooted in reading crime novel !!"),
            "The actual review sentiment score is not the same as the expected");
    }

    @Test
    void testGetReviewSentimentAllUnfamiliarWords() {

        Assertions.assertEquals(-1, this.movieReviewSentimentAnalyzer.
                getReviewSentiment("$$$ happy new 2023 $$$"),
            "All words are unfamiliar but the actual sentiment score is not -1");
    }

    @Test
    void testGetReviewSentimentAsNameAllUnfamiliarWords() {

        Assertions.assertEquals("unknown", this.movieReviewSentimentAnalyzer.
                getReviewSentimentAsName("$$$ happy new 2023 $$$"),
            "All words are unfamiliar but the actual review sentiment as name is not unknown");
    }

    @Test
    void testGetMostFrequentWordsWithNegativeN() {

        Assertions.assertThrows(IllegalArgumentException.class, () ->
            this.movieReviewSentimentAnalyzer.getMostFrequentWords(-1),
            "IllegalArgumentException is expected but not thrown");
    }

    @Test
    void testGetMostFrequentWordsSuccessfully() {

        List<String> expected = List.of("year", "isn't");

        Assertions.assertTrue(expected.containsAll(this.movieReviewSentimentAnalyzer.
            getMostFrequentWords(2)) && this.movieReviewSentimentAnalyzer.getMostFrequentWords(2).
            containsAll(expected), "The actual list is not the same as the expected");
    }

    @Test
    void testGetMostFrequentWordsWithNEqualsToZero() {

        Assertions.assertTrue(this.movieReviewSentimentAnalyzer.getMostFrequentWords(0).isEmpty(),
            "The actual list must be empty as n is 0");
    }

    @Test
    void testGetMostPositiveWordsWithNegativeN() {

        Assertions.assertThrows(IllegalArgumentException.class, () ->
            this.movieReviewSentimentAnalyzer.getMostPositiveWords(-1),
            "IllegalArgumentException is expected but not thrown");
    }

    @Test
    void testGetMostPositiveWordsWithNEqualsToZero() {

       Assertions.assertTrue(this.movieReviewSentimentAnalyzer.getMostPositiveWords(0).isEmpty(),
           "Returned list is expected to be empty");
    }


    @Test
    void testGetMostPositiveWordsSuccessfully() {

        System.out.println(this.movieReviewSentimentAnalyzer.getMostPositiveWords(16));

        List<String> expected = List.of("nearly" ,"epic",
                "proportions", "rooted", "in", "sincere", "performance", "by", "title", "character", "11",
            "year", "isn't", "undergoing", "midlife", "crisis");

        Assertions.assertTrue(this.movieReviewSentimentAnalyzer.getMostPositiveWords(16).containsAll(expected) &&
            expected.containsAll(this.movieReviewSentimentAnalyzer.getMostPositiveWords(16)),
            "Actual list does not contain expected values");
    }

    @Test
    void testGetMostNegativeWordsWithNegativeN() {

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                this.movieReviewSentimentAnalyzer.getMostNegativeWords(-1),
            "IllegalArgumentException is expected but not thrown");
    }

    @Test
    void testGetMostNegativeWordsWithNEqualsToZero() {

        Assertions.assertTrue(this.movieReviewSentimentAnalyzer.getMostNegativeWords(0).isEmpty(),
            "Returned list is expected to be empty");
    }

    @Test
    void testGetMostNegativeWordsSuccessfully() {


        System.out.println(this.movieReviewSentimentAnalyzer.getMostNegativeWords(1));

        List<String> expected = List.of("envy");

        Assertions.assertIterableEquals(this.movieReviewSentimentAnalyzer.getMostNegativeWords(1), expected,
            "Actual list is not the same as expected");
    }

    @Test
    void testIsStropwordTrue() {

       Assertions.assertTrue(this.movieReviewSentimentAnalyzer.isStopWord("a"),
           "False is returned but true is expected");
    }

    @Test
    void testIsStopwordFalse() {

        Assertions.assertFalse(this.movieReviewSentimentAnalyzer.isStopWord("film"),
            "True is returned but but false is expected");
    }

    @Test
    void testGetSentimentDictionarySize() {

        this.movieReviewSentimentAnalyzer.getSentimentDictionarySize();

        Assertions.assertEquals(106, this.movieReviewSentimentAnalyzer.getSentimentDictionarySize(),
            "Sentiment Dictionary size is not the same as expected");
    }

    @Test
    void testAppendReviewWithNegativeSentiment() {

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                this.movieReviewSentimentAnalyzer.appendReview("Sample review", -1),
            "IllegalClassArgumentException is expected but not thrown");
    }

    @Test
    void testAppendReviewWithSentimentOutOfBound() {

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                this.movieReviewSentimentAnalyzer.appendReview("Sample review", 5),
            "IllegalClassArgumentException is expected but not thrown");
    }

    @Test
    void testAppendReviewWithReviewNull() {

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                this.movieReviewSentimentAnalyzer.appendReview(null, 1),
            "IllegalClassArgumentException is expected but not thrown");
    }

    @Test
    void testAppendReviewSuccessfully() {

        String reviewToAdd = "you you yOu";
        int sentimentScoreToAdd = 3;

        this.movieReviewSentimentAnalyzer.appendReview(reviewToAdd, sentimentScoreToAdd);

        Assertions.assertEquals(1.66, this.movieReviewSentimentAnalyzer.getWordSentiment("you"), 0.01,
            "Actual sentiment score of some words after appending review is not the same as expected");
    }

    @Test
    void testAppendReviewSuccessfullyWithStopWordsAndOneOrdinaryWord() {

        String reviewToAdd = "a of about about of of of a about London";
        int sentimentScoreToAdd = 4;

        int currentDictionarySize = this.movieReviewSentimentAnalyzer.getSentimentDictionarySize();

        this.movieReviewSentimentAnalyzer.appendReview(reviewToAdd, sentimentScoreToAdd);

        Assertions.assertEquals(currentDictionarySize + 1,
            this.movieReviewSentimentAnalyzer.getSentimentDictionarySize(),
            "Expected number of words in dictionary after appending review is not the same as the expected");

        Assertions.assertEquals(4, this.movieReviewSentimentAnalyzer.getWordSentiment("london"),
            "Actual word sentiment after appending review is not the same as as the expected");
    }

}
