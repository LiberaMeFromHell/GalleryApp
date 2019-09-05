package com.example.galleryapp.view;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.galleryapp.R;

import java.util.SortedMap;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ImageHolder> {

    private SortedMap<Integer,String> images;
    private Object[] keys;

    public GalleryAdapter(SortedMap<Integer,String> images) {
        this.images = images;
        keys = images.keySet().toArray();
    }

    @NonNull
    @Override
    public GalleryAdapter.ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.image_element,parent,false);
        view.getLayoutParams().width = parent.getMeasuredWidth() / 3;
        view.getLayoutParams().height = parent.getMeasuredHeight() / 4;
        view.setPadding(5,5,5,5);
        ImageHolder imageHolder = new ImageHolder(view);
        return imageHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryAdapter.ImageHolder holder, final int position) {

        final Integer key = (Integer)keys[position];
        holder.set_id(key); //TODO: remove?
        final String uriStr = images.get(key);
        Glide.with(holder.getImageView())
                .load(uriStr)
                .centerCrop()
                .thumbnail(0.1f)
                .into(holder.getImageView());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),PreviewActivity.class);
                intent.putExtra("URI",uriStr);
                intent.putExtra("_ID",key);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    class ImageHolder extends RecyclerView.ViewHolder {

        int _id;
        ImageView imageView;

        ImageHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }

        void set_id(int _id) {
            this._id = _id;
        }

        private ImageView getImageView() {
            return imageView;
        }
    }
}
