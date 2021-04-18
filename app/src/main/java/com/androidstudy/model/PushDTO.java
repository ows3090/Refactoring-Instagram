package com.androidstudy.model;

import lombok.Getter;
import lombok.Setter;

public class PushDTO {

    @Setter @Getter
    private String to = null;

    @Setter @Getter
    private Notification notification = new Notification();

    public class Notification{
        @Setter @Getter
        private String body = null;

        @Setter @Getter
        private String title = null;
    }
}
