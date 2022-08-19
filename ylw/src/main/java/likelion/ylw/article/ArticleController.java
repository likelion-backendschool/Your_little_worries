package likelion.ylw.article;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/article")
public class ArticleController {

    private final ArticleService articleService;

    @RequestMapping("/list")
    public String list(Model model) {
        List<Article> articleList = articleService.getList();

        model.addAttribute("articleList", articleList);

        return "article_list";
    }
}
