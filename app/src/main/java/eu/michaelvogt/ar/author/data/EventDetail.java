package eu.michaelvogt.ar.author.data;

import com.google.ar.sceneform.math.Vector3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventDetail {
  private static final String TAG = EventDetail.class.getSimpleName();

  private Map<Integer, Object> eventDetails;

  private EventDetail() {
    eventDetails = new HashMap<>();
  }

  public static EventDetail builder() {
    return new EventDetail();
  }

  public String getTitle() {
    return (String) eventDetails.get(Detail.KEY_TITLE);
  }

  public EventDetail setTitle(String title) {
    eventDetails.put(Detail.KEY_TITLE, title);
    return this;
  }

  public String getImageFolderPath() {
    return (String) eventDetails.get(Detail.KEY_IMAGEFOLDERPATH);
  }

  public EventDetail setImageFolderPath(String folderPath) {
    eventDetails.put(Detail.KEY_IMAGEFOLDERPATH, folderPath);
    return this;
  }

  public Vector3 getZoomInPosition() {
    return (Vector3) eventDetails.get(Detail.KEY_ZOOMINPOSITION);
  }

  public EventDetail setZoomInPosition(Vector3 zoomPosition) {
    eventDetails.put(Detail.KEY_ZOOMINPOSITION, zoomPosition);
    return this;
  }

  public Vector3 getZoomInSize() {
    return (Vector3) eventDetails.get(Detail.KEY_ZOOMINSIZE);
  }

  public EventDetail setZoomInSize(Vector3 zoomSize) {
    eventDetails.put(Detail.KEY_ZOOMINSIZE, zoomSize);
    return this;
  }

  public int getResource() {
    return (int) eventDetails.get(Detail.KEY_RESOURCE);
  }

  public EventDetail setResource(int resource) {
    eventDetails.put(Detail.KEY_RESOURCE, resource);
    return this;
  }

  public String getLanguage() {
    return (String) eventDetails.get(Detail.KEY_LANGUAGE);
  }

  public EventDetail setLanguage(String language) {
    eventDetails.put(Detail.KEY_LANGUAGE, language);
    return this;
  }

  public List<Float> getScaleValues() {
    return (List<Float>) eventDetails.get(Detail.KEY_SCALEVALUES);
  }

  public EventDetail setScaleValues(List<? extends Number> scaleValues) {
    eventDetails.put(Detail.KEY_SCALEVALUES, scaleValues);
    return this;
  }
}
