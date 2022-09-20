package likelion.ylw.comment.report;

import likelion.ylw.comment.Comment;
import likelion.ylw.comment.CommentRepository;
import likelion.ylw.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentReportService {

    private final CommentReportRepository commentReportRepository;
    private final CommentRepository commentRepository;
    

    /**
     * 신고 추가
     */
    public boolean pushReportBtn(Comment comment, Member member) {
        // 유저가 해당 댓글을 이미 신고한 경우
        boolean result = commentReportRepository.existsByMemberAndComment(member, comment);
        if (result) {
            return true;
        }
        // 댓글의 신고회수가 초과된 경우 삭제
        if (comment.getReports().size() >= 15) {
            commentRepository.delete(comment);
            return false;
        }
        CommentReport commentReport = new CommentReport();
        commentReport.setMember(member);
        commentReport.setComment(comment);
        commentReportRepository.save(commentReport);

        return false;

    }

}
