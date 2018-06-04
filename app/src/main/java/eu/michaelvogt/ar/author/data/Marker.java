package eu.michaelvogt.ar.author.data;

import java.io.Serializable;

public class Marker {
    public static final int MIN_SIZE = 300;

    private String thumbPath;
    private String imagePath;
    private String title;
    private String location;

    public Marker() {
        this("", "", "");
    }

    public Marker(String imagePath, String title, String location) {
        this.imagePath = imagePath;
        this.title = title;
        this.location = location;
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
}
