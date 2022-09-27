package likelion.ylw.article;

import likelion.ylw.category.Category;
import likelion.ylw.category.CategoryService;
import likelion.ylw.comment.Comment;
import likelion.ylw.member.Member;
import likelion.ylw.member.MemberRepository;
import likelion.ylw.member.MemberService;
import likelion.ylw.util.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    private final CategoryService categoryService;

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    public List<Article> getList() {
        return articleRepository.findAll();
    }

    public List<Article> getSearchList(String kw) {
        Specification<Article> spec = search(kw);
        return this.articleRepository.findAll(spec);
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

    public List<Article> findByCategoryTop3(Category category) {
        return articleRepository.findTop3ByCategoryOrderByIdDesc(category);
    }

    public List<Article> findByViewCountTop6() {
        return articleRepository.findTop6ByOrderByViewCountDesc();
    }

    public List<Article> findTop6() {
        return articleRepository.findTop6ByOrderByIdDesc();
    }

    public List<Article> findByAuthor(Member member) {
        return articleRepository.findByAuthor(member);
    }

    public Article create(String title, String content, String author,Integer category_id) {
        Category category = categoryService.findById(category_id);

        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);
        article.setAuthor(memberRepository.findByMemberId(author).get());
        article.setCategory(category);
        article.getAuthor().setEnrollCount(memberRepository.findByMemberId(author).get().getEnrollCount()+ 1);
        articleRepository.save(article);
        memberService.evalEnrollScore(article.getAuthor());
        return article;
    }

    public Article createPupular(String title, String content, String author, Integer category_id, Long view_count) {
        Category category = categoryService.findById(category_id);

        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);
        article.setAuthor(memberRepository.findByMemberId(author).get());
        article.setCategory(category);
        article.setViewCount(view_count);

        articleRepository.save(article);
        return article;
    }

    public void delete(Integer id) {
        Optional<Article> article = articleRepository.findById(id);
        if (article.isPresent()) {
            articleRepository.delete(article.get());
        }
    }

    public void modify(Article article, String content, Integer category_id) {
        article.setContent(content);
        article.setCategory(categoryService.findById(category_id));

        articleRepository.save(article);
    }

    private Specification<Article> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Article> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                return cb.or(cb.like(q.get("title"), "%" + kw + "%"), // 제목
                        cb.like(q.get("content"), "%" + kw + "%"));      // 내용
            }
        };
    }

    public Page<Article> getPageList(int page, Integer category_id) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createdDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return articleRepository.findAllByCategoryId(category_id ,pageable);
    }

    /* Views Counting */
    @Transactional
    public Integer updateViewCount(Integer id) {
        return articleRepository.updateViewCount(id);
    }
}
