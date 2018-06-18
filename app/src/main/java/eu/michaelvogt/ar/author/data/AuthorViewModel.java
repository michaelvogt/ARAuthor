package eu.michaelvogt.ar.author.data;

import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class AuthorViewModel extends ViewModel {
    private final List<Marker> markers = new ArrayList<>();
    private Marker cropMarker = null;

    public void addMarker(Marker marker) { markers.add(marker); }

    public Marker getMarker(int index) { return markers.get(index); }

    public int getMarkerSize() { return markers.size(); }

    public Iterable<Marker> markerIterable() {
        return () -> markers.iterator();
    }

    public void setCropMarker(Marker editMarker) {
        cropMarker = editMarker;
    }

    public Marker getCropMarker() {
        return cropMarker;
    }

    public void clearCropMarker() {
        cropMarker = null;
    }
}
