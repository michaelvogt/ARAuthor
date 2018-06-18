package eu.michaelvogt.ar.author.rotation;

import android.util.Log;
import android.view.MotionEvent;

import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.ux.BaseGesture;
import com.google.ar.sceneform.ux.GesturePointersUtility;

public class RotateGesture extends BaseGesture<RotateGesture> {

  private static final String TAG = RotateGesture.class.getSimpleName();

  /**
   * Interface definition for callbacks to be invoked by a {@link RotateGesture}.
   */
  public interface OnGestureEventListener
      extends BaseGesture.OnGestureEventListener<RotateGesture> {

  }

  private static final boolean ROTATE_GESTURE_DEBUG = true;

  private final int pointerId;
  private final Vector3 startPosition;
  private final Vector3 previousPosition;

  private final Vector3 delta = new Vector3();

  private static final float SLOP_INCHES = 0.1f;

  public RotateGesture(
      GesturePointersUtility gesturePointersUtility, MotionEvent motionEvent) {
    super(gesturePointersUtility);

    pointerId = motionEvent.getPointerId(motionEvent.getActionIndex());
    startPosition = GesturePointersUtility.motionEventToPosition(motionEvent, pointerId);
    previousPosition = new Vector3(startPosition);
    debugLog("RotateGesture Created");
  }

  public Vector3 getDelta() {
    return delta;
  }

  @Override
  protected boolean canStart(HitTestResult hitTestResult, MotionEvent motionEvent) {
    if (gesturePointersUtility.isPointerIdRetained(pointerId)) {
      cancel();
      return false;
    }

    int actionId = motionEvent.getPointerId(motionEvent.getActionIndex());
    int action = motionEvent.getActionMasked();

    if (action == MotionEvent.ACTION_CANCEL) {
      cancel();
      return false;
    }

    boolean touchEnded = action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP;

    if (touchEnded && (actionId == pointerId)) {
      cancel();
      return false;
    }

    if (action != MotionEvent.ACTION_MOVE) {
      return false;
    }

    Vector3 newPosition = GesturePointersUtility.motionEventToPosition(motionEvent, pointerId);
    Vector3 deltaPosition = Vector3.subtract(newPosition, previousPosition);
    previousPosition.set(newPosition);

    // Check that both fingers are moving.
    if (Vector3.equals(deltaPosition, Vector3.zero())) {
      return false;
    }

    // Check that both fingers have moved beyond the slop threshold.
    float diff = Vector3.subtract(newPosition, startPosition).length();
    float slopPixels = gesturePointersUtility.inchesToPixels(SLOP_INCHES);
    if (diff >= slopPixels) {
      delta.set(Vector3.subtract(newPosition, previousPosition));
      return true;
    }

    return false;
  }

  @Override
  protected void onStart(HitTestResult hitTestResult, MotionEvent motionEvent) {
    debugLog("Started");
    gesturePointersUtility.retainPointerId(pointerId);
  }

  @Override
  protected boolean updateGesture(HitTestResult hitTestResult, MotionEvent motionEvent) {
    int actionId = motionEvent.getPointerId(motionEvent.getActionIndex());
    int action = motionEvent.getActionMasked();

    if (action == MotionEvent.ACTION_CANCEL) {
      cancel();
      return false;
    }

    boolean touchEnded = action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP;

    if (touchEnded && (actionId == pointerId)) {
      complete();
      return false;
    }

    if (action != MotionEvent.ACTION_MOVE) {
      return false;
    }

    Vector3 newPosition = GesturePointersUtility.motionEventToPosition(motionEvent, pointerId);

    delta.set(Vector3.subtract(newPosition, previousPosition));

    previousPosition.set(newPosition);

    debugLog("Update: " + delta);
    return true;
  }

  @Override
  protected void onCancel() {
    debugLog("Cancelled");
  }

  @Override
  protected void onFinish() {
    debugLog("Finished");
    gesturePointersUtility.releasePointerId(pointerId);
  }

  @Override
  protected RotateGesture getSelf() {
    return this;
  }

  private static void debugLog(String log) {
    if (ROTATE_GESTURE_DEBUG) {
      Log.d(TAG, "RotateGesture:[" + log + "]");
    }
  }
}