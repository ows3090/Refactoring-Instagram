package com.androidstudy.model;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

public class FollowDTO {

    @Getter @Setter
    private int followerCount = 0;

    @Getter @Setter
    private Map<String, Boolean> followers = new HashMap<>();

    @Getter @Setter
    private int followingCount = 0;

    @Getter @Setter
    private Map<String, Boolean> followings = new HashMap<>();
}
