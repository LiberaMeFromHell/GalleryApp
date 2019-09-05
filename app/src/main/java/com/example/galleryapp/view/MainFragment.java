package com.example.galleryapp.view;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galleryapp.R;
import com.example.galleryapp.model.ImageContainer;

import java.util.List;
import java.util.SortedMap;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainFragment extends Fragment  implements EasyPermissions.PermissionCallbacks{

    private ImageContainer imageContainer;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        view = inflater.inflate(R.layout.fragment_main, container, false);
        return view;
    }
    @AfterPermissionGranted(1)
    private void initRecyclerView() {

        if (EasyPermissions.hasPermissions(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)) {
            imageContainer = ImageContainer.getInstance(getActivity().getApplication());
            SortedMap<Integer,String> imageMap = imageContainer.getImageMap();
            RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
            recyclerView.setAdapter(new GalleryAdapter(imageMap));
        }
        else
            EasyPermissions.requestPermissions(this,"Application cannot work properly without permissions",1,Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        // display a settings dialog if the permission request was disabled.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        initRecyclerView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }
}
