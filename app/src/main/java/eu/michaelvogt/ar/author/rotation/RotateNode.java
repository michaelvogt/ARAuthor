package eu.michaelvogt.ar.author.rotation;

import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.ar.sceneform.ux.TransformationSystem;

public class RotateNode extends TransformableNode {
  private final RotationController rotationController;

  public RotateNode(TransformationSystem transformationSystem,
                    RotateGestureRecognizer recognizer, Node rotationChild) {
    super(transformationSystem);

    rotationController = new RotationController(this, recognizer, rotationChild);
  }

  public boolean isTransforming() {
    return super.isTransforming() || rotationController.isTransforming();
  }

  @Override
  public void onActivate() {
    super.onActivate();
    rotationController.onNodeActivated();
  }

  @Override
  public void onDeactivate() {
    super.onDeactivate();
    rotationController.onNodeDeactivated();
  }
}
