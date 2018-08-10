package com.a23andme.shock.photofeed;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.a23andme.shock.photofeed.Network.Response;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.PhotoViewHolder> {
    public static final String LIKES = "likes";

    PhotoPresenter presenter;
    List<Response.Photo> photos = new ArrayList<>();

    public FeedAdapter(@NonNull PhotoPresenter photoPresenter) {
        presenter = photoPresenter;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PhotoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item, null));
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Response.Photo photo = photos.get(position);
        holder.photo = photo;
        holder.likeText.setText(Integer.toString(photo.getLikes().getCount()) + " " + LIKES);
        Glide.with(holder.itemView)
                .load(photo.getImages().getStandard_resolution().getUrl())
                .transition(withCrossFade())
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public void addFeedItems(List<Response.Photo> newPhotos) {
        photos.addAll(newPhotos);
        notifyDataSetChanged();
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView thumbnail;
        private ImageView likeIcon;
        private TextView likeText;
        private Response.Photo photo;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            likeIcon = itemView.findViewById(R.id.like_icon);
            likeText = itemView.findViewById(R.id.like_text);

            likeIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (photo != null) {
                        presenter.likeClicked(photo);
                    }
                }
            });
        }
    }
}
