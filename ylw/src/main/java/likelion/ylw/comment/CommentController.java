package likelion.ylw.comment;

import likelion.ylw.article.Article;
import likelion.ylw.article.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final ArticleService articleService;

    /**
     * 댓글 생성
     */
    @PostMapping("/create/{id}")
    public String createAnswer(Model model, @PathVariable("id") Integer id,
                               @Valid CommentForm commentForm, BindingResult bindingResult) {
        Article article = this.articleService.findById(id);

        if (bindingResult.hasErrors()) {
            model.addAttribute("article", article);
            return "article_result"; //미완료
        }
        // 답변 등록
        this.commentService.create(article, commentForm.getContent());
        return String.format("redirect:/article/result/%s", id); //미완료
    }
}
