package Types;

import java.sql.Date;

public class Rating {
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

}