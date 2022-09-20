package likelion.ylw.comment.report;

import likelion.ylw.comment.Comment;
import likelion.ylw.comment.vote.CommentVote;
import likelion.ylw.comment.vote.CommentVoteRepository;
import likelion.ylw.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentReportService {

    public final CommentReportRepository commentReportRepository;

    /**
     * 신고 추가
     */
    public boolean pushReportBtn(Comment comment, Member member) {
        boolean result = commentReportRepository.existsByMemberAndComment(member, comment);
        if (result) {
            return true;
        }
        CommentReport commentReport = new CommentReport();
        commentReport.setMember(member);
        commentReport.setComment(comment);
        commentReportRepository.save(commentReport);

        return false;

    }

}
