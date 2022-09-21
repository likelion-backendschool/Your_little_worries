package likelion.ylw.comment.vote;

import likelion.ylw.comment.Comment;
import likelion.ylw.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@RequiredArgsConstructor
@Service
public class CommentVoteService {

    private final CommentVoteRepository commentVoteRepository;

    /**
     * 좋아요 추가
     */
    public void pushVoteBtn(Comment comment, Member member) {

        boolean result = commentVoteRepository.existsByMemberAndComment(member, comment);
        if (result) {
            commentVoteRepository.deleteByMemberAndComment(member, comment);
            // 이미 좋아요를 누른 상태인 경우 취소해줌
        } else {
            // 좋아요가 아닌 상태인 경우 추가해줌
            CommentVote commentVote = new CommentVote();
            commentVote.setMember(member);
            commentVote.setComment(comment);
            commentVoteRepository.save(commentVote);
        }
    }

    /**
     * 회원이 좋아요 누른 댓글 조회
     */
    public Set<Comment> getCommentsByMemberId(Member member) {
        return commentVoteRepository.findByMember(member);
    }
}
