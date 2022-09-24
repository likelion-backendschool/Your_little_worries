package likelion.ylw.comment.vote;

import likelion.ylw.comment.Comment;
import likelion.ylw.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

public interface CommentVoteRepository extends JpaRepository<CommentVote, Long> {
    boolean existsByMemberAndComment(Member member, Comment comment);

    @Transactional // 삭제의 경우
    void deleteByMemberAndComment(Member member, Comment comment);

    @Query("select cv.comment from CommentVote cv where cv.member=:member")
    Set<Comment> findByMember(@Param("member") Member member);
}