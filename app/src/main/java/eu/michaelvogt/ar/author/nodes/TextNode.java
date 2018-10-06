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
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ViewRenderable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import eu.michaelvogt.ar.author.R;
import eu.michaelvogt.ar.author.data.AreaVisual;
import eu.michaelvogt.ar.author.data.AuthorViewModel;
import eu.michaelvogt.ar.author.data.EventDetail;
import eu.michaelvogt.ar.author.data.VisualDetail;
import eu.michaelvogt.ar.author.utils.FileUtils;

/**
 * AnchorNode that distributes events fired from one of its children implementing EventSender
 * to all of its children implementing the Interface EventHandler.
 */
public class TextNode extends AreaNode implements EventHandler {
  private static final String TAG = TextNode.class.getSimpleName();

  private TextNode(Context context, AreaVisual areaVisual) {
    super(context, areaVisual);
  }

  public static TextNode builder(Context context, AreaVisual area) {
    return new TextNode(context, area);
  }


  public CompletionStage<Node> build() {
    CompletableFuture<Node> future = new CompletableFuture<>();

    ViewRenderable.builder()
        .setView(context, (Integer) areaVisual.getDetail(VisualDetail.KEY_RESOURCE))
        .build()
        .thenAccept(renderable -> {
          renderable.setSizer(view -> (Vector3) areaVisual.getDetail(VisualDetail.KEY_SIZE));
          renderable.setShadowCaster(false);
          setRenderable(renderable);

          AuthorViewModel viewModel = ViewModelProviders.of(
              (FragmentActivity) context).get(AuthorViewModel.class);
          displayText(renderable, viewModel.getCurrentLanguage());

          Log.i(TAG, "TextNode successfully created");
          future.complete(this);
        })
        .exceptionally(throwable -> {
          Log.e(TAG, "Could not create TextRenderable", throwable);
          return null;
        });

    Log.i(TAG, "TextNode successfully created");
    return future;
  }

  private void displayText(ViewRenderable renderable, String language) {
    TextView textView = renderable.getView().findViewById(R.id.view_text);
    areaVisual.applyDetail(textView);

    if (areaVisual.hasDetail(VisualDetail.KEY_TEXTPATH)) {
      String content;
      String filePath = (String) areaVisual.getDetail(VisualDetail.KEY_TEXTPATH);

      try {
        content = FileUtils.readTextFile(filePath, language);
      } catch (Exception exception) {
        throw new RuntimeException("Couldn't load textfile " + filePath, exception);
      }

      textView.setText(Html.fromHtml(content, Html.FROM_HTML_SEPARATOR_LINE_BREAK_HEADING));
    } else if (areaVisual.hasDetail(VisualDetail.KEY_TEXTCONTENT)) {
      // TODO: make translatable by providing a map with translations
      textView.setText((String) areaVisual.getDetail(VisualDetail.KEY_TEXTCONTENT));
    } else if (areaVisual.hasDetail(VisualDetail.KEY_MARKUPCONTENT)) {
//          String encoded = null;
//          try {
//            String content = FileUtils.readTextFile(area.getDetailString(AreaVisual.TEXTPATH));
//            encoded = Base64.encodeToString(content.getBytes(), Base64.NO_PADDING);
//          }catch (Exception e){
//            future.completeExceptionally(e);
//          }
//          WebView webView = renderable.getView().findViewById(R.id.view_web);
//          webView.loadData(encoded, "text/html", "base64");
      Log.i(TAG, "");
    }

  }

  @Override
  public void handleEvent(EventDetail eventDetail, MotionEvent motionEvent) {
    if (eventDetail.getType() == EventDetail.EVENT_SWITCHLANGUAGE) {
      displayText((ViewRenderable) getRenderable(), (String) eventDetail.getValue());
    }
  }
}
