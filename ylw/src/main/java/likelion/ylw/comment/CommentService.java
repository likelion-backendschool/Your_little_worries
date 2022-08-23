package likelion.ylw.comment;

import likelion.ylw.article.Article;
import likelion.ylw.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    /**
     *  댓글 등록
     */
    public void create(Article article, String content) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setArticle(article);
        this.commentRepository.save(comment);
    }
}
