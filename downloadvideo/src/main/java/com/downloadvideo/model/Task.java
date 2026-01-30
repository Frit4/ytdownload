package com.downloadvideo.model;

import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    private Long videoDataId;
    private String url;
    private String videoId;
    private String audioId;
    private int duration;
}
