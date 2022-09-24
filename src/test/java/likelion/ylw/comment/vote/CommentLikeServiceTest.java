package likelion.ylw.comment.vote;

import likelion.ylw.comment.Comment;
import likelion.ylw.comment.CommentService;
import likelion.ylw.member.Member;
import likelion.ylw.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CommentLikeServiceTest {

    @Autowired
    private CommentService commentService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CommentVoteRepository commentLikeRepository;
    @Autowired
    private CommentVoteService commentLikeService;
    @Autowired
    private CommentVoteRepository commentVoteRepository;




}