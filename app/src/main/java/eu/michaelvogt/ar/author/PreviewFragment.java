package eu.michaelvogt.ar.author;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.ar.core.Anchor;
import com.google.ar.core.AugmentedImage;
import com.google.ar.core.Plane;
import com.google.ar.core.Session;
import com.google.ar.core.TrackingState;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.core.exceptions.UnavailableException;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.Collection;

import androidx.navigation.Navigation;
import eu.michaelvogt.ar.author.utils.DemoUtils;

import static android.support.constraint.Constraints.TAG;

public class PreviewFragment extends Fragment {
    private TransformableNode andy;
    private ArSceneView arSceneView;
    private ModelRenderable andyRenderable;
    private boolean installRequested;
    private Snackbar loadingMessageSnackbar = null;
    private LoopArFragment loopArFragment;
    private Node userNode;
    private ViewRenderable userPanelRenderable;

    public PreviewFragment() {/* Required empty public constructor*/}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_preview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        loopArFragment = (LoopArFragment)
                getChildFragmentManager().findFragmentById(R.id.ux_fragment);
        arSceneView = loopArFragment.getArSceneView();

        ModelRenderable.builder()
                .setSource(getContext(), R.raw.andy)
                .build()
                .thenAccept(renderable -> andyRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Log.e(TAG, "Unable to load Renderable.", throwable);
                            return null;
                        });

        ViewRenderable.builder()
                .setView(getContext(), R.layout.user_panel)
                .build()
                .thenAccept(renderable -> userPanelRenderable = renderable);

        loopArFragment.setOnFrameListener((frameTime, frame) -> {
            Collection<AugmentedImage> updatedAugmentedImages =
                    frame.getUpdatedTrackables(AugmentedImage.class);

            if (updatedAugmentedImages.size() == 1) {
                for (AugmentedImage image : updatedAugmentedImages) {
                    if (image.getTrackingState() == TrackingState.TRACKING) {
                        if (image.getName().equals("hidakaya_sign")) {
                            // show user panel
                            Anchor anchor = image.createAnchor(image.getCenterPose());
                            if (userNode == null) {
                                makeUserPanel(anchor, image);
                            }
                        } else {
                            Anchor anchor = image.createAnchor(image.getCenterPose());
                            if (andy == null) {
                                makeAndy(anchor);
                            }
                        }
                    }
                }
            }

            if (loadingMessageSnackbar == null) {
                return;
            }

            if (frame.getCamera().getTrackingState() != TrackingState.TRACKING) {
                return;
            }

            for (Plane plane : frame.getUpdatedTrackables(Plane.class)) {
                if (plane.getTrackingState() == TrackingState.TRACKING) {
                    hideLoadingMessage();
                }
            }
        });

        view.findViewById(R.id.listmarker_fab).setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.listFragment)
        );
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (arSceneView.getSession() == null) {
            // If the session wasn't created yet, don't resume rendering.
            // This can happen if ARCore needs to be updated or permissions are not granted yet.
            try {
                Session session = DemoUtils.createArSession(getActivity(), installRequested);
                if (session == null) {
                    installRequested = DemoUtils.hasCameraPermission(getActivity());
                    return;
                } else {
                    arSceneView.setupSession(session);
                }
            } catch (UnavailableException e) {
                DemoUtils.handleSessionException(getActivity(), e);
            }
        }

        try {
            arSceneView.resume();
        } catch (CameraNotAvailableException ex) {
            DemoUtils.displayError(getContext(), "Unable to get camera", ex);
            getActivity().finish();
            return;
        }

        if (arSceneView.getSession() != null) {
            showLoadingMessage();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void makeAndy(Anchor anchor) {
        AnchorNode anchorNode = new AnchorNode(anchor);
        anchorNode.setParent(loopArFragment.getArSceneView().getScene());

        // Create the transformable andy and add it to the anchor.
        TransformableNode andy = new TransformableNode(loopArFragment.getTransformationSystem());
        andy.setParent(anchorNode);
        andy.setRenderable(andyRenderable);
        andy.select();

        this.andy = andy;
    }

    private void makeUserPanel(Anchor anchor, AugmentedImage image) {
        AnchorNode anchorNode = new AnchorNode(anchor);
        anchorNode.setParent(loopArFragment.getArSceneView().getScene());

        Node panel = new Node();
        panel.setRenderable(userPanelRenderable);
        panel.setLocalPosition(new Vector3(image.getExtentX(), 0, image.getExtentZ()));
        panel.setLocalRotation(Quaternion.axisAngle(new Vector3(1, 0, 0), -90));
        panel.setParent(anchorNode);

        View panelView = userPanelRenderable.getView();
        panelView.findViewById(R.id.button_video).setOnTouchListener((v, event) -> {
            Snackbar.make(
                    getActivity().findViewById(android.R.id.content),
                    "Display video",
                    Snackbar.LENGTH_SHORT);
            return true;
        });

        panelView.findViewById(R.id.button_photo).setOnTouchListener((v, event) -> {
            Snackbar.make(
                    getActivity().findViewById(android.R.id.content),
                    "Display photos",
                    Snackbar.LENGTH_SHORT);
            return true;
        });

        userNode = panel;
    }

    private void showLoadingMessage() {
        if (loadingMessageSnackbar != null && loadingMessageSnackbar.isShownOrQueued()) {
            return;
        }

        loadingMessageSnackbar =
                Snackbar.make(
                        getActivity().findViewById(android.R.id.content),
                        "Searching for surfaces...",
                        Snackbar.LENGTH_INDEFINITE);
        loadingMessageSnackbar.getView().setBackgroundColor(0xbf323232);
        loadingMessageSnackbar.show();
    }

    private void hideLoadingMessage() {
        if (loadingMessageSnackbar == null) {
            return;
        }

        loadingMessageSnackbar.dismiss();
        loadingMessageSnackbar = null;
    }

}
