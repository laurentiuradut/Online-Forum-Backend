package com.laur.licenta.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentsDto {
    private String id;
    private String text;
    private String postId;
    private String username;
    private Instant createdDate;
    private String duration;
}
