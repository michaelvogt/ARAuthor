package eu.michaelvogt.ar.author.nodes;

import android.content.Context;

import com.google.ar.sceneform.Node;

import eu.michaelvogt.ar.author.data.AreaVisual;
import eu.michaelvogt.ar.author.data.AreaVisualKt;

public class AreaNode extends Node {
  public static final int RENDER_FIRST = 0;
  public static final int RENDER_LAST = 7;

  protected Context context;
  protected final AreaVisual areaVisual;

  boolean isCameraFacing;
  int renderPriority = 4;

  AreaNode(Context context, AreaVisual areaVisual) {
    this.context = context;
    this.areaVisual = areaVisual;

    setName(areaVisual.getTitle());
    setLocalPosition(areaVisual.getPosition());
    setLocalRotation(areaVisual.getRotation());
    setLocalScale(areaVisual.getScale());
  }

  public boolean isUiNode() {
    return areaVisual.getUsageType() == AreaVisualKt.KIND_UI;
  }

  public boolean isContentNode() {
    return areaVisual.getUsageType() == AreaVisualKt.KIND_CONTENT;
  }

  public boolean isCameraFacing() {
    return isCameraFacing;
  }

  public void hide() {
    // TODO: For now, remove the hidden nodes to free up memory. Rebuild the nodes when needed again
    getParent().removeChild(this);
  }
}
