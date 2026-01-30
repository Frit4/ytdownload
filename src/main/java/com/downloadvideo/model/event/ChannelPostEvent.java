package com.downloadvideo.model.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ChannelPostEvent {
    private Long videoDataId;
    private String fileId;
    private boolean isNew;
}
