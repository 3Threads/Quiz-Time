package Types;

import java.util.Date;

public class Announcement {
    private final int id;
    private final String title;
    private final String body;
    private final int writerId;
    private final Date writeTime;

    public Announcement(int id, String title, String body, int writerId, Date writeTime) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.writerId = writerId;
        this.writeTime = writeTime;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public int getWriterId() {
        return writerId;
    }

    public Date getWriteTime() {
        return writeTime;
    }
}