import java.io.InputStream;
import java.io.Serializable; 

public class QiuShi {
    private int id;
    private String author, content, createdAt;
    private InputStream header, thumb;

    public QiuShi() {}

    public QiuShi(int id, String author, InputStream header, String content, InputStream thumb, String createdAt) {
        this.id = id;
        this.author = author;
        this.header = header;
        this.content = content;
        this.thumb = thumb;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public InputStream getHeader() {
        return header;
    }

    public String getContent() {
        return content;
    }

    public InputStream getThumb() {
        return thumb;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setHeader(InputStream header) {
        this.header = header;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setThumb(InputStream thumb) {
        this.thumb = thumb;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}