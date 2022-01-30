package com.laur.licenta.service;

import com.laur.licenta.dto.VoteDto;
import com.laur.licenta.exceptions.GenericException;
import com.laur.licenta.exceptions.PostNotFoundException;
import com.laur.licenta.models.Post;
import com.laur.licenta.models.Vote;
import com.laur.licenta.repository.PostRepository;
import com.laur.licenta.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import static com.laur.licenta.models.VoteType.UPVOTE;

@Service
@AllArgsConstructor
public class VoteService {

    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private  AuthService authService;

    @Transactional
    public void vote(VoteDto voteDto) {
        Post post = postRepository.findByPostId(voteDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post Not Found with ID - " + voteDto.getPostId()));
        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
        if (voteByPostAndUser.isPresent() &&
                voteByPostAndUser.get().getVoteType()
                        .equals(voteDto.getVoteType())) {
            throw new GenericException("You have already "
                    + voteDto.getVoteType() + "'d for this post");
        }
        if (UPVOTE.equals(voteDto.getVoteType())) {
            post.setVoteCount(post.getVoteCount() + 1);
        } else {
            post.setVoteCount(post.getVoteCount() - 1);
        }
        voteRepository.save(mapToVote(voteDto, post));
        postRepository.save(post);
    }


    private Vote mapToVote(VoteDto voteDto,Post post){
        Vote vote=new Vote();
        vote.setVoteType(voteDto.getVoteType());
        vote.setPost(post);
        vote.setUser(authService.getCurrentUser());

        return vote;
    }

}
