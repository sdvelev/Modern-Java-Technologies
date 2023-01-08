package bg.sofia.uni.fmi.mjt.sentiment;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MovieReviewSentimentAnalyzer implements SentimentAnalyzer {

    private final static int NEGATIVE_RATE = 0;
    private final static String NEGATIVE_ASSESSMENT = "negative";
    private final static int SOMEWHAT_NEGATIVE_RATE = 1;
    private final static String SOMEWHAT_NEGATIVE_ASSESSMENT = "somewhat negative";
    private final static int NEUTRAL_RATE = 2;
    private final static String NEUTRAL_ASSESSMENT = "neutral";
    private final static int SOMEWHAT_POSITIVE_RATE = 3;
    private final static String SOMEWHAT_POSITIVE_ASSESSMENT = "somewhat positive";
    private final static int POSITIVE_RATE = 4;
    private final static String POSITIVE_ASSESSMENT = "positive";
    private final static String UNKNOWN_ASSESSMENT = "unknown";
    private final static String NOT_WORD_REGEX = "[^a-zA-Z0-9']+";

    private final Reader stopwordsIn;
    private final Reader reviewsIn;
    private final Writer reviewsOut;
    private Reviewer reviewer;

    public MovieReviewSentimentAnalyzer(Reader stopwordsIn, Reader reviewsIn, Writer reviewsOut) {

        this.stopwordsIn = stopwordsIn;
        this.reviewsIn = reviewsIn;
        this.reviewsOut = reviewsOut;
        this.initializeReviewer();
    }

    /**
     * @param review the text of the review
     * @return the review sentiment as a floating-point number in the interval [0.0,
     * 4.0] if known, and -1.0 if unknown.
     */
    @Override
    public double getReviewSentiment(String review) {

        double totalWordsSentiment = 0;
        int counterWords = 0;

        for (String currentWord : review.split(NOT_WORD_REGEX)) {

            if (this.getWordSentiment(currentWord) == -1) {
                continue;
            }

            counterWords++;
            totalWordsSentiment += this.getWordSentiment(currentWord);
        }

        if (counterWords == 0) {

            return -1;
        }

        return totalWordsSentiment / counterWords;
    }

    /**
     * @param review the text of the review
     * @return the review sentiment as a name: "negative", "somewhat negative",
     * "neutral", "somewhat positive", "positive" or "unknown"
     */
    @Override
    public String getReviewSentimentAsName(String review) {

        int reviewSentimentScoreRounded = (int) Math.round(this.getReviewSentiment(review));

        return switch (reviewSentimentScoreRounded) {

            case NEGATIVE_RATE -> NEGATIVE_ASSESSMENT;
            case SOMEWHAT_NEGATIVE_RATE -> SOMEWHAT_NEGATIVE_ASSESSMENT;
            case NEUTRAL_RATE -> NEUTRAL_ASSESSMENT;
            case SOMEWHAT_POSITIVE_RATE -> SOMEWHAT_POSITIVE_ASSESSMENT;
            case POSITIVE_RATE -> POSITIVE_ASSESSMENT;
            default -> UNKNOWN_ASSESSMENT;
        };
    }

    /**
     * @param word
     * @return the review sentiment of the word as a floating-point number in the
     * interval [0.0, 4.0] if known, and -1.0 if unknown
     */
    @Override
    public double getWordSentiment(String word) {

        if (!this.reviewer.getFrequencySentimentMap().containsKey(word.toLowerCase())) {

            return -1;
        }

        return this.reviewer.getFrequencySentimentMap().get(word.toLowerCase()).getWordSentimentScore();
    }

    /**
     * @param word
     * @return the number of occurrences of the word in all reviews.
     * If {@code word} is a stopword, the result is undefined.
     */
    @Override
    public int getWordFrequency(String word) {

        if (!this.reviewer.getFrequencySentimentMap().containsKey(word.toLowerCase())) {

            return 0;
        }

        return this.reviewer.getFrequencySentimentMap().get(word.toLowerCase()).getWordFrequencyCounter();
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

        return this.reviewer.getFrequencySentimentMap().entrySet().stream()
            .sorted((firstEntry, secondEntry) -> Integer.compare(secondEntry.getValue().getWordFrequencyCounter(),
                firstEntry.getValue().getWordFrequencyCounter()))
            .limit(n)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }

    /**
     * Returns a list of the n most positive words in the reviews, sorted by sentiment score in decreasing order
     *
     * @param n
     * @throws {@link IllegalArgumentException}, if n is negative
     */
    @Override
    public List<String> getMostPositiveWords(int n) {

        validateDesiredNumber(n);

        return this.reviewer.getFrequencySentimentMap().entrySet().stream()
            .sorted((firstEntry, secondEntry) -> Double.compare(secondEntry.getValue().getWordSentimentScore(),
                firstEntry.getValue().getWordSentimentScore()))
            .limit(n)
            .map(Map.Entry::getKey)
            .toList();
    }

    /**
     * Returns a list of the n most negative words in the reviews, sorted by sentiment score in ascending order
     *
     * @param n
     * @throws {@link IllegalArgumentException}, if n is negative
     */
    @Override
    public List<String> getMostNegativeWords(int n) {

        validateDesiredNumber(n);

        return this.reviewer.getFrequencySentimentMap().entrySet().stream()
            .sorted(Comparator.comparingDouble(currentEntry -> currentEntry.getValue().getWordSentimentScore()))
            .limit(n)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
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

        validateSentimentInRange(sentiment);
        validateReviewIsNullEmptyOrBlank(review);

        String entryToAdd = sentiment + " " + review;

        try (var bufferedWriter = new BufferedWriter(this.reviewsOut)) {

            bufferedWriter.write(entryToAdd + System.lineSeparator());
            bufferedWriter.flush();
            this.updateReviewerAppendReview(entryToAdd);
        } catch (IOException e) {

            return false;
        }

        return true;
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

        return this.reviewer.getStopWordsSet().contains(word.toLowerCase());
    }

    public Reader getStopwordsIn() {

        return stopwordsIn;
    }

    public Reader getReviewsIn() {

        return reviewsIn;
    }

    public Writer getReviewsOut() {

        return reviewsOut;
    }

    private void initializeReviewer() {

        this.reviewer = new Reviewer(this.stopwordsIn, this.reviewsIn, this.reviewsOut);
    }

    private void validateDesiredNumber(int n) {

        if (n < 0) {
            throw new IllegalArgumentException("Desired quantity n is negative");
        }
    }

    private void validateSentimentInRange(int sentiment) {

        if (sentiment < NEGATIVE_RATE || sentiment > POSITIVE_RATE) {
            throw new IllegalArgumentException("Given sentiment is not within the permitted range");
        }
    }

    private void validateReviewIsNullEmptyOrBlank(String review) {

        if (review == null || review.isEmpty() || review.isBlank()) {
            throw new IllegalArgumentException("Given review is null, empty or blank");
        }
    }

    public void updateReviewerAppendReview(String currentReview) {

        this.reviewer.readWordsAddingFrequency(currentReview);

        for (String currentWord : currentReview.split(NOT_WORD_REGEX)) {

            WordCharacteristics currentWordCharacteristics = this.reviewer.getFrequencySentimentMap()
                .get(currentWord.toLowerCase());

            if (currentWordCharacteristics != null) {

                currentWordCharacteristics.calculateSentimentScore();
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {

        Reader stopWordsIn = new FileReader("stopwords.txt");
        Reader reviewsIn = new FileReader("movieReviews.txt");
        Writer reviewsOut = new FileWriter("movieReviews.txt", true);

        MovieReviewSentimentAnalyzer m = new MovieReviewSentimentAnalyzer(stopWordsIn, reviewsIn, reviewsOut);

        System.out.println(m.getSentimentDictionarySize());
        System.out.println(m.getWordSentiment("rose"));
        System.out.println(m.getWordFrequency("rose"));

        System.out.println(m.getWordSentiment("sYdNeY's"));
        System.out.println(m.getWordFrequency("LanGuaGE"));
        System.out.println(m.getWordSentiment("LanGUAGE"));

        System.out.println(m.getReviewSentiment("!!&LanguAge!!! !! %s%`sAvVy  LUv QQ"));
        System.out.println(m.getReviewSentimentAsName("!!&LanguAge!!! !! %s%`sAvVy  LUv QQ "));

        System.out.println(m.getReviewSentiment("chortleS."));

    }
}
