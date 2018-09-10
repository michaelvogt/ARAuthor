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
import com.google.ar.sceneform.rendering.ViewRenderable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import eu.michaelvogt.ar.author.R;
import eu.michaelvogt.ar.author.data.Area;
import eu.michaelvogt.ar.author.data.Detail;
import eu.michaelvogt.ar.author.utils.FileUtils;
import eu.michaelvogt.ar.author.utils.Slider;
import eu.michaelvogt.ar.author.utils.ToggleSlideTextHandler;

public class SliderNode extends AreaNode {
  private static final String TAG = SliderNode.class.getSimpleName();

  private SliderNode(Context context, Area area) {
    super(context, area);
  }

  public static SliderNode builder(Context context, Area area) {
    return new SliderNode(context, area);
  }

  @SuppressLint("ClickableViewAccessibility")
  public CompletionStage<Node> build() {
    CompletableFuture<Node> future = new CompletableFuture<>();

    ViewRenderable.builder()
        .setView(context, R.layout.view_slider)
        .build()
        .thenAccept(renderable -> {
          renderable.setSizer(view -> area.getSize());
          setRenderable(renderable);
          area.applyDetail(renderable);

          Slider slider = renderable.getView().findViewById(R.id.slider);
          View sliderText = renderable.getView().findViewById(R.id.slider_text);

          String puplicFolderPath = FileUtils.getFullPuplicFolderPath(
              area.getDetailString(Detail.KEY_IMAGEFOLDERPATH));
          List<String> descriptions = (List<String>) area.getDetail(Detail.KEY_IMAGEDESCRIPTIONS);
          slider.setImages(FileUtils.getFilepathsOfFolder(puplicFolderPath), descriptions);
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
}
