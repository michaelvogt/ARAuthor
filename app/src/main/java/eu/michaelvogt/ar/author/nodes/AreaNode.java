package eu.michaelvogt.ar.author.nodes;

import android.content.Context;

import com.google.ar.sceneform.Node;

import eu.michaelvogt.ar.author.data.Area;
import eu.michaelvogt.ar.author.data.Detail;

abstract class AreaNode extends Node {
  protected Context context;
  protected final Area area;

  AreaNode(Context context, Area area) {
    this.context = context;
    this.area = area;

    setLocalPosition(area.getPosition());
    setLocalRotation(area.getRotation());
    setLocalScale(area.getScale());
    setName(area.getTitle());
  }

  public boolean isUiNode() {
    return area.getUsageType() == Area.TYPE_UI;
  }

  public boolean isContentNode() {
    return area.getUsageType() == Area.TYPE_CONTENT;
  }

  public void hide() {
    // TODO: For now, remove the hidden nodes to free up memory. Rebuild the nodes when needed again
    getParent().removeChild(this);
  }
}
