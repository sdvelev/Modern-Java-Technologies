package bg.sofia.uni.fmi.mjt.sentiment;

public class WordCharacteristics {

    private int wordFrequencyCounter;
    private double wordSentimentScore;

    public WordCharacteristics() {

        this.wordFrequencyCounter = 1;
        this.wordSentimentScore = 0;
    }

    public void increaseFrequencyCounter() {

        this.wordFrequencyCounter++;
    }

    public int getWordFrequencyCounter() {

        return this.wordFrequencyCounter;
    }

    public double getWordSentimentScore() {

        return this.wordSentimentScore;
    }


}
