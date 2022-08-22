package likelion.ylw.article;

import likelion.ylw.category.Category;
import likelion.ylw.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/article")
public class ArticleController {

    private final ArticleService articleService;

    private final CategoryService categoryService;

    @GetMapping("/list")
    public String list(Model model, @RequestParam("category") Integer category_id) {
        Category category = categoryService.findById(category_id);
        List<Article> articleList = articleService.findByCategory(category);

        model.addAttribute("articleList", articleList);
        model.addAttribute("category", category);

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

    @GetMapping("/create")
    public String createArticle(ArticleForm articleForm, @RequestParam("category") Integer category_id, Model model) {
        List<Category> categoryList = categoryService.getList();

        model.addAttribute("category_id", category_id);
        model.addAttribute("categoryList", categoryList);
        return "article_form";
    }

    @PostMapping("/create")
    public String createArticle(@Valid ArticleForm articleForm, BindingResult bindingResult,
                                @RequestParam("category") Integer category_id) {
        if (bindingResult.hasErrors()) {
            return "article_form";
        }

        Integer id = articleService.create(articleForm.getTitle(), articleForm.getContent(), category_id);

        return String.format("redirect:/article/vote/%d", id);
    }
}
