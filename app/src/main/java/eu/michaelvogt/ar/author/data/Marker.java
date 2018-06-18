package eu.michaelvogt.ar.author.data;

public class Marker {
    public static final int MIN_SIZE = 300;

    private String thumbPath;
    private String imagePath;
    private String title;
    private String location;
    private float sizeInM = -1;

    public Marker() {
        this("", "", "", .03f);
    }

    public Marker(String imagePath, String title, String location, float sizeInM) {
        this.imagePath = imagePath;
        this.title = title;
        this.location = location;
        this.sizeInM = sizeInM;
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

    public float getSizeInM() { return sizeInM; }

    public void setSizeInM(float sizeInM) { this.sizeInM = sizeInM; }
}
