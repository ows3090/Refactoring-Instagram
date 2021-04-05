package com.androidstudy.model;

import lombok.Getter;
import lombok.Setter;

public class AlarmDTO {

    @Getter @Setter
    private String destinationUid = null;

    @Getter @Setter
    private String userId = null;

    @Getter @Setter
    private String uid = null;

    // 0 : like alarm
    // 1 : comment alarm
    // 2 : follow alarm
    @Getter @Setter
    private int kind = 0;

    @Getter @Setter
    private String message = null;

    @Getter @Setter
    private Long timestamp = 0L;
}
