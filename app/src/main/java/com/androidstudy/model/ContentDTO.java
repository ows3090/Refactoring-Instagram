package com.androidstudy.model;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

public class ContentDTO {

    @Getter @Setter
    private String explain = null;

    @Getter @Setter
    private String imageUri = null;

    @Getter @Setter
    private String uid = null;

    @Getter @Setter
    private String userId = null;

    @Getter @Setter
    private Long timestamp = 0L;

    @Getter @Setter
    private int favoriteCount = 0;

    @Getter @Setter
    private Map<String, Boolean> favorites = new HashMap<>();

    public static class Comment{
        @Getter @Setter
        private String uid = null;

        @Getter @Setter
        private String userId = null;

        @Getter @Setter
        private String comment = null;

        @Getter @Setter
        private Long timestamp = 0L;
    }
}
