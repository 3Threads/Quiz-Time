package Types;

import java.util.Date;

public class Announcement {
    private int id;
    private String title;
    private String body;
    private int writerId;
    private Date writeTime;

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