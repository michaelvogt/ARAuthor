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

import android.content.Context;
import android.util.Log;
import android.widget.ViewSwitcher;

import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.rendering.Texture;
import com.google.ar.sceneform.rendering.ViewRenderable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import eu.michaelvogt.ar.author.R;
import eu.michaelvogt.ar.author.data.Area;

public class SliderNode extends Node {
  private static final String TAG = SliderNode.class.getSimpleName();

  private Context context;
  private final Area area;

  private boolean isFadeIn = true;

  private SliderNode(Context context, Area area) {
    this.context = context;
    this.area = area;

    setLocalPosition(area.getPosition());
    setLocalRotation(area.getRotation());
    setLocalScale(area.getScale());
    setName(area.getTitle());
  }

  public static SliderNode builder(Context context, Area area) {
    return new SliderNode(context, area);
  }

  public CompletionStage<Node> build() {
    CompletableFuture<Node> future = new CompletableFuture<>();
    CompletableFuture<Texture> futureTexture;

    ViewRenderable.builder()
        .setView(context, R.layout.view_slider)
        .build()
        .thenAccept(renderable -> {
          renderable.setSizer(view -> area.getSize());
          setRenderable(renderable);

          area.applyDetail(renderable);

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
