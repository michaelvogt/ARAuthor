package eu.michaelvogt.ar.author;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.google.ar.core.AugmentedImageDatabase;
import com.google.ar.core.Config;
import com.google.ar.core.Frame;
import com.google.ar.core.Session;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.ux.ArFragment;


import eu.michaelvogt.ar.author.data.AuthorViewModel;
import eu.michaelvogt.ar.author.data.Marker;
import eu.michaelvogt.ar.author.utils.ImageUtils;

public class LoopArFragment extends ArFragment {
    private static  final String TAG = LoopArFragment.class.getSimpleName();

    FrameListener listener = null;

    public interface FrameListener {
        void onFrame(FrameTime frameTime, Frame frame);
    }

    @Override
    public void onUpdate(FrameTime frameTime) {
        super.onUpdate(frameTime);

        Frame arFrame = getArSceneView().getArFrame();
        if (listener != null) {
            listener.onFrame(frameTime, arFrame);
        }
    }

    @Override
    protected Config getSessionConfiguration(Session session) {
        AuthorViewModel viewModel = ViewModelProviders.of(getActivity()).get(AuthorViewModel.class);
        AugmentedImageDatabase imageDatabase = new AugmentedImageDatabase(session);

        Bitmap bitmap = null;
        for (Marker marker : viewModel.markerIterable()) {
            try {
                bitmap = ImageUtils.decodeSampledBitmapFromImagePath(
                        marker.getImagePath(), Marker.MIN_SIZE, Marker.MIN_SIZE);
                if (bitmap != null) {
                    int index = marker.getWidthInM() <= 0
                            ? imageDatabase.addImage(marker.getTitle(), bitmap)
                            : imageDatabase.addImage(marker.getTitle(), bitmap, marker.getWidthInM());
                    Log.d(TAG, "marker " + marker.getTitle() + " imported");
                } else {
                    Log.d(TAG, "marker " + marker.getTitle() + " NOT imported");
                    Snackbar.make(this.getView(), "marker " + marker.getTitle() + " NOT imported",
                            Snackbar.LENGTH_SHORT).show();

                }
            } catch (Throwable ex) {
                Log.e(TAG, "Something bad happened", ex);
                Snackbar.make(this.getView(), "Exception: " + ex.getMessage(),
                        Snackbar.LENGTH_SHORT).show();
            }
        }



        Config config = new Config(session);
        config.setAugmentedImageDatabase(imageDatabase);

        return config;
    }

    public void setOnFrameListener(FrameListener listener) { this.listener = listener; }
}