package eu.michaelvogt.ar.author;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.Navigation;


public class IntroFragment extends Fragment {

    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_STORAGE = 2;

    public IntroFragment() {/* Required empty public constructor*/}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_intro, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            view.findViewById(R.id.req_needed_header).setVisibility(View.VISIBLE);
            view.findViewById(R.id.camera_req_text).setVisibility(View.VISIBLE);
            View requestCamera = view.findViewById(R.id.camera_req_btn);
            requestCamera.setVisibility(View.VISIBLE);
            requestCamera.setOnClickListener(v -> ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA));

        }

        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            view.findViewById(R.id.req_needed_header).setVisibility(View.VISIBLE);
            view.findViewById(R.id.storage_req_text).setVisibility(View.VISIBLE);
            View requestStorage = view.findViewById(R.id.storage_req_btn);
            requestStorage.setVisibility(View.VISIBLE);
            requestStorage.setOnClickListener(v -> ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE));
        }

        view.findViewById(R.id.start_authoring_btn).setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.action_list_markers)
        );
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int i = 0; i < permissions.length; i++) {
            Log.d("Intro Fragment", permissions[i] + " : " + grantResults[i]);
        }
    }
}
