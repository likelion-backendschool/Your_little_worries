package likelion.ylw.article;

import likelion.ylw.category.Category;
import likelion.ylw.category.CategoryService;
import likelion.ylw.member.MemberRepository;
import likelion.ylw.util.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    private final CategoryService categoryService;

    private final MemberRepository memberRepository;

    public List<Article> getList() {
        return articleRepository.findAll();
    }

    public Article findByTitle(String title) {
        Optional<Article> article = articleRepository.findByTitle(title);
        if (article.isPresent()) {
            return article.get();
        } else {
            return null;
        }
    }

    public Article findById(Integer id) {
        Optional<Article> article = articleRepository.findById(id);
        if (article.isPresent()) {
            return article.get();
        } else {
            throw new DataNotFoundException("article not found");
        }
    }

    public List<Article> findByCategory(Category category) {
        List<Article> articleList = articleRepository.findByCategory(category);
        return articleList;
    }


    public Article create(String title, String content, String author,Integer category_id) {
        Category category = categoryService.findById(category_id);
        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);
        article.setAuthor(memberRepository.findByMemberId(author).get());
        article.setCategory(category);

        articleRepository.save(article);
        return article;
    }

    public void delete(Integer id) {
        Optional<Article> article = articleRepository.findById(id);
        if (article.isPresent()) {
            articleRepository.delete(article.get());
        }
    }

    public void modify(Article article, String title, String content) {
        article.setTitle(title);
        article.setContent(content);

        articleRepository.save(article);
    }
}
