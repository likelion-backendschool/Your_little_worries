package likelion.ylw.article;

import likelion.ylw.article.recommend.ArticleRecommendService;
import likelion.ylw.category.Category;
import likelion.ylw.category.CategoryService;
import likelion.ylw.comment.Comment;
import likelion.ylw.comment.CommentForm;
import likelion.ylw.comment.CommentService;
import likelion.ylw.comment.NonMemberCommentForm;
import likelion.ylw.comment.vote.CommentVoteService;
import likelion.ylw.member.Member;
import likelion.ylw.member.MemberRepository;
import likelion.ylw.member.MemberService;
import likelion.ylw.notice.Notice;
import likelion.ylw.stats.StatsCollection;
import likelion.ylw.stats.StatsCollectionForm;
import likelion.ylw.stats.StatsCollectionService;
import likelion.ylw.stats.statsResult.StatsResult;
import likelion.ylw.stats.statsResult.StatsResultService;
import likelion.ylw.util.message.MessageDto;
import likelion.ylw.util.requestservice.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private final MemberRepository memberRepository;
    private final CommentVoteService commentVoteService;
    private final StatsCollectionService statsCollectionService;
    private final RequestService requestService;
    private final StatsResultService statsResultService;
    private final ArticleRecommendService articleRecommendService;

    private final PasswordEncoder passwordEncoder;

    private static int MINIMUM_VOTES = 30;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "category", defaultValue = "0") Integer category_id,
                       @RequestParam(value="page", defaultValue="0") int page) {
        if (category_id == 0) {
            return "error/400";
        }

        Category category = categoryService.findById(category_id);
        Page<Article> paging = articleService.getPageList(page, category_id);

        model.addAttribute("category", category);
        model.addAttribute("paging", paging);

        return "article/article_list";
    }


    @GetMapping("/vote/{id}")
    public String vote(Model model, @PathVariable("id") Integer id, StatsCollectionForm statsCollectionForm,
                       Principal principal, HttpServletRequest request, HttpServletResponse response) {
        Article article = articleService.findById(id);

        /* 조회수 로직 */
        Cookie oldCookie = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("postView")) {
                    oldCookie = cookie;
                }
            }
        }

        /* 조회수 로직 */
        if (oldCookie != null) {
            if (!oldCookie.getValue().contains("["+ id.toString() +"]")) {
                articleService.updateViewCount(id);
                oldCookie.setValue(oldCookie.getValue() + "_[" + id + "]");
                oldCookie.setPath("/");
                oldCookie.setMaxAge(60 * 60 * 24); 							// 쿠키 시간
                response.addCookie(oldCookie);
            }
        } else {
            articleService.updateViewCount(id);
            Cookie newCookie = new Cookie("postView", "[" + id + "]");
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 60 * 24); 								// 쿠키 시간
            response.addCookie(newCookie);
        }

        if (principal != null) {
//             로그인 회원의 좋아요 목록
            Member member = memberService.findByMemberId(principal.getName());
            boolean exist = articleRecommendService.existsByMemberAndArticle(member, article);
            model.addAttribute("exist", exist);
        }

        List<ArticleItem> articleItems = articleItemService.findArticleItemByArticleId(id);

        model.addAttribute("article", article);
        model.addAttribute("articleItems", articleItems);
        model.addAttribute("params", null);

        return "article/article_vote";
    }

    @PostMapping("/vote/{id}")
    public String vote(@Valid StatsCollectionForm statsCollectionForm, BindingResult bindingResult,
                       @PathVariable("id") Integer id, Model model, Principal principal,
                       HttpServletRequest request, MessageDto params) {

        Article article = articleService.findById(id);
        List<ArticleItem> articleItems = articleItemService.findArticleItemByArticleId(id);

        model.addAttribute("article", article);
        model.addAttribute("articleItems", articleItems);

        if (bindingResult.hasErrors()) {
            return "article/article_vote";
        }

        // Client IP
        String clientIp = requestService.getClientIp(request);
        // 암호화된 IP
        String encryptedIp = passwordEncoder.encode(clientIp);

        if (principal == null) {
            // 비로그인의 경우
            List<StatsCollection> findByArticle = statsCollectionService.findByArticle(article);

            int IPcount__ = (int) findByArticle.stream().filter(statsCollection -> passwordEncoder.matches(clientIp, statsCollection.getIP())).count();

            if (IPcount__ != 0) {
                String redirectUri = String.format("/article/vote/%d", id);
                MessageDto message = new MessageDto("이미 투표하셨습니다.", redirectUri, RequestMethod.GET, null);
                showMessageAndRedirect(message, model);
                return "article/article_vote";
            }
        } else {
            // 로그인의 경우
            Member member = memberService.findByMemberId(principal.getName());
            List<StatsCollection> statsCollectionList = statsCollectionService.findByMember(member);
            int memberCount = (int)statsCollectionList.stream().filter(statsCollection -> statsCollection.getArticleItem().getArticle().getId() == id).count();

            if (memberCount != 0) {
                String redirectUri = String.format("/article/vote/%d", id);
                MessageDto message = new MessageDto("이미 투표하셨습니다.", redirectUri, RequestMethod.GET, null);
                showMessageAndRedirect(message, model);
                return "article/article_vote";
            }

            member.setParticipateCount(member.getParticipateCount()+1);
            memberRepository.save(member);
            memberService.evalParticipateScore(member);
        }

        statsCollectionService.createStatsCollection(statsCollectionForm.getArticleItemId(),
                statsCollectionForm.getAge(), statsCollectionForm.getGender(), statsCollectionForm.getUserName(),
                encryptedIp, article);

        // 투표 더 한 것을 db에 계산
        articleItemService.plusResult(statsCollectionForm.getArticleItemId(), statsCollectionForm.getAge(), statsCollectionForm.getGender());

        // 카이제곱검정 수행
        statsResultService.calculate(article);
        return String.format("redirect:/article/result/%d", id);
    }

    private void showMessageAndRedirect(MessageDto params, Model model) {
        model.addAttribute("params", params);
    }

    @RequestMapping("/categoryList")
    public String categoryList(Model model) {
        List<Category> categoryList = categoryService.getList();

        model.addAttribute("categoryList", categoryList);

        return "article/category_list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String createArticle(ArticleForm articleForm, @RequestParam(value = "category", defaultValue = "0") Integer category_id, Model model) {
        List<Category> categoryList = categoryService.getList();

        model.addAttribute("category_id", category_id);
        model.addAttribute("categoryList", categoryList);
        return "article/article_form";
    }

    @PostMapping("/create")
    public String createArticle(@Valid ArticleForm articleForm, BindingResult bindingResult, @RequestParam Integer category_id, Principal principal) {

        if (bindingResult.hasErrors()) {
            return "article/article_form";
        }

        Article article = articleService.create(articleForm.getTitle(), articleForm.getContent(),
                principal.getName(), category_id);
        Stream.of(articleForm.getItems())
                .forEach(item -> {
                    articleItemService.create(article, item);
                });

        // 카이제곱 db 생성
        statsResultService.create(article);

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
    public String modifyArticle(ArticleForm articleForm, @PathVariable("id") Integer id, Model model) {
        Article article = articleService.findById(id);
        Integer category_id = article.getCategory().getId();
        List<Category> categoryList = categoryService.getList();
        Integer modifyToken = 1;

        model.addAttribute("categoryList", categoryList);
        model.addAttribute("category_id", category_id);
        model.addAttribute("modifyToken", modifyToken);

        articleForm.setTitle(article.getTitle());
        articleForm.setContent(article.getContent());

        return "article/article_form";
    }

    @PostMapping("modify/{id}")
    public String modifyArticle(@Valid ArticleForm articleForm, BindingResult bindingResult,
                                @PathVariable("id") Integer id, @RequestParam Integer category_id) {
        if (bindingResult.hasErrors()) {
            return "article/article_form";
        }

        Article article = articleService.findById(id);

        articleService.modify(article, articleForm.getContent(), category_id);

        return String.format("redirect:/article/vote/%d", article.getId());
    }

//    @PreAuthorize("isAuthenticated()")
//    @GetMapping("/recommend/{id}")
//    public String pushRecommendBtn(@PathVariable Integer id, Principal principal, MessageDto params, Model model) {
//        Member member = memberService.findByMemberId(principal.getName());
//        Article article = articleService.findById(id);
//
//        // 본인 글에 추천하는 경우
//        if (article.getAuthor() != null) {
//            if (article.getAuthor().getMemberId().equals(principal.getName())) {
//
//            }
//        }
//        articleRecommendService.pushVoteBtn(article, member);
//
//        return String.format("redirect:/article/vote/%d", id);
//    }

    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/recommend/{id}")
    public Map<String, String> pushRecommendBtn(@PathVariable Integer id, Principal principal) {
        Member member = memberService.findByMemberId(principal.getName());
        Article article = articleService.findById(id);

        Map<String,String> result = new HashMap<>();
//      이미 추천한 경우
        articleRecommendService.pushVoteBtn(article, member);

        result.put("result", "success");
        return result;
    }

    /**
     * [임시] 투표 결과 페이지
     */
    @GetMapping("/result/{id}")
    public String resultArticle(Model model, @PathVariable("id") Integer id, CommentForm commentForm, NonMemberCommentForm nonMemberCommentForm,
                                @RequestParam(value="page", defaultValue="0") int page, Principal principal) {

        Article article = articleService.findById(id);
        statsResultService.calculate(article);
        // 최소 투표수 체크
        int total = articleItemService.getVoteTotal(article);
        if (total < MINIMUM_VOTES) {
            model.addAttribute("rejectResultPage", true );
        }

        if (principal != null) {
            // 로그인 회원의 댓글 좋아요 목록
            Member member = memberService.findByMemberId(principal.getName());
            Set<Comment> votedComments = commentVoteService.getCommentsByMemberId(member);
            model.addAttribute("votedComments", votedComments);
        }

        Page<Comment> commentList = commentService.getCommentByArticleId(article, page);

        StatsResult statsResult = statsResultService.getStatsResultByArticle(article);
        List<ArticleItem> articleItemList = articleItemService.findArticleItemByArticleId(id);

        String maxArticleItemContent = articleItemService.getMaxVoteArticleItemContent(article);
        int maxArticleItemTotal = articleItemService.getMaxVoteArticleItemTotal(article);
        String percentage = articleItemService.getPercentage(article);

        long[][] articleItem2dArr = statsResultService.listTo2DArray(articleItemList);
        articleItem2dArr = statsResultService.transpose(articleItem2dArr);

        model.addAttribute("article", article);
        model.addAttribute("commentList", commentList);
        model.addAttribute("statsResult", statsResult);

        model.addAttribute("articleItemList", articleItemList);
        model.addAttribute("maxArticleItemContent", maxArticleItemContent);
        model.addAttribute("maxArticleItemTotal", maxArticleItemTotal);
        model.addAttribute("percentage", percentage);
        model.addAttribute("articleItem2dArr", articleItem2dArr);


        return "article/article_result";
    }

    @GetMapping("/search")
    public String search(Model model, @RequestParam(value = "kw", defaultValue = "") String kw) {
        List<Article> articleList = articleService.getSearchList(kw);

        model.addAttribute("articleList", articleList);

        return "article/article_search_list";
    }
}
