package eu.michaelvogt.ar.author;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.Constraints;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.ar.core.Anchor;
import com.google.ar.core.AugmentedImage;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.GesturePointersUtility;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.Collection;

import androidx.navigation.Navigation;
import eu.michaelvogt.ar.author.locations.Hidakaya;
import eu.michaelvogt.ar.author.locations.Kumagaike;
import eu.michaelvogt.ar.author.rotation.RotateGestureRecognizer;

public class PreviewFragment extends Fragment {
  private LoopArFragment loopArFragment;

  private TransformableNode butterfly;
  private ModelRenderable butterflyRenderable;

  private RotateGestureRecognizer rotateRecognizer;

  private boolean hidakainfoDone;
  private boolean kumagaikeinfoDone;
  private boolean butterflyDone;


  private final Scene.OnPeekTouchListener peekTouchListener = new Scene.OnPeekTouchListener() {
    @Override
    public void onPeekTouch(HitTestResult hitTestResult, MotionEvent motionEvent) {
      loopArFragment.onPeekTouch(hitTestResult, motionEvent);
      rotateRecognizer.onTouch(hitTestResult, motionEvent);
    }
  };

  public PreviewFragment() {/* Required empty public constructor*/}

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_preview, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    loopArFragment = (LoopArFragment)
        getChildFragmentManager().findFragmentById(R.id.ux_fragment);
    loopArFragment.getArSceneView().getScene().setOnPeekTouchListener(peekTouchListener);
    loopArFragment.getPlaneDiscoveryController().hide();
    loopArFragment.getPlaneDiscoveryController().setInstructionView(null);

    rotateRecognizer = new RotateGestureRecognizer(new GesturePointersUtility(getResources().getDisplayMetrics()));

    ModelRenderable.builder()
        .setSource(getContext(), R.raw.butterfly)
        .build()
        .thenAccept(renderable -> butterflyRenderable = renderable)
        .exceptionally(throwable -> {
              Log.e(Constraints.TAG, "Unable to load butterfly Renderable.", throwable);
              return null;
            });

    loopArFragment.setOnFrameListener((frameTime, frame) -> {
      Collection<AugmentedImage> updatedAugmentedImages =
          frame.getUpdatedTrackables(AugmentedImage.class);

      if (updatedAugmentedImages.size() == 1) {
        for (AugmentedImage image : updatedAugmentedImages) {
          if (image.getTrackingState() == TrackingState.TRACKING) {
            Anchor anchor = image.createAnchor(image.getCenterPose());
            AnchorNode anchorNode = new AnchorNode(anchor);
            anchorNode.setParent(loopArFragment.getArSceneView().getScene());

            switch (image.getName()) {
              case Hidakaya.SIGN:
                if (!hidakainfoDone) {
                  hidakainfoDone = true;
                  Hidakaya.displayInfoScene(getContext(), anchorNode, image);
                }
                break;
              case Kumagaike.SIGN:
                if (!kumagaikeinfoDone) {
                  kumagaikeinfoDone = true;
                  Kumagaike.displayInfoScene(getContext(), anchorNode, image);
                }
                break;
              default:
                if (!butterflyDone) {
                  butterflyDone = true;
                  makeButterfly(anchor);
                }
                break;
            }
          }
        }
      }
    });

    view.findViewById(R.id.listmarker_fab).setOnClickListener(
        Navigation.createNavigateOnClickListener(R.id.listFragment)
    );
  }

  private void makeButterfly(Anchor anchor) {
    if (this.butterfly != null)
      return;

    AnchorNode anchorNode = new AnchorNode(anchor);
    anchorNode.setParent(loopArFragment.getArSceneView().getScene());

    // Create the transformable butterfly and add it to the anchor.
    TransformableNode andy = new TransformableNode(loopArFragment.getTransformationSystem());
    andy.setParent(anchorNode);
    andy.setRenderable(butterflyRenderable);
    andy.select();

    this.butterfly = andy;
  }
}
