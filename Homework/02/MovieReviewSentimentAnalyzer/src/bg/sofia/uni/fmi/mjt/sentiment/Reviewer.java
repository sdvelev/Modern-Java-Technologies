package bg.sofia.uni.fmi.mjt.sentiment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Reviewer {

    private static final int BEGIN_INDEX = 2;
    private static final String BEGINNING_REGEX = "(.)*\\b";
    private static final String ENDING_REGEX = "\\b(.)*";

    private Reader stopwordsIn;
    private Reader reviewsIn;
    private Writer reviewsOut;
    private Map<String, WordCharacteristics> frequencySentimentMap;
    private Set<String> stopWordsSet;
    private StringBuilder reviewsFile;

    public Reviewer(Reader stopwordsIn, Reader reviewsIn, Writer reviewsOut) {

        this.stopwordsIn = stopwordsIn;
        this.reviewsIn = reviewsIn;
        this.reviewsOut = reviewsOut;
        this.frequencySentimentMap = new HashMap<>();
        this.stopWordsSet = new HashSet<>();
        this.reviewsFile = new StringBuilder("");

        updateReviewer();
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

    public void readWordsWithAddingFrequency(String currentLine) {

        int currentRating = Integer.parseInt(currentLine.substring(0, 1));

        currentLine = currentLine.substring(BEGIN_INDEX);
        String[] currentWords = currentLine.split(" ");
        Set<String> currentLineWordsSet = new HashSet<>();

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

            if (!currentLineWordsSet.contains(currentWord) /*&& processIfExists(currentWord, currentLine)*/) {

                this.frequencySentimentMap.get(currentWord).increaseWordReviewsCounter();
                this.frequencySentimentMap.get(currentWord).increaseWordTotalSentimentCounter(currentRating);
            }

            currentLineWordsSet.add(currentWord);
        }

    }

    private void calculateWordsSentiment() {

        try (BufferedReader bufferedReader = new BufferedReader(reviewsIn)) {

            String currentLine;

            while ((currentLine = bufferedReader.readLine()) != null) {

                this.reviewsFile.append(currentLine);
                this.reviewsFile.append(System.lineSeparator());

                readWordsWithAddingFrequency(currentLine);

            }

        } catch (IOException e) {

            throw new RuntimeException("There is a problem in reading from reviewsIn reader", e);
        }


        calculateSentimentScore();
    }

    private boolean processIfExists(String currentWord, String currentLine) {

        return currentLine.toLowerCase().matches(BEGINNING_REGEX + currentWord + ENDING_REGEX);
    }

    private void calculateSentimentScore() {

        for (Map.Entry<String, WordCharacteristics> currentFrequencySentimentMap :
            this.frequencySentimentMap.entrySet()) {

            currentFrequencySentimentMap.getValue().calculateSentimentScore();

            /*
            double totalRating = 0;
            double totalReviews = 0;

            try (BufferedReader bufferedReader = new BufferedReader(new StringReader(this.reviewsFile.toString()))) {

                String currentLine;

                while ((currentLine = bufferedReader.readLine()) != null) {

                    int currentRating = Integer.parseInt(currentLine.substring(0, 1));

                    if (processIfExists(currentFrequencySentimentMap.getKey(), currentLine.substring(BEGIN_INDEX))) {

                        totalRating += currentRating;
                        totalReviews++;
                    }
                }

                this.frequencySentimentMap.get(currentFrequencySentimentMap.getKey())
                    .setWordSentimentScore(totalRating / totalReviews);

            } catch (IOException e) {

                throw new RuntimeException("There is a problem in reading from reviewsFile StringBuilder", e);
            }*/
        }
    }

    public Set<String> getStopWordsSet() {

        return this.stopWordsSet;
    }

    public Map<String, WordCharacteristics> getFrequencySentimentMap() {

        return this.frequencySentimentMap;
    }

}
