package eu.michaelvogt.ar.author.rotation;

import android.view.MotionEvent;

import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.ux.BaseGestureRecognizer;
import com.google.ar.sceneform.ux.GesturePointersUtility;

public class RotateGestureRecognizer extends BaseGestureRecognizer<RotateGesture> {

  /**
   * Interface definition for a callbacks to be invoked when a {@link RotateGesture} starts.
   */
  public interface OnGestureStartedListener
      extends BaseGestureRecognizer.OnGestureStartedListener<RotateGesture> {

  }

  public RotateGestureRecognizer(GesturePointersUtility gesturePointersUtility) {
    super(gesturePointersUtility);
  }

  @Override
  protected void tryCreateGestures(HitTestResult hitTestResult, MotionEvent motionEvent) {
    if (motionEvent.getPointerCount() > 1) {
      return;
    }

    int actionId = motionEvent.getPointerId(motionEvent.getActionIndex());
    int action = motionEvent.getActionMasked();
    boolean touchBegan =
        action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN;

    if (!touchBegan || gesturePointersUtility.isPointerIdRetained(actionId)) {
      return;
    }

    gestures.add(new RotateGesture(gesturePointersUtility, motionEvent));
  }
}