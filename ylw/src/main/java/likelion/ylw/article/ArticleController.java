package likelion.ylw.article;

import likelion.ylw.category.Category;
import likelion.ylw.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/article")
public class ArticleController {

    private final ArticleService articleService;

    private final CategoryService categoryService;

//    @RequestMapping("/list")
//    public String list(Model model) {
//        List<Article> articleList = articleService.getList();
//
//        model.addAttribute("articleList", articleList);
//
//        return "article_list";
//    }

    @GetMapping("/list")
    public String list(Model model, @RequestParam("category") Integer category_id) {
        Category category = categoryService.findById(category_id);
        List<Article> articleList = articleService.findByCategory(category);

        model.addAttribute("articleList", articleList);

        return "article_list";
    }

    @GetMapping("/vote/{id}")
    public String vote(Model model, @PathVariable("id") Integer id) {
        Article article = articleService.findById(id);

        model.addAttribute("article", article);

        return "article_vote";
    }

    @RequestMapping("/categoryList")
    public String categoryList(Model model) {
        List<Category> categoryList = categoryService.getList();

        model.addAttribute("categoryList", categoryList);

        return "category_list";
    }

}
