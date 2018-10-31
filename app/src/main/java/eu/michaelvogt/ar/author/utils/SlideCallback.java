package eu.michaelvogt.ar.author.utils;

import android.content.Context;
import android.util.Log;

import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;

import java.util.List;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import eu.michaelvogt.ar.author.data.AreaVisual;
import eu.michaelvogt.ar.author.data.AuthorViewModel;
import eu.michaelvogt.ar.author.data.Slide;
import eu.michaelvogt.ar.author.data.VisualDetailKt;

public class SlideCallback implements NodeCallback {
  private static final String TAG = SlideCallback.class.getSimpleName();

  private final Context context;
  private final AreaVisual areaVisual;
  private final Scene scene;

  public SlideCallback(Context context, AreaVisual areaVisual, Scene scene) {
    this.context = context;
    this.areaVisual = areaVisual;
    this.scene = scene;
  }

  @Override
  public void createImageComparatorCover() {
    AuthorViewModel viewModel = ViewModelProviders.of((FragmentActivity) context).get(AuthorViewModel.class);

    Slide comparisionData = ((List<Slide>) areaVisual.getDetailValue(VisualDetailKt.KEY_SLIDES)).get(3);

    AreaVisual comparisionArea = new AreaVisual(this.areaVisual);
    // TODO: Set details
//    comparisionArea.setObjectType(AreaVisualKt.TYPE_COMPARATORONIMAGE);
//    comparisionArea.setTitle("Compare old and new");
//    comparisionArea.setResource(R.layout.view_comparison);
//    comparisionArea.setPosition(Vector3.add(areaVisual.getPosition(), new Vector3(0.0f, 0.0f, -0.29f)));
//    comparisionArea.setVisual(AreaVisual.builder(-1)
//        .setImagePath(comparisionData.getContentPath())
//        .setSecondaryImagePath(comparisionData.getSecondaryPaths().get(0)));

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
