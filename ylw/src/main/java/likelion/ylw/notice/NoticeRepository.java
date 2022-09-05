package likelion.ylw.notice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface NoticeRepository extends JpaRepository<Notice, Integer> {

    @Modifying
    @Query("update Notice p set p.viewCount = p.viewCount + 1 where p.id = :id")
    int updateCount(Integer id);
}
