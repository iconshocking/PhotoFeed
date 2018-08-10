package com.a23andme.shock.photofeed;

import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.a23andme.shock.photofeed.Network.Response;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

        int color = holder.likeIcon.getResources().getColor(
                photo.getLikes().haveLiked() ? android.R.color.holo_red_light : android.R.color.darker_gray);
        holder.likeIcon.getDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);

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
        Collections.sort(photos, new Comparator<Response.Photo>() {
            @Override
            public int compare(Response.Photo photo1, Response.Photo photo2) {
                long diff = photo2.getCreatedTime() - photo1.getCreatedTime();
                return diff < 0 ? -1 : (diff > 1 ? 1 : 0);
            }
        });
        notifyDataSetChanged();
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView thumbnail;
        private ImageView likeIcon;
        private TextView likeText;
        private Response.Photo photo;

        public PhotoViewHolder(final View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            likeIcon = itemView.findViewById(R.id.like_icon);
            likeText = itemView.findViewById(R.id.like_text);

            likeIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (photo != null) {
                        boolean newLikedValue = presenter.likeClicked(photo);
                        if (newLikedValue) {
                            likeIcon.getDrawable().setColorFilter(
                                    v.getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);
                            Animation animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.heart_bounce_anim);
                            likeIcon.startAnimation(animation);
                        } else {
                            likeIcon.getDrawable().setColorFilter(
                                    v.getResources().getColor(android.R.color.darker_gray), PorterDuff.Mode.SRC_ATOP);
                        }
                    }
                }
            });
        }
    }
}
