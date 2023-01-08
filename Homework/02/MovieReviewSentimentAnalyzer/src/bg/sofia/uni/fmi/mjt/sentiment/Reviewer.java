package bg.sofia.uni.fmi.mjt.sentiment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Reviewer {

    private final static int BEGIN_INDEX_AFTER_SENTIMENT = 2;
    private final static String NOT_WORD_REGEX = "[^a-zA-Z0-9']+";
    private final static String LETTERS_DIGITS_APOSTROPHE_REGEX = "[a-zA-Z0-9']+";

    private final Reader stopwordsIn;
    private final Reader reviewsIn;
    private final Writer reviewsOut;
    private final Map<String, WordCharacteristics> frequencySentimentMap;
    private Set<String> stopWordsSet;

    public Reviewer(Reader stopwordsIn, Reader reviewsIn, Writer reviewsOut) {

        this.stopwordsIn = stopwordsIn;
        this.reviewsIn = reviewsIn;
        this.reviewsOut = reviewsOut;
        this.frequencySentimentMap = new HashMap<>();
        this.stopWordsSet = new HashSet<>();

        updateReviewer();
    }

    public Set<String> getStopWordsSet() {

        return this.stopWordsSet;
    }

    public Map<String, WordCharacteristics> getFrequencySentimentMap() {

        return this.frequencySentimentMap;
    }

    public void readWordsAddingFrequency(String currentLine) {

        int currentRating = Integer.parseInt(currentLine.substring(0, 1));

        currentLine = currentLine.substring(BEGIN_INDEX_AFTER_SENTIMENT);
        String[] currentWords = currentLine.split(NOT_WORD_REGEX);
        Set<String> currentLineWordsSet = new HashSet<>();

        for (String currentWord : currentWords) {

            currentWord = currentWord.toLowerCase();

            if (isForSkipping(currentWord)) {
                continue;
            }

            increaseFrequency(currentWord);
            increaseWordReviewAndSentimentIfFirstOccurrence(currentWord, currentLineWordsSet, currentRating);

            currentLineWordsSet.add(currentWord);
        }
    }

    private void updateReviewer() {

        readStopWords();
        calculateWordsSentiment();
    }

    private void readStopWords() {

        try (BufferedReader bufferedReader = new BufferedReader(stopwordsIn)) {

            this.stopWordsSet = new HashSet<>(bufferedReader.lines().toList());
        } catch (IOException e) {

            throw new RuntimeException("There is a problem in reading from stopWordsIn reader", e);
        }
    }

    private void calculateWordsSentiment() {

        try (BufferedReader bufferedReader = new BufferedReader(reviewsIn)) {

            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null) {

                if (currentLine.isEmpty() || currentLine.isBlank()) {
                    continue;
                }

                readWordsAddingFrequency(currentLine);
            }
        } catch (IOException e) {

            throw new RuntimeException("There is a problem in reading from reviewsIn reader", e);
        }

        calculateSentimentScore();
    }

    private void calculateSentimentScore() {

        for (Map.Entry<String, WordCharacteristics> currentFrequencySentimentMap :
            this.frequencySentimentMap.entrySet()) {

            currentFrequencySentimentMap.getValue().calculateSentimentScore();
        }
    }

    private boolean isForSkipping(String word) {

        return (word.length() < 2 || !word.matches(LETTERS_DIGITS_APOSTROPHE_REGEX) ||
            this.stopWordsSet.contains(word));
    }

    private void increaseFrequency(String word) {

        if (!this.frequencySentimentMap.containsKey(word)) {

            //In newly created WordCharacteristics wordFrequencyCounter is 1
            this.frequencySentimentMap.put(word, new WordCharacteristics());
        } else {

            this.frequencySentimentMap.get(word).increaseFrequencyCounter();
        }
    }

    private void increaseWordReviewAndSentimentIfFirstOccurrence(String word, Set<String> currentLineWordsSet,
                                                                 int currentRating) {

        if (!currentLineWordsSet.contains(word)) {

            this.frequencySentimentMap.get(word).increaseWordReviewsCounter();
            this.frequencySentimentMap.get(word).increaseWordTotalSentimentCounter(currentRating);
        }
    }
}
