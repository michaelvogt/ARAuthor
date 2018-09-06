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

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.ar.core.Anchor;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ViewRenderable;

import java.util.Map;

import eu.michaelvogt.ar.author.ImagePreviewFragment;
import eu.michaelvogt.ar.author.R;
import eu.michaelvogt.ar.author.data.Area;
import eu.michaelvogt.ar.author.data.AuthorViewModel;
import eu.michaelvogt.ar.author.data.Event;
import eu.michaelvogt.ar.author.data.EventDetail;
import eu.michaelvogt.ar.author.utils.AreaNodeBuilder;
import eu.michaelvogt.ar.author.utils.Detail;

public class AuthorAnchorNode extends AnchorNode {
  private final Context context;
  private final ViewGroup containerView;
  private final ImagePreviewFragment.EventCallback eventCallback;

  private int currentScaleIndex = 1;

  public AuthorAnchorNode(Anchor anchor, Context context, ViewGroup containerView, ImagePreviewFragment.EventCallback eventCallback) {
    super(anchor);

    this.context = context;
    this.containerView = containerView;
    this.eventCallback = eventCallback;
  }

  @Override
  public boolean onTouchEvent(HitTestResult hitTestResult, MotionEvent motionEvent) {
    if (motionEvent.getActionMasked() == MotionEvent.ACTION_UP) {
      Node node = hitTestResult.getNode();
      if (node instanceof EventSender) {
        EventSender eventNode = (EventSender) node;

        Map<Integer, EventDetail> eventTypes = eventNode.getEventTypes();
        eventTypes.forEach((eventType, eventDetail) -> {
          switch (eventType) {
            case Event.EVENT_GRABCONTENT:
              handleGrabContent();
              break;
            case Event.EVENT_ZOOM:
              handleZoomSlides(eventDetail);
              break;
            case Event.EVENT_SCALE:
              handleScale(eventDetail);
              break;
            default:
              broadcastEvent(eventNode, eventType, eventDetail, motionEvent);
              break;
          }
        });
      }
    }
    return true;
  }

  private void handleScale(EventDetail eventDetail) {
    currentScaleIndex =
        currentScaleIndex + 1 >= eventDetail.getScaleValues().size() ? 0 : ++currentScaleIndex;

    Node background = findInHierarchy(node -> node.getName().equals("Background"));
    background.setLocalScale(Vector3.one().scaled(currentScaleIndex));
  }

  private void handleZoomSlides(EventDetail eventDetail) {
    // fade out existing content, leave ui objects visible
    // TODO: fix hardcoded nodes
    Node image = findInHierarchy(node -> node.getName().equals("Muneoka Background Image"));
    image.setLocalPosition(image.getUp().negated());

    Node intro = findInHierarchy(node -> node.getName().equals("Muneoka Background Intro"));
    intro.setLocalPosition(intro.getUp().negated());

    // create slides object
    // load images
    // add explanation texts on tap
    Area zoomedSliderArea = new Area(Area.TYPE_SLIDESONIMAGE,
        eventDetail.getTitle(),
        R.layout.view_slider,
        Detail.builder()
            .setImageFolderPath(eventDetail.getImageFolderPath()),
        eventDetail.getZoomInSize(),
        Area.COORDINATE_LOCAL,
        eventDetail.getZoomInPosition(),
        new Quaternion(new Vector3(-1.0f, 0.0f, 0.0f), 90.0f),
        Vector3.one());

    AreaNodeBuilder.builder(context, zoomedSliderArea)
        .build()
        .thenAccept(node -> {
          // attach to anchornode
          node.setParent(getChildren().get(0));
        });
  }

  private void handleGrabContent() {
    // TODO: display text in WebView instead
    // TODO: add animations
    // TODO: improve layout
    View content = getCurrentContentView();
    ViewGroup contentParent = (ViewGroup) content.getParent();

    ViewGroup grabContainer = containerView.findViewById(R.id.grab_container);
    View fab = containerView.findViewById(R.id.listmarker_fab);
    View closeButton = containerView.findViewById(R.id.grab_close);
    closeButton.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() == MotionEvent.ACTION_UP) {
          try {
            eventCallback.resume();
          } catch (CameraNotAvailableException e) {
            // TODO: get to know how this can happen and handle it appropriately
            e.printStackTrace();
          }

          grabContainer.removeView(content);
          contentParent.addView(content);

          grabContainer.setVisibility(View.GONE);
          closeButton.setVisibility(View.GONE);
          fab.setVisibility(View.VISIBLE);
        }
        return true;
      }
    });

    contentParent.removeView(content);
    grabContainer.addView(content);
    grabContainer.setVisibility(View.VISIBLE);
    closeButton.setVisibility(View.VISIBLE);
    fab.setVisibility(View.INVISIBLE);

    eventCallback.pause();
  }

  private void broadcastEvent(EventSender eventNode, int eventType, Object eventDetail, MotionEvent motionEvent) {
    callOnHierarchy(node -> {
      if (node instanceof EventHandler) {
        ((EventHandler) node).handleEvent(eventType, eventDetail, motionEvent);
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
