package com.downloadvideo.model;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
@ToString
@NoArgsConstructor
public class ResponseDto {
    private String videoName;
    private List<YoutubeQuality> youtubeQuality;
    private Integer duration;

}
