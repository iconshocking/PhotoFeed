package com.a23andme.shock.photofeed.model.network;

import java.util.List;

public class Response {

    public static class Data {
        List<Photo> data;

        public List<Photo> getData() {
            return data;
        }
    }


    public static class Photo {
        Likes likes;
        String media_id;
        Images images;
        long created_time;

        public Likes getLikes() {
            return likes;
        }

        public Images getImages() {
            return images;
        }

        public String getMedia_id() {
            return media_id;
        }

        public long getCreatedTime() {
            return created_time;
        }
    }


    public static class Likes {
        int count;
        boolean haveLiked;

        public int getCount() {
            if (haveLiked) {
                return count + 1;
            } else {
                return count;
            }
        }

        public boolean haveLiked() {
            return haveLiked;
        }

        public boolean changeIfLikedOrNot() {
            haveLiked = !haveLiked;
            return haveLiked;
        }
    }


    public static class Images {
        Image standard_resolution;
        Image thumbnail;
        Image low_resolution;

        public Image getThumbnail() {
            return thumbnail;
        }

        public Image getStandard_resolution() {
            return standard_resolution;
        }

        public Image getLow_resolution() {
            return low_resolution;
        }
    }


    public static class Image {
        int height, width;
        String url;

        public String getUrl() {
            return url;
        }

        public int getHeight() {
            return height;
        }

        public int getWidth() {
            return width;
        }
    }


    public static class StatusResponse {
        private static final int OK_STATUS_CODE = 200;
        Meta meta;

        public boolean isCodeOK() {
            return meta.code == 200;
        }
    }


    public static class Meta {
        int code;
    }
}
