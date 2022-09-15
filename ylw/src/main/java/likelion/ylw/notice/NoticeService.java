package likelion.ylw.notice;

import likelion.ylw.member.Member;
import likelion.ylw.util.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public Page<Notice> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createdDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.noticeRepository.findAll(pageable);
    }

    public Notice getNotice(Integer id) {
        Optional<Notice> notice = this.noticeRepository.findById(id);
        if (notice.isPresent()) {
            return notice.get();
        } else {
            throw new DataNotFoundException("Notice not found");
        }
    }

    public Notice getNoticeByTop1(){
        Optional<Notice> notice = this.noticeRepository.findTopByOrderByIdDesc();
        if (notice.isPresent()) {
            return notice.get();
        } else {
            throw new DataNotFoundException("Notice not found");
        }
    }

    public void create(String title, String content, Member member) {
        Notice notice = new Notice();
        notice.setTitle(title);
        notice.setContent(content);
        notice.setAuthor(member);
        notice.setViewCount(0);
        this.noticeRepository.save(notice);
    }

    public void modify(Notice Notice, String title, String content) {
        Notice.setTitle(title);
        Notice.setContent(content);
        this.noticeRepository.save(Notice);
    }

    public void delete(Notice Notice) {
        this.noticeRepository.delete(Notice);
    }

    @Transactional
    public void updateCount(Notice notice) {
        this.noticeRepository.updateCount(notice.getId());
    }
}
