/*
    ARTester - AR for tourists by tourists
    Copyright (C) 2018  Michael Vogt

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package eu.michaelvogt.ar.author.nodes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.ar.core.Anchor;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ViewRenderable;

import java.util.List;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import eu.michaelvogt.ar.author.R;
import eu.michaelvogt.ar.author.data.AreaVisualKt;
import eu.michaelvogt.ar.author.data.AuthorViewModel;
import eu.michaelvogt.ar.author.data.EventDetail;
import eu.michaelvogt.ar.author.data.EventDetailKt;
import eu.michaelvogt.ar.author.data.Slide;
import eu.michaelvogt.ar.author.fragments.ImagePreviewFragment;
import eu.michaelvogt.ar.author.fragments.adapters.Slider;
import eu.michaelvogt.ar.author.utils.ToggleSlideTextHandler;

public class AuthorAnchorNode extends AnchorNode {
  private static final String TAG = AuthorAnchorNode.class.getSimpleName();

  private final Context context;
  private final ViewGroup containerView;
  private ViewGroup grabContainer;
  private final ImagePreviewFragment.EventCallback eventCallback;

  private int currentScaleIndex = 1;

  public AuthorAnchorNode(Anchor anchor, Context context, ViewGroup containerView, ImagePreviewFragment.EventCallback eventCallback) {
    super(anchor);

    this.context = context;
    this.containerView = containerView;
    this.eventCallback = eventCallback;
  }

  public void changeGrabbedOrientation(int configOrientation) {
    if (grabContainer != null && grabContainer.findViewById(R.id.slider) != null) {
      setupSlider(grabContainer, configOrientation == Configuration.ORIENTATION_LANDSCAPE
          ? R.layout.view_slider_grab_landscape : R.layout.view_slider_grab_portrait);
    }
  }

  @Override
  public boolean onTouchEvent(HitTestResult hitTestResult, MotionEvent motionEvent) {
    if (hitTestResult.getNode() == null) {
      return false;
    }

    Log.i(TAG, "Touch Event catched from " + hitTestResult.getNode().getName());

    if (motionEvent.getActionMasked() == MotionEvent.ACTION_UP) {
      Node node = hitTestResult.getNode();
      if (node instanceof EventSender) {
        EventSender eventNode = (EventSender) node;

        SparseArray<EventDetail> eventDetails = eventNode.getEventDetails();
        for (int index = 0; index < eventDetails.size(); index++) {
          EventDetail eventDetail = eventDetails.valueAt(index);
          switch (eventDetail.getType()) {
            case EventDetailKt.EVENT_HIDECONTENT:
              handleHideContent();
              break;
            case EventDetailKt.EVENT_GRABCONTENT:
              handleGrabContent();
              break;
            case EventDetailKt.EVENT_SETMAINCONTENT:
              handleSetMainContent(eventDetail);
              break;
            case EventDetailKt.EVENT_ZOOM:
              handleZoom(eventDetail);
              break;
            case EventDetailKt.EVENT_SCALE:
              handleScale(eventDetail);
              break;
            default:
              broadcastEvent(eventDetail, motionEvent);
              break;
          }
        }
      }
    }
    return true;
  }

  private void handleSetMainContent(EventDetail eventDetail) {
    AuthorViewModel viewModel = ViewModelProviders.of((FragmentActivity) context).get(AuthorViewModel.class);
    viewModel.setCurrentMainContentId((String) eventDetail.getAnyValue());
  }

  private void handleHideContent() {
    callOnHierarchy(node -> {
      if (node instanceof AreaNode && ((AreaNode) node).isContentNode()) {
        ((AreaNode) node).hide();
      }
    });
  }

  private void handleScale(EventDetail eventDetail) {
    currentScaleIndex =
        currentScaleIndex + 1 >= ((List<Float>) eventDetail.getAnyValue()).size() ? 0 : ++currentScaleIndex;

    Node background = findInHierarchy(node -> node.getName().equals(AreaVisualKt.BACKGROUNDAREATITLE));
    if (background != null) {
      Float scale = ((List<Float>) eventDetail.getAnyValue()).get(currentScaleIndex);
      background.setLocalScale(new Vector3(scale, 1f, scale));
    } else {
      // TODO: Handle scale for non background scenes
    }
  }

  private void handleZoom(EventDetail eventDetail) {
    AuthorViewModel viewModel = ViewModelProviders.of((FragmentActivity) context).get(AuthorViewModel.class);
    long areaId = (long) eventDetail.getAnyValue();
    viewModel.getAreaVisual(areaId)
        .thenAccept(areaVisual -> {
          Node background = findInHierarchy(node -> node.getName().equals(AreaVisualKt.BACKGROUNDAREATITLE));

          AreaNodeBuilder.builder(context, areaVisual)
              .setScene(getScene())
              .build()
              .thenAccept(node -> node.setParent(background))
              .exceptionally(throwable -> {
                Log.e(TAG, "Unable to zoom area.", throwable);
                return null;
              });
        })
        .exceptionally(throwable -> {
          Log.e(TAG, "Unable to fetch area visual " + areaId, throwable);
          return null;
        });
  }

  @SuppressLint("ClickableViewAccessibility")
  private void handleGrabContent() {
    // TODO: display text in WebView instead
    // TODO: add animations
    // TODO: improve layout
    View content = getCurrentContentView();
    ViewGroup contentParent = (ViewGroup) content.getParent();

    grabContainer = containerView.findViewById(R.id.grab_container);
    View fab = containerView.findViewById(R.id.listmarker_fab);

    View closeButton = containerView.findViewById(R.id.grab_close);
    closeButton.setOnTouchListener((view, motionEvent) -> {
      if (motionEvent.getActionMasked() == MotionEvent.ACTION_UP) {
        try {
          ((FragmentActivity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
          eventCallback.resume();
        } catch (CameraNotAvailableException e) {
          // TODO: get to know how this can happen and handle it appropriately
          e.printStackTrace();
        }

        if (content.findViewById(R.id.slider) == null) {
          grabContainer.removeView(content);
          contentParent.addView(content);
        }

        grabContainer.setVisibility(View.GONE);
        grabContainer = null;

        closeButton.setVisibility(View.GONE);
        fab.setVisibility(View.VISIBLE);
      }
      return true;
    });

    if (content.findViewById(R.id.slider) != null) {
      // Uses separate layout for the grabbed view of slider (had trouble to reuse the view from the node)
      setupSlider(content, R.layout.view_slider_grab_portrait);
    } else {
      contentParent.removeView(content);
      grabContainer.addView(content);
    }

    grabContainer.setVisibility(View.VISIBLE);
    closeButton.setVisibility(View.VISIBLE);
    fab.setVisibility(View.INVISIBLE);

    eventCallback.pause();
  }

  @SuppressLint("ClickableViewAccessibility")
  private void setupSlider(View content, int sliderResource) {
    Slider slider = content.findViewById(R.id.slider);
    List<Slide> slides = slider.getSlides();

    grabContainer.removeAllViews();
    View grabView = LayoutInflater.from(context).inflate(sliderResource, grabContainer);
    Slider grabSlider = grabView.findViewById(R.id.slider);
    View sliderText = grabView.findViewById(R.id.slider_text);

    grabSlider.setSlides(slides, null);
    grabSlider.setOnTouchListener(new ToggleSlideTextHandler(context, sliderText));

    ((FragmentActivity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
  }

  private void broadcastEvent(EventDetail eventDetail, MotionEvent motionEvent) {
    callOnHierarchy(node -> {
      if (node instanceof EventHandler) {
        ((EventHandler) node).handleEvent(eventDetail, motionEvent);
      }
    });
  }

  private View getCurrentContentView() {
    AuthorViewModel viewModel = ViewModelProviders.of((FragmentActivity) context).get(AuthorViewModel.class);
    String currentMainContent = viewModel.getCurrentMainContentId();

    Node result = findInHierarchy(node -> node.getName().equals(currentMainContent));
    return result == null ? null : ((ViewRenderable) result.getRenderable()).getView();
  }
}
