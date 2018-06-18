package eu.michaelvogt.ar.author;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import eu.michaelvogt.ar.author.data.AuthorViewModel;
import eu.michaelvogt.ar.author.data.Marker;

public class AuthorActivity extends AppCompatActivity {

    private AuthorViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(AuthorViewModel.class);
        initViewModel();

        setContentView(R.layout.activity_author);
    }

    private void initViewModel() {
        viewModel.addMarker(new Marker(
                "/Touristar/Markers/IMG_20180419_092548.jpg",
                "kameishi_sign",
                "城上神社",
                1.365f));
        viewModel.addMarker(new Marker(
                "/Touristar/Markers/IMG_20180419_100912.jpg",
                "hidakaya_sign",
                "日高屋",
                0.644f));
        viewModel.addMarker(new Marker(
                "/Touristar/Markers/IMG_20180419_110133.jpg",
                "kouengate_sign",
                "石見公園",
                0.615f));
        viewModel.addMarker(new Marker(
                "/Touristar/Markers/IMG_20180419_125435.jpg",
                "opera_sign",
                "大森座",
                0.671f));
        viewModel.addMarker(new Marker(
                "/Touristar/Markers/IMG_20180419_101603.jpg",
                "jiso",
                "観世音寺",
                0));
        viewModel.addMarker(new Marker(
                "/Touristar/Markers/IMG_20180419_100646.jpg",
                "aoyamake_sign",
                "青山家",
                0.648f));
        viewModel.addMarker(new Marker(
                "/Touristar/Markers/IMG_20180419_100356.jpg",
                "kumagaike_sign",
                "熊谷家",
                0.648f));
    }
}