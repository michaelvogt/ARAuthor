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
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Material;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.rendering.Texture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import eu.michaelvogt.ar.author.R;
import eu.michaelvogt.ar.author.data.Area;
import eu.michaelvogt.ar.author.utils.AreaNodeBuilder;
import eu.michaelvogt.ar.author.utils.Detail;
import eu.michaelvogt.ar.author.utils.FileUtils;

public class ImageNode extends Node {
  private static final String TAG = ImageNode.class.getSimpleName();

  private Context context;
  private final Area area;

  private ImageNode(Context context, Area area) {
    this.context = context;
    this.area = area;

    setLocalPosition(area.getPosition());
    setLocalRotation(area.getRotation());
    setLocalScale(area.getScale());
  }

  public static ImageNode builder(Context context, Area area) {
    return new ImageNode(context, area);
  }

  public CompletionStage<Node> build() {
    CompletableFuture<Node> future = new CompletableFuture<>();
    CompletableFuture<Texture> futureTexture;

    if (area.hasDetail(Detail.IMAGEPATH)) {
      String textureFilePath = FileUtils.getFullPuplicFolderPath(
          area.getDetailString(Detail.IMAGEPATH, "Touristar/default/images/"));

      futureTexture = Texture.builder()
          .setSource(BitmapFactory.decodeFile(textureFilePath))
          .setUsage(Texture.Usage.COLOR)
          .build().exceptionally(throwable -> {
            Log.d(TAG, "Could not load texture image: " +
                area.getDetail(Detail.IMAGEPATH, "Touristar/default/images/"), throwable);
            return null;
          });

    } else if (area.hasDetail(Detail.IMAGERESOURCE)) {
      int textureResource = area.getDetailResource(Detail.IMAGERESOURCE, R.drawable.ic_launcher);

      futureTexture = Texture.builder()
          .setSource(context, textureResource)
          .setUsage(Texture.Usage.COLOR)
          .build()
          .exceptionally(throwable -> {
            Log.d(TAG, "Could not load texture resource: " +
                area.getDetail(Detail.IMAGERESOURCE, R.drawable.ic_launcher), throwable);
            return null;
          });

    } else {
      throw new IllegalArgumentException("Missing Detail IMAGEPATH or IMAGERESOURCE");
    }

    futureTexture.thenAccept(texture -> ModelRenderable.builder()
        // Keep default model as resource
        .setSource(context, AreaNodeBuilder.CUSTOM_MATERIAL_TEMP)
        .build()
        .thenAccept(temp -> {
          // TODO: Hack - fix when custom material can be created #196
          Material material = temp.getMaterial();
          material.setTexture("texture", texture);
          area.applyDetail(material);

          Renderable renderable = ShapeFactory.makeCube(area.getSize(), Vector3.zero(), material);
          area.applyDetail(renderable);

          setRenderable(renderable);

          Log.i(TAG, "ImageNode successfully created");
          future.complete(this);
        })
        .exceptionally(throwable -> {
          Log.d(TAG, "Could not create model " + AreaNodeBuilder.CUSTOM_MATERIAL_TEMP, throwable);
          return null;
        }));

    return future;
  }
}
