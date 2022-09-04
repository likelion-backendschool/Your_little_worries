package likelion.ylw.article;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleItemService {

    private final ArticleItemRepository articleItemRepository;


    public ArticleItem create(Article article, String content) {
        ArticleItem articleItem = new ArticleItem();
        articleItem.setArticle(article);
        articleItem.setContent(content);
        articleItemRepository.save(articleItem);
        return articleItem;
    }

    public List<ArticleItem> findArticleItemByArticleId(Integer ArticleId) {
        List<ArticleItem> articleItems = articleItemRepository.findArticleItemByArticleId(ArticleId);

        return articleItems;
    }
}
