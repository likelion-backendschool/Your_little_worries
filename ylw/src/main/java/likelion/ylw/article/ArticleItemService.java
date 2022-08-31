package likelion.ylw.article;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleItemService {

    private final ArticleItemRepository articleItemRepository;


    public ArticleItem create(Article article, String content) {
        ArticleItem articleItem = new ArticleItem();
        articleItem.setArticle(article);
        articleItem.setContent(content);
        this.articleItemRepository.save(articleItem);
        return articleItem;
    }
}
