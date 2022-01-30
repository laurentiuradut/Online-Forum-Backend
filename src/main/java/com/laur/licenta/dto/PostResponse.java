package com.laur.licenta.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private String postId;
    private String postName;
    private String url;
    private String description;
    private String userName;
    private String categoryName;
    private Integer voteCount;
    private Integer commentCount;
    private String duration;
    private boolean upVote;
    private boolean downVote;
}
