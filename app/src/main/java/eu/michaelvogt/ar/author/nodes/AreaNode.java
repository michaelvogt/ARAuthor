package eu.michaelvogt.ar.author.nodes;

import android.content.Context;

import com.google.ar.sceneform.Node;

import eu.michaelvogt.ar.author.data.Area;

public class AreaNode extends Node {
  public static final int RENDER_FIRST = 0;
  public static final int RENDER_LAST = 1;

  protected Context context;
  protected final Area area;

  boolean isCameraFacing;
  int renderPriority = 4;

  AreaNode(Context context, Area area) {
    this.context = context;
    this.area = area;

    setLocalPosition(area.getPosition());
    setLocalRotation(area.getRotation());
    setLocalScale(area.getScale());
    setName(area.getTitle());
  }

  public boolean isUiNode() {
    return area.getUsageType() == Area.KIND_UI;
  }

  public boolean isContentNode() {
    return area.getUsageType() == Area.KIND_CONTENT;
  }

  public boolean isCameraFacing() {
    return isCameraFacing;
  }

  public void hide() {
    // TODO: For now, remove the hidden nodes to free up memory. Rebuild the nodes when needed again
    getParent().removeChild(this);
  }
}
