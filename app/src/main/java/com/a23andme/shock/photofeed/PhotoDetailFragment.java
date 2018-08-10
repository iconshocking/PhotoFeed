package com.a23andme.shock.photofeed;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.a23andme.shock.photofeed.Network.Response;
import com.bumptech.glide.Glide;

import static com.a23andme.shock.photofeed.FeedAdapter.LIKES;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class PhotoDetailFragment extends Fragment {
    private static final long SEC_IN_MIN = 60;
    private static final long SEC_IN_HOUR = 60 * SEC_IN_MIN;
    private static final long SEC_IN_DAY = 24 * SEC_IN_HOUR;
    private static final long SEC_IN_WEEK = 7 * SEC_IN_DAY;

    PhotoPresenter presenter;
    Response.Photo photo;

    public void setPresenter(PhotoPresenter presenter) {
        this.presenter = presenter;
    }

    public void setPhoto(Response.Photo photo) {
        this.photo = photo;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup mRootView = (ViewGroup) inflater.inflate(R.layout.photo_detail, container, false);

        ImageView photoView = mRootView.findViewById(R.id.photo);
        Glide.with(photoView.getContext())
                .load(photo.getImages().getStandard_resolution().getUrl())
                .transition(withCrossFade())
                .into(photoView);

        TextView likesTextView = mRootView.findViewById(R.id.like_text);
        likesTextView.setText(Integer.toString(photo.getLikes().getCount()) + " " + LIKES);

        TextView timeTextView = mRootView.findViewById(R.id.post_time_text);
        timeTextView.setText(convertPostedTimestampToText(photo.getCreatedTime()));

        final ImageView likeIconView = mRootView.findViewById(R.id.like_icon);
        int color = likeIconView.getResources().getColor(
                photo.getLikes().haveLiked() ? android.R.color.holo_red_light : android.R.color.darker_gray);
        likeIconView.getDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        likeIconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photo != null) {
                    boolean newLikedValue = presenter.likeClicked(photo);
                    if (newLikedValue) {
                        likeIconView.getDrawable().setColorFilter(
                                v.getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP);
                        Animation animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.heart_bounce_anim);
                        likeIconView.startAnimation(animation);
                    } else {
                        likeIconView.getDrawable().setColorFilter(
                                v.getResources().getColor(android.R.color.darker_gray), PorterDuff.Mode.SRC_ATOP);
                    }
                }
            }
        });

        return mRootView;
    }

    private String convertPostedTimestampToText(long timestamp) {
        String timeString;
        long currentTime = System.currentTimeMillis() / 1000;
        long timeDiff = (currentTime - timestamp);
        if (timeDiff < SEC_IN_MIN) {
            timeString = Long.toString(timeDiff) + "s";
        } else if (timeDiff < SEC_IN_HOUR) {
            timeString = Long.toString(timeDiff / SEC_IN_MIN) + "m";
        } else if (timeDiff < SEC_IN_DAY) {
            timeString = Long.toString(timeDiff / SEC_IN_HOUR) + "h";
        } else if (timeDiff < SEC_IN_WEEK) {
            timeString = Long.toString(timeDiff / SEC_IN_DAY) + "d";
        } else {
            timeString = Long.toString(timeDiff / SEC_IN_WEEK) + "w";
        }
        return timeString + " ago";
    }
}
