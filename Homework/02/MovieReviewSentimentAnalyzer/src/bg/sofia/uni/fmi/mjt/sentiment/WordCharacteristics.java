package bg.sofia.uni.fmi.mjt.sentiment;

public class WordCharacteristics {

    private int wordFrequencyCounter;
    private double wordSentimentScore;
    private int wordReviewsCounter;
    private double wordTotalSentimentCounter;

    public WordCharacteristics() {

        this.wordFrequencyCounter = 1;
        this.wordSentimentScore = 0;
        this.wordReviewsCounter = 0;
    }

    public void increaseFrequencyCounter() {

        this.wordFrequencyCounter++;
    }

    public void increaseWordReviewsCounter() {

        this.wordReviewsCounter++;
    }

    public void increaseWordTotalSentimentCounter(double toAdd) {

        this.wordTotalSentimentCounter += toAdd;
    }

    public int getWordFrequencyCounter() {

        return this.wordFrequencyCounter;
    }

    public double getWordSentimentScore() {

        return this.wordSentimentScore;
    }

    public void calculateSentimentScore() {

        this.wordSentimentScore = this.wordTotalSentimentCounter / this.wordReviewsCounter;
    }

    public double getWordTotalSentimentCounter() {

        return this.wordTotalSentimentCounter;
    }

    public int getWordReviewsCounter() {

        return this.wordReviewsCounter;
    }

    public void setWordSentimentScore(double wordSentimentScore) {

        this.wordSentimentScore = wordSentimentScore;
    }


}
