package eu.michaelvogt.ar.author.nodes;

import android.content.Context;

import com.google.ar.sceneform.rendering.ViewRenderable;

import java.util.concurrent.CompletableFuture;

import eu.michaelvogt.ar.author.R;

public class InfoPanel {
  public CompletableFuture<ViewRenderable> build(Context context) {
    return ViewRenderable.builder()
        .setView(context, R.layout.panel_interactive)
        .build();
  }
}
