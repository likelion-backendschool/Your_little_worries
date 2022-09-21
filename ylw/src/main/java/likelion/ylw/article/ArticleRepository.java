package likelion.ylw.article;

import likelion.ylw.category.Category;
import likelion.ylw.member.Member;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
    Optional<Article> findByTitle(String title);

    List<Article> findByCategory(Category category);

    List<Article> findTop3ByCategoryOrderByIdDesc(Category category);

    List<Article> findTop6ByOrderByIdDesc();

    List<Article> findTop6ByOrderByViewCountDesc();

    List<Article> findByAuthor(Member member);

    List<Article> findAll(Specification<Article> spec);
}
