package eu.michaelvogt.ar.author.nodes;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.rendering.ViewRenderable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import eu.michaelvogt.ar.author.R;
import eu.michaelvogt.ar.author.data.Area;
import eu.michaelvogt.ar.author.data.AuthorViewModel;
import eu.michaelvogt.ar.author.utils.Detail;
import eu.michaelvogt.ar.author.utils.Event;
import eu.michaelvogt.ar.author.utils.FileUtils;

public class TextNode extends Node {
  private static final String TAG = TextNode.class.getSimpleName();

  private Context context;
  private AuthorViewModel viewModel;
  private Area area;

  private TextNode(Context context, AuthorViewModel viewModel, Area area) {
    this.context = context;
    this.viewModel = viewModel;
    this.area = area;

    setLocalPosition(area.getPosition());
    setLocalRotation(area.getRotation());
    setLocalScale(area.getScale());
  }

  public static TextNode builder(Context context, AuthorViewModel viewModel, Area area) {
    return new TextNode(context, viewModel, area);
  }


  public CompletionStage<Node> build() {
    CompletableFuture<Node> future = new CompletableFuture<>();

    ViewRenderable.builder()
        .setView(context, area.getResource())
        .build()
        .thenAccept(renderable -> {
          renderable.setSizer(view -> area.getSize());
          renderable.setShadowCaster(false);
          setRenderable(renderable);

          TextView textView = renderable.getView().findViewById(R.id.view_text);
          area.applyDetail(textView);

          if (area.hasDetail(Detail.TEXTPATH)) {
            String content = null;
            try {
              content = FileUtils.readTextFile(area.getDetailString(Detail.TEXTPATH, "Touristar/default/images/"));
            } catch (Exception e) {
              future.completeExceptionally(e);
            }

            textView.setText(Html.fromHtml(content, Html.FROM_HTML_SEPARATOR_LINE_BREAK_HEADING));
          } else if (area.hasDetail(Detail.TEXTCONTENT)) {
            textView.setText(area.getDetailString(Detail.TEXTCONTENT, "Touristar/default/text/default.txt"));
          } else if (area.hasDetail(Detail.MARKUPCONTENT)) {
//          String encoded = null;
//          try {
//            String content = FileUtils.readTextFile(area.getDetailString(Detail.TEXTPATH));
//            encoded = Base64.encodeToString(content.getBytes(), Base64.NO_PADDING);
//          }catch (Exception e){
//            future.completeExceptionally(e);
//          }
//          WebView webView = renderable.getView().findViewById(R.id.view_web);
//          webView.loadData(encoded, "text/html", "base64");
          }

          viewModel.addEvent(new Event(Event.SWITCHLANGUAGE, "en"));


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
}
