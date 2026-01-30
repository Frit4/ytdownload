package com.taskdistributor.taskdistributor.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class InfoVideo {
    private Long videoDataId;
    private String info;
    private InfoVideoType type;
}
