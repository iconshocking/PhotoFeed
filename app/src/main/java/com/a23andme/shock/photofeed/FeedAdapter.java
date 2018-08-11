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

import static com.a23andme.shock.photofeed.FeedActivity.FEED_COLUMN_COUNT;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.PhotoViewHolder> {
    public static final String LIKES = "likes";

    RecyclerView recyclerView;
    PhotoPresenter presenter;
    List<Response.Photo> photos = new ArrayList<>();

    public FeedAdapter(@NonNull RecyclerView recyclerView, @NonNull PhotoPresenter photoPresenter) {
        this.recyclerView = recyclerView;
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

        int imageWidth = (holder.image.getContext().getResources().getDisplayMetrics().widthPixels / FEED_COLUMN_COUNT)
                - (int) (4 * holder.image.getResources().getDisplayMetrics().density);
        Response.Image image = photo.getImages().getLow_resolution();
        holder.image.getLayoutParams().height = (int) (((float) imageWidth / image.getWidth()) * image.getHeight());
        Glide.with(holder.itemView)
                .load(image.getUrl())
                .transition(withCrossFade())
                .into(holder.image);
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

    public void animateLikeForPhoto(Response.Photo photo, boolean newLikedValue) {
        int position = photos.indexOf(photo);
        View view = recyclerView.getLayoutManager().findViewByPosition(position);
        if (view != null && view.getTag() instanceof PhotoViewHolder) {
            PhotoViewHolder viewHolder = (PhotoViewHolder) view.getTag();

            if (newLikedValue) {
                viewHolder.likeIcon.getDrawable().setColorFilter(
                        view.getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);
                Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.heart_bounce_anim);
                viewHolder.likeIcon.startAnimation(animation);
            } else {
                viewHolder.likeIcon.getDrawable().setColorFilter(
                        view.getResources().getColor(android.R.color.darker_gray), PorterDuff.Mode.SRC_ATOP);
            }

            viewHolder.likeText.setText(Integer.toString(photo.getLikes().getCount()) + " " + LIKES);
        }
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private ImageView likeIcon;
        private TextView likeText;
        private Response.Photo photo;

        public PhotoViewHolder(final View itemView) {
            super(itemView);
            itemView.setTag(this);
            image = itemView.findViewById(R.id.photo);
            likeIcon = itemView.findViewById(R.id.like_icon);
            likeText = itemView.findViewById(R.id.like_text);

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.photoClicked(photo);
                }
            });

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
