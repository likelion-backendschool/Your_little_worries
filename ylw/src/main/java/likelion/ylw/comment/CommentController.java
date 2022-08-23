package likelion.ylw.comment;

import likelion.ylw.article.Article;
import likelion.ylw.article.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

/**
 * Controller와 Service에 멤버를 적용하지 않은 상태입니다.
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final ArticleService articleService;

    /**
     * 댓글 생성하기
     */
    @PostMapping("/create/{id}")
    public String commentCreate(Model model, @PathVariable("id") Integer id,
                               @Valid CommentForm commentForm, BindingResult bindingResult) {
        Article article = this.articleService.findById(id);

        if (bindingResult.hasErrors()) {
            model.addAttribute("article", article);
            return String.format("redirect:/article/result/%s", id);
        }
        // 답변 등록
        this.commentService.create(article, commentForm.getContent());
        return String.format("redirect:/article/result/%s", id);
    }

    /**
     * 댓글 수정하기
     */
    @PostMapping("/modify/{id}")
    public String modifyComment(@Valid CommentForm commentForm, BindingResult bindingResult,
                                @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return String.format("redirect:/article/result/%s", id);
        }
        Comment comment = this.commentService.getComment(id);

        //수정권한체크

        this.commentService.modify(comment, commentForm.getContent());

        commentForm.setContent(comment.getContent());
        return String.format("redirect:/article/result/%s", comment.getArticle().getId());
    }

    /**
     * 댓글 삭제하기
     */
    @GetMapping("/delete/{id}")
    public String deleteComment(@PathVariable("id") Integer id) {
        Comment comment = this.commentService.getComment(id);

        // 수정권한체크

        this.commentService.delete(comment);
        return String.format("redirect:/article/result/%s", comment.getArticle().getId());
    }
}
