package Types;

import java.sql.Date;
import java.util.Comparator;

public class Rating implements Comparable<Rating> {
    private final int userId;
    private final int rating;
    private final String comment;
    private final Date ratingsDate;

    public Rating(int userId, int rating, String comment, Date ratingsDate) {
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
        this.ratingsDate = ratingsDate;
    }

    public int getRating() {
        return rating;
    }

    public int getUserId() {
        return userId;
    }

    public String getComment() {
        return comment;
    }

    public Date getRatingsDate() {
        return ratingsDate;
    }

    @Override
    public int compareTo(Rating other) {
        return this.ratingsDate.compareTo(other.ratingsDate);
    }

    // If you also want a separate Comparator, you can create it like this:
    public static final Comparator<Rating> DateComparator = new Comparator<Rating>() {
        @Override
        public int compare(Rating r1, Rating r2) {
            return r1.getRatingsDate().compareTo(r2.getRatingsDate());
        }
    };
}