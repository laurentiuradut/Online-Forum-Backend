package com.laur.licenta.dto;

import com.laur.licenta.models.VoteType;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteDto {
    private VoteType voteType;
    private String postId;
}
