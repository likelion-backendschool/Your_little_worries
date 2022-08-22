package likelion.ylw.article;

import likelion.ylw.category.Category;
import likelion.ylw.util.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

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


}
