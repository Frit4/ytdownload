package com.downloadvideo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class YtDlpInfo {
    private String title;
    private List<YtFormat> formats;
    private Integer duration;
}