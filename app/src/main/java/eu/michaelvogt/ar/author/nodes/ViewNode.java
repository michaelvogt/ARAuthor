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

import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Material;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import eu.michaelvogt.ar.author.R;
import eu.michaelvogt.ar.author.data.AreaVisual;
import eu.michaelvogt.ar.author.data.VisualDetail;
import eu.michaelvogt.ar.author.utils.AreaNodeBuilder;
import eu.michaelvogt.ar.author.utils.FileUtils;
import eu.michaelvogt.ar.author.utils.Slider;

public class ViewNode extends Node {
  private static final String TAG = ViewNode.class.getSimpleName();

  private final Context context;
  private final AreaVisual areaVisual;

  private ViewNode(Context context, AreaVisual areaVisual) {
    this.context = context;
    this.areaVisual = areaVisual;

    setLocalPosition((Vector3) areaVisual.getDetail(VisualDetail.KEY_POSITION));
    setLocalRotation((Quaternion) areaVisual.getDetail(VisualDetail.KEY_ROTATION));
    setLocalScale((Vector3) areaVisual.getDetail(VisualDetail.KEY_SCALE));
    setName(areaVisual.getTitle());
  }

  public static ViewNode builder(Context context, AreaVisual areaVisual) {
    return new ViewNode(context, areaVisual);
  }

  public CompletionStage<Node> build() {
    CompletableFuture<Node> future = new CompletableFuture<>();

    ModelRenderable.builder()
        // Keep default model as resource
        .setSource(context, AreaNodeBuilder.SLIDE_MATERIAL_TEMP)
        .build()
        .thenAccept(temp -> {
          // TODO: Hack - fix when custom material can be created #196
          Material material = temp.getMaterial();
          areaVisual.applyDetail(material);

          ViewRenderable.builder()
              .setView(context, (Integer) areaVisual.getDetail(VisualDetail.KEY_RESOURCE))
              .setSource(context, R.raw.slide)
              .build()
              .thenAccept(renderable -> {
                renderable.setSizer(view -> (Vector3) areaVisual.getDetail(VisualDetail.KEY_SIZE));
                setRenderable(renderable);



//                String encoded = null;
//                try {
//                  String content = FileUtils.readTextFile(areaVisual.getDetailString(AreaVisual.TEXTPATH));
//                  encoded = Base64.encodeToString(content.getBytes(), Base64.NO_PADDING);
//                }catch (Exception e){
//                  future.completeExceptionally(e);
//                }
//                WebView webView = renderable.getView().findViewById(R.id.view_web);
//                webView.loadData(encoded, "text/html", "base64");



//                String puplicFolderPath = FileUtils.getFullPuplicFolderPath("Touristar/iwamiginzan/muneokake/infoboard/images/PANO_20180826_115346.jpg");
//                Bitmap bitmap = BitmapFactory.decodeFile(puplicFolderPath);
//                VrPanoramaView pano = renderable.getView().findViewById(R.id.view_panorama);
//                pano.loadImageFromBitmap(bitmap, null);



                Slider slider = renderable.getView().findViewById(R.id.slider);
                String puplicFolderPath = FileUtils.getFullPuplicFolderPath((String)
                    areaVisual.getDetail(VisualDetail.KEY_IMAGEFOLDERPATH, "Touristar/default/images/"));
                // slider.setSlides(FileUtils.getFilepathsOfFolder(puplicFolderPath));
                // slider.startTimer(3000);




                Log.i(TAG, "ViewNode successfully created");
                future.complete(this);
              })
              .exceptionally(throwable -> {
                Log.d(TAG, "Could not create ViewRenderable ", throwable);
                return null;
              });
        })
        .exceptionally(throwable -> {
          Log.d(TAG, "Could not create model " + AreaNodeBuilder.SLIDE_MATERIAL_TEMP, throwable);
          return null;
        });


    return future;
  }
}
