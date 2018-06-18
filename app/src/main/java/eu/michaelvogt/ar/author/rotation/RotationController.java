package eu.michaelvogt.ar.author.rotation;

import android.util.Log;

import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.ux.BaseTransformationController;
import com.google.ar.sceneform.ux.TransformableNode;

public class RotationController extends BaseTransformationController<RotateGesture> {
  private static final String TAG = RotationController.class.getSimpleName();
  private final Node rotateChild;

  private static final float DELTA_MULTIPLIER = -0.002f;

  public RotationController(TransformableNode transformableNode,
                            RotateGestureRecognizer gestureRecognizer, Node rotateChild) {
    super(transformableNode, gestureRecognizer);
    this.rotateChild = rotateChild;
  }

  @Override
  protected boolean canStartTransformation(RotateGesture gesture) {
    return true;
  }

  @Override
  protected void onContinueTransformation(RotateGesture gesture) {
    Quaternion localRotation = rotateChild.getLocalRotation();
    localRotation.x = 1;
    localRotation.w = localRotation.w + (gesture.getDelta().y * DELTA_MULTIPLIER);

    Log.d(TAG, "new rotation: " + localRotation.w);

    rotateChild.setLocalRotation(localRotation);
  }

  @Override
  protected void onEndTransformation(RotateGesture gesture) {}
}
