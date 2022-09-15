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

    /**
     * 유저가 투표하는 순간 총 합계를 db에 바로 적용
     */
    public void plusResult(Integer articleItemId, Integer age, String gender) {

        ArticleItem articleItem = articleItemRepository.findById(articleItemId).get();

        articleItem.setTotal(articleItem.getTotal()+1);

        if (gender.equals("male")) {
            articleItem.setMaleTotal(articleItem.getMaleTotal()+1);
        } else {
            articleItem.setFemaleTotal(articleItem.getFemaleTotal()+1);
        }

        switch (age) {
            case 10 -> articleItem.setTotal10(articleItem.getTotal10()+1);
            case 20 -> articleItem.setTotal20(articleItem.getTotal20()+1);
            case 30 -> articleItem.setTotal30(articleItem.getTotal30()+1);
            case 40 -> articleItem.setTotal40(articleItem.getTotal40()+1);
            default -> articleItem.setTotalOver50(articleItem.getTotalOver50()+1);
        }
        articleItemRepository.save(articleItem);
    }

    public Integer getVoteTotal(Article article) {
        List<ArticleItem> articleItems = articleItemRepository.findArticleItemByArticleId(article.getId());
        int sum = 0;
        for (ArticleItem articleItem : articleItems) {
            sum += articleItem.getTotal();
        }
        return sum;
    }
}
