package eu.michaelvogt.ar.author.data;

public class Marker {
    public static final int MIN_SIZE = 300;

    private String thumbPath;
    private String imagePath;
    private String title;
    private String location;
    private float widthInM = -1;

    public Marker() {
        this("", "", "", .03f);
    }

    public Marker(String imagePath, String title, String location, float widthInM) {
        this.imagePath = imagePath;
        this.title = title;
        this.location = location;
        this.widthInM = widthInM;
    }

    public String getThumbPath() { return thumbPath; }

    public String getImagePath() { return imagePath; }

    public String getTitle() { return title; }

    public String getLocation() { return location; }

    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public void setTitle(String title) { this.title = title; }

    public void setLocation(String location) { this.location = location; }

    public boolean hasImage() {
        return !imagePath.isEmpty();
    }

    public float getWidthInM() { return widthInM; }

    public void setWidthInM(float widthInM) { this.widthInM = widthInM; }
}
