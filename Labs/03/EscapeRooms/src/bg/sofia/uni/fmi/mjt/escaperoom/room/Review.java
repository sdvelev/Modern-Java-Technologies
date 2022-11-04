package bg.sofia.uni.fmi.mjt.escaperoom.room;

public record Review(int rating, String reviewText) {
    private static final int MAX_RATING = 10;
    private static final int MAX_REVIEW_LENGTH = 200;
    public Review {
        if (rating < 0 || rating > MAX_RATING) {
            throw new IllegalArgumentException("Rating must be between 0 and 10");
        }

        if (reviewText == null || reviewText.length() > MAX_REVIEW_LENGTH) {
            throw new IllegalArgumentException("Review text must be no longer than 200 characters and not null");
        }
    }

}
