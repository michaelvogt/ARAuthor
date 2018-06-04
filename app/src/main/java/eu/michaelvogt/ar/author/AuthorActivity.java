package eu.michaelvogt.ar.author;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.List;

import eu.michaelvogt.ar.author.data.AuthorViewModel;
import eu.michaelvogt.ar.author.data.Marker;

public class AuthorActivity extends AppCompatActivity {

    private AuthorViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(AuthorViewModel.class);
        initViewModel();

        setContentView(R.layout.activity_edit);
    }

    private void initViewModel() {
        viewModel.addMarker(new Marker(
                "/Touristar/Markers/IMG_20180419_092548.jpg",
                "kameishi_sign",
                "城上神社"));
        viewModel.addMarker(new Marker(
                "/Touristar/Markers/IMG_20180419_100912.jpg",
                "hidakaya_sign",
                "日高屋"));
        viewModel.addMarker(new Marker(
                "/Touristar/Markers/IMG_20180419_110133.jpg",
                "kouengate_sign",
                "石見公園"));
        viewModel.addMarker(new Marker(
                "/Touristar/Markers/IMG_20180419_125435.jpg",
                "opera_sign",
                "大森座"));
        viewModel.addMarker(new Marker(
                "/Touristar/Markers/IMG_20180419_101603.jpg",
                "jiso",
                "観世音寺"));
        viewModel.addMarker(new Marker(
                "/Touristar/Markers/IMG_20180419_100646.jpg",
                "aoyamake_sign",
                "青山家"));
    }
}