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
import android.util.Log;
import android.view.View;

import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.rendering.ViewRenderable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import eu.michaelvogt.ar.author.R;
import eu.michaelvogt.ar.author.data.AreaVisual;
import eu.michaelvogt.ar.author.data.VisualDetail;
import eu.michaelvogt.ar.author.data.VisualDetailKt;
import eu.michaelvogt.ar.author.fragments.support.Slider;
import eu.michaelvogt.ar.author.utils.SlideCallback;
import eu.michaelvogt.ar.author.utils.ToggleSlideTextHandler;

public class SliderNode extends AreaNode {
  private static final String TAG = SliderNode.class.getSimpleName();

  private Scene scene;

  private SliderNode(Context context, AreaVisual area) {
    super(context, area);
  }

  public static SliderNode builder(Context context, AreaVisual area) {
    return new SliderNode(context, area);
  }

  @SuppressLint("ClickableViewAccessibility")
  public CompletionStage<Node> build() {
    CompletableFuture<Node> future = new CompletableFuture<>();

    ViewRenderable.builder()
        .setView(context, R.layout.view_slider)
        .build()
        .thenAccept(renderable -> {
          renderable.setSizer(view -> areaVisual.getSize());
          setRenderable(renderable);
          areaVisual.apply(renderable);

          Slider slider = renderable.getView().findViewById(R.id.slider);
          View sliderText = renderable.getView().findViewById(R.id.slider_text);

          VisualDetail detail = areaVisual.getDetail(VisualDetailKt.KEY_SLIDES);

          slider.setSlides(detail.getSlides(), new SlideCallback(context, areaVisual, scene));
          slider.setOnTouchListener(new ToggleSlideTextHandler(context, sliderText));

          Log.i(TAG, "ImageNode successfully created");
          future.complete(this);
        })
        .exceptionally(throwable -> {
          Log.d(TAG, "Could not create slider ", throwable);
          return null;
        });

    return future;
  }

  public SliderNode setScene(Scene scene) {
    this.scene = scene;
    return this;
  }
}
