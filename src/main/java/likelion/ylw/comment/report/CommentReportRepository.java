package likelion.ylw.comment.report;

import likelion.ylw.comment.Comment;
import likelion.ylw.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentReportRepository extends JpaRepository<CommentReport, Long> {
    boolean existsByMemberAndComment(Member member, Comment comment);

    int countByComment(Comment comment);
}
