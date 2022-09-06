package likelion.ylw.article;

import likelion.ylw.category.Category;
import likelion.ylw.category.CategoryService;
import likelion.ylw.comment.Comment;
import likelion.ylw.comment.CommentForm;
import likelion.ylw.comment.CommentService;
import likelion.ylw.comment.NonMemberCommentForm;
import likelion.ylw.comment.vote.CommentVoteService;
import likelion.ylw.member.Member;
import likelion.ylw.member.MemberService;
import likelion.ylw.stats.StatsCollectionForm;
import likelion.ylw.stats.StatsCollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Controller
@RequiredArgsConstructor
@RequestMapping("/article")
public class ArticleController {

    private final ArticleService articleService;
    private final CommentService commentService;
    private final ArticleItemService articleItemService;
    private final CategoryService categoryService;
    private final MemberService memberService;
    private final CommentVoteService commentVoteService;

    private final StatsCollectionService statsCollectionService;

    @GetMapping("/list")
    public String list(Model model, @RequestParam("category") Integer category_id) {
        Category category = categoryService.findById(category_id);
        List<Article> articleList = articleService.findByCategory(category);

        model.addAttribute("articleList", articleList);
        model.addAttribute("category", category);

        return "article_list";
    }

    @GetMapping("/vote/{id}")
    public String vote(Model model, @PathVariable("id") Integer id, StatsCollectionForm statsCollectionForm) {
        Article article = articleService.findById(id);
        List<ArticleItem> articleItems = articleItemService.findArticleItemByArticleId(id);

        model.addAttribute("article", article);
        model.addAttribute("articleItems", articleItems);

        return "article_vote";
    }

    @PostMapping("/vote/{id}")
    public String vote(@Valid StatsCollectionForm statsCollectionForm, BindingResult bindingResult, @PathVariable("id") Integer id, Model model) {
        if (bindingResult.hasErrors()) {
            return "article_vote";
        }
        statsCollectionService.createStatsCollection(statsCollectionForm.getArticleItemId(),
                statsCollectionForm.getAge(), statsCollectionForm.getGender(), statsCollectionForm.getUserName());

        return String.format("redirect:/article/result/%d", id);
    }

    @RequestMapping("/categoryList")
    public String categoryList(Model model) {
        List<Category> categoryList = categoryService.getList();

        model.addAttribute("categoryList", categoryList);

        return "category_list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String createArticle(ArticleForm articleForm, @RequestParam(value = "category", defaultValue = "0") Integer category_id, Model model) {
        List<Category> categoryList = categoryService.getList();

        model.addAttribute("category_id", category_id);
        model.addAttribute("categoryList", categoryList);
        return "article_form";
    }

    @PostMapping("/create")
    public String createArticle(@Valid ArticleForm articleForm, BindingResult bindingResult, @RequestParam Integer category_id) {

        if (bindingResult.hasErrors()) {
            return "article_form";
        }

        Article article = articleService.create(articleForm.getTitle(), articleForm.getContent(),
                articleForm.getAuthor(),category_id);
        Stream.of(articleForm.getItems())
                .forEach(item -> articleItemService.create(article, item));
        return String.format("redirect:/article/vote/%d", article.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String deleteArticle(@PathVariable("id") Integer id) {
        articleService.delete(id);

        return String.format("redirect:/");
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("modify/{id}")
    public String modifyArticle(ArticleForm articleForm, @PathVariable("id") Integer id) {
        Article article = articleService.findById(id);

        articleForm.setTitle(article.getTitle());
        articleForm.setContent(article.getContent());

        return "article_form";
    }

    @PostMapping("modify/{id}")
    public String modifyArticle(@Valid ArticleForm articleForm, BindingResult bindingResult,
                                @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "article_form";
        }

        Article article = articleService.findById(id);

        articleService.modify(article, articleForm.getTitle(), articleForm.getContent());

        return String.format("redirect:/article/vote/%d", article.getId());
    }

    /**
     * [임시] 투표 결과 페이지
     */
    @GetMapping("/result/{id}")
    public String resultArticle(Model model, @PathVariable("id") Integer id, CommentForm commentForm, NonMemberCommentForm nonMemberCommentForm,
                                @RequestParam(value="page", defaultValue="0") int page, Principal principal) {

        if (principal != null) {
            // 로그인 회원의 댓글 좋아요 목록
            Member member = memberService.getMemberId(principal.getName());
            Set<Comment> votedComments = commentVoteService.getCommentsByMemberId(member);
            model.addAttribute("votedComments", votedComments);
        }

        Article article = articleService.findById(id);
        Page<Comment> commentList = commentService.getCommentByArticleId(article, page);

        model.addAttribute("article", article);
        model.addAttribute("commentList", commentList);

        // 댓글 전달

        return "article_result";
    }
}
