package bg.sofia.uni.fmi.mjt.sentiment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MovieReviewSentimentAnalyzer implements SentimentAnalyzer {

    private Reader stopwordsIn;
    private Reader reviewsIn;
    private Writer reviewsOut;
    private Reviewer reviewer;

    public MovieReviewSentimentAnalyzer(Reader stopwordsIn, Reader reviewsIn, Writer reviewsOut) {

        this.stopwordsIn = stopwordsIn;
        this.reviewsIn = reviewsIn;
        this.reviewsOut = reviewsOut;
        this.update();
    }

    private void update() {

        this.reviewer = new Reviewer(this.stopwordsIn, this.reviewsIn, this.reviewsOut);
    }

    /**
     * @param review the text of the review
     * @return the review sentiment as a floating-point number in the interval [0.0,
     * 4.0] if known, and -1.0 if unknown.
     */
    @Override
    public double getReviewSentiment(String review) {
        return 0;
    }

    /**
     * @param review the text of the review
     * @return the review sentiment as a name: "negative", "somewhat negative",
     * "neutral", "somewhat positive", "positive" or "unknown"
     */
    @Override
    public String getReviewSentimentAsName(String review) {
        return null;
    }

    /**
     * @param word
     * @return the review sentiment of the word as a floating-point number in the
     * interval [0.0, 4.0] if known, and -1.0 if unknown
     */
    @Override
    public double getWordSentiment(String word) {

        if (!this.reviewer.getFrequencySentimentMap().containsKey(word)) {

            return -1;
        }

        return this.reviewer.getFrequencySentimentMap().get(word).getWordSentimentScore();
    }

    /**
     * @param word
     * @return the number of occurrences of the word in all reviews.
     * If {@code word} is a stopword, the result is undefined.
     */
    @Override
    public int getWordFrequency(String word) {

        if (!this.reviewer.getFrequencySentimentMap().containsKey(word)) {

            return 0;
        }

        return this.reviewer.getFrequencySentimentMap().get(word).getWordFrequencyCounter();
    }

    /**
     * Returns a list of the n most frequent words found in the reviews, sorted by frequency in decreasing order.
     * Stopwords are ignored and should not be included in the result.
     *
     * @param n
     * @throws {@link IllegalArgumentException}, if n is negative
     */
    @Override
    public List<String> getMostFrequentWords(int n) {

        validateDesiredNumber(n);

        if (n == 0) {

            return new ArrayList<>();
        }

        return this.reviewer.getFrequencySentimentMap().entrySet().stream()
            .sorted((firstEntry, secondEntry) -> Integer.compare(secondEntry.getValue().getWordFrequencyCounter(),
                firstEntry.getValue().getWordFrequencyCounter()))
            .limit(n)
            .map(Map.Entry::getKey)
            .toList();
    }

    private void validateDesiredNumber(int n) {

        if (n < 0) {

            throw new IllegalArgumentException("Desired quantity n is negative");
        }
    }

    /**
     * Returns a list of the n most positive words in the reviews, sorted by sentiment score in decreasing order
     *
     * @param n
     * @throws {@link IllegalArgumentException}, if n is negative
     */
    @Override
    public List<String> getMostPositiveWords(int n) {
        return null;
    }

    /**
     * Returns a list of the n most negative words in the reviews, sorted by sentiment score in ascending order
     *
     * @param n
     * @throws {@link IllegalArgumentException}, if n is negative
     */
    @Override
    public List<String> getMostNegativeWords(int n) {
        return null;
    }

    /**
     * Appends a review to the end of the data set.
     * Any information from the data set stored in memory should be automatically updated.
     *
     * @param review    The text part of the review
     * @param sentiment the given rating
     * @return true if the operation was successful and false if an issue has occurred and the review is not stored
     * @throws {@link IllegalArgumentException}, if review is null, empty or blank,
     *                or if the sentiment is not in the [0.0, 4.0] range
     */
    @Override
    public boolean appendReview(String review, int sentiment) {
        return false;
    }

    /**
     * Returns the total number of words with known sentiment score
     */
    @Override
    public int getSentimentDictionarySize() {

        return this.reviewer.getFrequencySentimentMap().size();
    }

    /**
     * Returns whether a word is a stopword
     *
     * @param word
     */
    @Override
    public boolean isStopWord(String word) {

        return this.reviewer.getStopWordsSet().contains(word);
    }

    public static void main(String[] args) throws FileNotFoundException {

        Reader stopWordsIn = new FileReader("stopwords.txt");
        Reader reviewsIn = new FileReader("movieReviews.txt");

        MovieReviewSentimentAnalyzer m = new MovieReviewSentimentAnalyzer(stopWordsIn, reviewsIn, null);

        System.out.println(m.getMostFrequentWords(6));
        System.out.println(m.getSentimentDictionarySize());
    }

}
