package eu.michaelvogt.ar.author;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.Constraints;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.ar.core.Anchor;
import com.google.ar.core.AugmentedImage;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.rendering.Texture;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.TransformableNode;

import java.io.File;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import androidx.navigation.Navigation;
import eu.michaelvogt.ar.author.utils.FileUtils;

public class PreviewFragment extends Fragment {
  private LoopArFragment loopArFragment;
  private Node panelNode;
  private ViewRenderable userPanelRenderable;
  private String TAG;

  private TransformableNode andy;
  private ModelRenderable andyRenderable;

  private ModelRenderable panelDisplayRenderable;

  public PreviewFragment() {/* Required empty public constructor*/}

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_preview, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    loopArFragment = (LoopArFragment)
        getChildFragmentManager().findFragmentById(R.id.ux_fragment);

    ModelRenderable.builder()
        .setSource(getContext(), R.raw.andy)
        .build()
        .thenAccept(renderable -> andyRenderable = renderable)
        .exceptionally(
            throwable -> {
              Log.e(Constraints.TAG, "Unable to load Renderable.", throwable);
              return null;
            });

    ViewRenderable.builder()
        .setView(getContext(), R.layout.user_panel)
        .build()
        .thenAccept(renderable -> userPanelRenderable = renderable);

    loopArFragment.setOnFrameListener((frameTime, frame) -> {
      Collection<AugmentedImage> updatedAugmentedImages =
          frame.getUpdatedTrackables(AugmentedImage.class);

      if (updatedAugmentedImages.size() == 1) {
        for (AugmentedImage image : updatedAugmentedImages) {
          if (image.getTrackingState() == TrackingState.TRACKING) {
            if (image.getName().equals("hidakaya_sign")) {
              Anchor anchor = image.createAnchor(image.getCenterPose());
              if (panelNode == null) {
                // Bring sign to live - animations, image noise
                makeUserPanel(anchor, image);
              }
            } else {
              Anchor anchor = image.createAnchor(image.getCenterPose());
              if (andy == null) {
                makeAndy(anchor);
              }
            }
          }
        }
      }
    });

    view.findViewById(R.id.listmarker_fab).setOnClickListener(
        Navigation.createNavigateOnClickListener(R.id.listFragment)
    );
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
  }

  @Override
  public void onDetach() {
    super.onDetach();
  }

  private void makeAndy(Anchor anchor) {
    AnchorNode anchorNode = new AnchorNode(anchor);
    anchorNode.setParent(loopArFragment.getArSceneView().getScene());

    // Create the transformable andy and add it to the anchor.
    TransformableNode andy = new TransformableNode(loopArFragment.getTransformationSystem());
    andy.setParent(anchorNode);
    andy.setRenderable(andyRenderable);
    andy.select();

    this.andy = andy;
  }

  private void makeUserPanel(Anchor anchor, AugmentedImage image) {
    AnchorNode anchorNode = new AnchorNode(anchor);
    anchorNode.setParent(loopArFragment.getArSceneView().getScene());

    Node panel = new Node();
    panel.setRenderable(userPanelRenderable);
    panel.setLocalPosition(new Vector3(image.getExtentX(), 0, image.getExtentZ()));
    panel.setLocalRotation(Quaternion.axisAngle(new Vector3(1, 0, 0), -90));
    panel.setParent(anchorNode);

    View panelView = userPanelRenderable.getView();
    TAG = "preview fragment";
    panelView.findViewById(R.id.button_video).setOnClickListener((event) -> {
      Log.d(TAG, "video pressed");

      // Show image on top of sign
    });

    panelView.findViewById(R.id.button_image).setOnClickListener((event) -> {
      Log.d(TAG, "image pressed");

      File photoFile = FileUtils.getFullPuplicFolderFile(FileUtils.HIDAKA_PATH + "hidakabreads.jpg");
      Uri photoURI = Uri.fromFile(photoFile);

      CompletableFuture<Texture> textureFuture = Texture.builder()
          .setSource(getContext(), R.drawable.hidakabreads)
          .setUsage(Texture.Usage.DATA)
          .build();

      // Show images on top of sign
      textureFuture.thenAccept( texture -> {
        MaterialFactory.makeOpaqueWithTexture(getContext(), texture)
            .thenAccept(material -> {
                ModelRenderable panelDisplayRenderable = ShapeFactory.makeCube(
                    new Vector3(image.getExtentX() - 0.02f, .01f, image.getExtentZ() - 0.025f),
                    new Vector3(0, 0, 0), material);

                Node display = new Node();
                display.setRenderable(panelDisplayRenderable);
                display.setParent(anchorNode);
            });
      });
    });

    panelNode = panel;
  }
}
