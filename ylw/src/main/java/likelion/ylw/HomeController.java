package likelion.ylw;

import likelion.ylw.article.Article;
import likelion.ylw.article.ArticleService;
import likelion.ylw.category.Category;
import likelion.ylw.category.CategoryService;
import likelion.ylw.notice.Notice;
import likelion.ylw.notice.NoticeService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ArticleService articleService;
    private final CategoryService categoryService;
    private final NoticeService noticeService;

    @RequestMapping("/")
    public String homeController(Model model) {
        List<Category> categoryList = categoryService.getList();

        Object[] articleLists = IntStream.rangeClosed(1, categoryList.size()).mapToObj(i -> {
            Category category = categoryService.findById(i);
            List<Article> articleList = articleService.findByCategoryTop8(category);
            return articleList;
        }).toArray();

        List<Article> newArticleList = articleService.findTop8();
        List<Article> popularArticleList = articleService.findByViewCountTop8();

        Notice recentNotice = noticeService.getNoticeByTop1();

        model.addAttribute("recentNotice", recentNotice);

        model.addAttribute("popularArticleList", popularArticleList);

        model.addAttribute("newArticleList", newArticleList);

        model.addAttribute("articleListArray", articleLists);

        model.addAttribute("categoryList", categoryList);

        return "index";
    }

    @RequestMapping("/layout")
    public String layouttest() {
        return "examples/tables";
    }
}
