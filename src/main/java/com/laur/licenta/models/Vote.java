package com.laur.licenta.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "vote")
public class Vote {
    @Id
    private String voteId;
    private VoteType voteType;
    @NotNull
    private Post post;
    private User user;

}
