package likelion.ylw.notice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Integer> {

    @Modifying
    @Query("update Notice p set p.viewCount = p.viewCount + 1 where p.id = :id")
    int updateCount(@Param("id")Integer id);

    Optional<Notice> findTopByOrderByIdDesc();
}
