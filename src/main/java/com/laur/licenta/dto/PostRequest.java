package com.laur.licenta.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {
    private String postId;
    private String postName;
    private String categoryName;
    private String url;
    private String description;
}
