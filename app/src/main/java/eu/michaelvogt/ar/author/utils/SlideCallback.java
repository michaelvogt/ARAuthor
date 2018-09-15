package eu.michaelvogt.ar.author.utils;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.math.Vector3;

import java.util.List;

import eu.michaelvogt.ar.author.R;
import eu.michaelvogt.ar.author.data.Area;
import eu.michaelvogt.ar.author.data.AuthorViewModel;
import eu.michaelvogt.ar.author.data.Detail;
import eu.michaelvogt.ar.author.data.Slide;

public class SlideCallback implements NodeCallback {
  private static final String TAG = SlideCallback.class.getSimpleName();

  private final Context context;
  private final Area area;
  private final Scene scene;

  public SlideCallback(Context context, Area area, Scene scene) {
    this.context = context;
    this.area = area;
    this.scene = scene;
  }

  @Override
  public void createImageComparatorCover() {
    AuthorViewModel viewModel = ViewModelProviders.of((FragmentActivity) context).get(AuthorViewModel.class);

    Slide comparisionData = ((List<Slide>) area.getDetail(Detail.KEY_SLIDES)).get(3);

    Area comparisionArea = new Area(this.area);
    comparisionArea.setObjectType(Area.TYPE_COMPARATORONIMAGE);
    comparisionArea.setTitle("Compare old and new");
    comparisionArea.setResource(R.layout.view_comparison);
    comparisionArea.setPosition(Vector3.add(area.getPosition(), new Vector3(0.0f, 0.0f, -0.29f)));
    comparisionArea.setDetail(Detail.builder()
        .setImagePath(comparisionData.getContentPath())
        .setSecondaryImagePath(comparisionData.getSecondaryPaths().get(0)));

    AreaNodeBuilder.builder(context, comparisionArea)
        .build()
        .thenAccept(node -> {
          Node mainContent = scene.findInHierarchy(node1 -> node1.getName().equals(viewModel.getCurrentMainContentId()));
          Node parent = mainContent.getParent();

          mainContent.setParent(null);
          parent.addChild(node);
        })
        .exceptionally(throwable -> {
          Log.e(TAG, "Comparator Area could not be created", throwable);
          return null;
        });
  }
}
