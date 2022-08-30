package likelion.ylw.comment;

import likelion.ylw.article.Article;
import likelion.ylw.article.ArticleService;
import likelion.ylw.member.Member;
import likelion.ylw.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller와 Service에 멤버를 적용하지 않은 상태입니다.
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final ArticleService articleService;
    private final MemberService memberService;

    /**
     * 회원 댓글 생성하기
     * {id} = 게시글 id
     */
    @PostMapping("/create/{id}")
    public String createComment(Model model, @PathVariable("id") Integer id,
                                @Valid CommentForm commentForm, BindingResult bindingResult, Principal principal) {

        Article article = this.articleService.findById(id);

        // form 검증
        if (bindingResult.hasErrors()) {
            List<Comment> commentList = commentService.getCommentByArticleId(article);
            model.addAttribute("article", article);
            model.addAttribute("commentList",commentList);
            return "article_result";
        }
        // 댓글 등록
        Member member = this.memberService.getMemberId(principal.getName());
        this.commentService.create(article, commentForm.getContent(), member);

        return String.format("redirect:/article/result/%s", id);
    }
    /**
     * 비회원 댓글 생성하기
     */
    @PostMapping("/non-create/{id}")
    public String createComment(Model model, @PathVariable("id") Integer id,
                                @Valid NonMemberCommentForm nonMemberCommentForm, BindingResult bindingResult) {

        Article article = this.articleService.findById(id);

        // form 검증
        if (bindingResult.hasErrors()) {
            List<Comment> commentList = commentService.getCommentByArticleId(article);
            model.addAttribute("article", article);
            model.addAttribute("commentList",commentList);
            return "article_result";
        }
        // 댓글 등록
        this.commentService.create(article, nonMemberCommentForm);

        return String.format("redirect:/article/result/%s", id);
    }
    /**
     * 댓글 수정하기
     */
    @PostMapping("/modify/{id}")
    public String modifyComment(@Valid CommentForm commentForm, BindingResult bindingResult,
                                @PathVariable("id") Integer id, Principal principal) {
//        if (bindingResult.hasErrors()) {
//            return String.format("redirect:/article/result/%s", id);
//        }
        Comment comment = this.commentService.getComment(id);

        //수정 권한 체크
        if (!comment.getAuthor().getMemberId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.commentService.modify(comment, commentForm.getContent());
        return String.format("redirect:/article/result/%s", comment.getArticle().getId());
    }

    /**
     * 회원 댓글 삭제하기
     */
    @GetMapping("/delete/{id}")
    public String deleteComment(@PathVariable("id") Integer id, Principal principal) {
        Comment comment = this.commentService.getComment(id);

        /*수정 권한 체크*/
        if (!comment.getAuthor().getMemberId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        this.commentService.delete(comment);
        return String.format("redirect:/article/result/%s", comment.getArticle().getId());
    }
    /**
     * 비회원 댓글 삭제하기
     * json 데이터를 반환해줌
     */
    @ResponseBody
    @PostMapping("/non-delete/{id}")
    public Map<String, String> deleteComment(@PathVariable("id") Integer id, @RequestBody NonMemberCommentForm nonMemberCommentForm) {
        Comment comment = this.commentService.getComment(id);

        Map<String,String> result = new HashMap<>();
        boolean isTempMember =commentService.getResultByTempMember(nonMemberCommentForm.getTempNickname(), nonMemberCommentForm.getTempPassword());
        if (isTempMember) {
            result.put("result","success");
            this.commentService.delete(comment);
            return result;
        }
        result.put("result","failure");
        return result;
    }
}
