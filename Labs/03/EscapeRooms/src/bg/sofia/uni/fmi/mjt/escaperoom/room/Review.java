package bg.sofia.uni.fmi.mjt.escaperoom.room;

public record Review(int rating, String reviewText) {

    public Review {
        if (rating < 0 || rating > 10){
            throw new IllegalArgumentException("Rating must be between 0 and 10");
        }

        if (reviewText == null || reviewText.length() > 200){
            throw new IllegalArgumentException("Review text must be no longer than 200 characters and not null");
        }
    }

}
