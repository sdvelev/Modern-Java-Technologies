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

    private static final int BEGIN_INDEX = 2;

    private Reader stopwordsIn;
    private Reader reviewsIn;
    private Writer reviewsOut;
    private Map<String, WordCharacteristics> frequencySentimentMap;
    private Set<String> stopWordsSet;

    public Reviewer(Reader stopwordsIn, Reader reviewsIn, Writer reviewsOut) {

        this.stopwordsIn = stopwordsIn;
        this.reviewsIn = reviewsIn;
        this.reviewsOut = reviewsOut;
        this.frequencySentimentMap = new HashMap<>();
        this.stopWordsSet = new HashSet<>();
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

    private void readWordsWithAddingFrequency(String currentLine) {

        currentLine = currentLine.substring(BEGIN_INDEX);
        String[] currentWords = currentLine.split(" ");

        for (String currentWord : currentWords) {

            currentWord = currentWord.toLowerCase();

            if (currentWord.length() < 2 || !currentWord.matches("[a-zA-Z0-9']+") ||
                this.stopWordsSet.contains(currentWord)) {

                continue;
            }

            if (!this.frequencySentimentMap.containsKey(currentWord)) {

                this.frequencySentimentMap.put(currentWord, new WordCharacteristics());
            } else {

                this.frequencySentimentMap.get(currentWord).increaseFrequencyCounter();
            }

        }

    }

    private void calculateWordsSentiment() {

        try (BufferedReader bufferedReader = new BufferedReader(reviewsIn)) {

            String currentLine;

            while ((currentLine = bufferedReader.readLine()) != null) {

                this.readWordsWithAddingFrequency(currentLine);

            }

        } catch (IOException e) {

            throw new RuntimeException("There is a problem in reading from reviewsIn reader", e);
        }

    }

    public Set<String> getStopWordsSet() {

        return this.stopWordsSet;
    }

    public Map<String, WordCharacteristics> getFrequencySentimentMap() {

        return this.frequencySentimentMap;
    }

}
