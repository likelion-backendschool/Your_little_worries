package likelion.ylw.comment;

import likelion.ylw.article.Article;
import likelion.ylw.member.Member;
import likelion.ylw.util.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    /**
     * 투표지id에 해당하는 댓글 목록 가져오기
     */
    public List<Comment> getCommentByArticleId(Article article) {
        return commentRepository.findByArticle(article);
    }

    /**
     *  회원 댓글 등록
     */
    public void create(Article article, String content, Member author) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setArticle(article);
        comment.setAuthor(author);
        this.commentRepository.save(comment);
    }
    /**
     *  비회원 댓글 등록
     */
    public void create(Article article, CommentForm commentForm) {
        Comment comment = new Comment();
        comment.setArticle(article);
        comment.setContent(commentForm.getContent());
        comment.setTempNickname(commentForm.getTempNickname());
        comment.setTempPassword(commentForm.getTempPassword());
        this.commentRepository.save(comment);
    }

    /**
     * 해당 id의 댓글 불러오기
     */
    public Comment getComment(Integer id) {
        Optional<Comment> comment = this.commentRepository.findById(id);
        if (comment.isPresent()) {
            return comment.get();
        } else {
            throw new DataNotFoundException("comment not found");
        }
    }

    /**
     * 댓글 수정
     */
    public void modify(Comment comment, String content) {
        comment.setContent(content);
        this.commentRepository.save(comment);
    }

    /**
     * 댓글 삭제
     */
    public void delete(Comment answer) {
        this.commentRepository.delete(answer);
    }


}
