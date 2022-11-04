package bg.sofia.uni.fmi.mjt.escaperoom.room;

import bg.sofia.uni.fmi.mjt.escaperoom.rating.Ratable;

import java.util.Objects;

public class EscapeRoom implements Ratable {

    final private String name;
    final private Theme theme;
    final private Difficulty difficulty;
    final private int maxTimeToEscape;
    final private double priceToPlay;
    final private int maxReviewsCount;
    private int currentReviewsCount;
    private boolean isReviewsCapacityFull;
    private double rating;
    final private Review[] reviews;


    public EscapeRoom(String name, Theme theme, Difficulty difficulty, int maxTimeToEscape, double priceToPlay,
                      int maxReviewsCount) {
        this.name = name;
        this.theme = theme;
        this.difficulty = difficulty;
        this.maxTimeToEscape = maxTimeToEscape;
        this.priceToPlay = priceToPlay;
        this.maxReviewsCount = maxReviewsCount;
        this.rating = 0;
        this.reviews = new Review[maxReviewsCount];
        this.currentReviewsCount = 0;
        this.isReviewsCapacityFull = false;
    }

    /**
     * Returns the name of the escape room.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the difficulty of the escape room.
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * Returns the maximum time to escape the room.
     */
    public int getMaxTimeToEscape() {
        return maxTimeToEscape;
    }

    /**
     * Returns all user reviews stored for this escape room, in the order they have been added.
     */
    public Review[] getReviews() {

        if (this.isReviewsCapacityFull == true) {

            Review[] fulfilled = new Review[this.maxReviewsCount];

            int start = 0;

            for (int i = this.currentReviewsCount; i < this.maxReviewsCount; i++) {
                fulfilled[start++] = this.reviews[i];
            }

            for (int i = 0; i < this.currentReviewsCount; i++) {
                fulfilled[start++] = this.reviews[i];
            }

            return fulfilled;
        }

        Review[] fulfilled = new Review[this.currentReviewsCount];
        for (int i = 0; i < this.currentReviewsCount; i++) {
            fulfilled[i] = this.reviews[i];
        }
        return fulfilled;
    }

    /**
     * Adds a user review for this escape room.
     *
     * @param review the user review to add.
     */
    public void addReview(Review review) {

        if (this.currentReviewsCount == this.maxReviewsCount) {
            this.currentReviewsCount = 0;
            this.isReviewsCapacityFull = true;
        }

        if (this.currentReviewsCount < this.maxReviewsCount) {
            this.reviews[this.currentReviewsCount] = review;
            ++this.currentReviewsCount;
            updateRating();
        }

    }

    private void updateRating() {

        double sumRating = 0;

        if (this.isReviewsCapacityFull == true) {

            for (int i = 0; i < this.maxReviewsCount; i++) {
                sumRating += this.reviews[i].rating();
            }

            this.rating = sumRating / this.maxReviewsCount;
            return;
        }

        for (int i = 0; i < this.currentReviewsCount; i++) {
            sumRating += this.reviews[i].rating();
        }

        this.rating = sumRating / this.currentReviewsCount;
    }

    /**
     * Returns the rating of a ratable object.
     *
     * @return the rating
     */
    @Override
    public double getRating() {
        return rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EscapeRoom that = (EscapeRoom) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}