package com.itaxi.server.notice.application;

import com.itaxi.server.exception.notice.NoticeNotFoundException;
import com.itaxi.server.notice.application.dto.NoticeCreateDto;
import com.itaxi.server.notice.application.dto.NoticeUpdateDto;
import com.itaxi.server.notice.domain.repository.NoticeRepository;
import com.itaxi.server.notice.domain.Notice;
import com.itaxi.server.notice.presentation.response.NoticeReadAllResponse;
import com.itaxi.server.notice.presentation.response.NoticeReadResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;

    public Long createNotice(NoticeCreateDto noticeCreateDto) {
        Notice savedNotice = noticeRepository.save(new Notice(noticeCreateDto));

        return savedNotice.getId();
    }

    public NoticeReadResponse readNotice(Long noticeId) {
        NoticeReadResponse response = null;

        Optional<Notice> notice = noticeRepository.findById(noticeId);
        if (notice.isPresent()) {
            Notice noticeInfo = notice.get();
            response = new NoticeReadResponse(noticeInfo.getTitle(), noticeInfo.getContent(), noticeInfo.getViewCnt(), noticeInfo.getCreatedAt());
        } else {
            throw new NoticeNotFoundException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response;
    }

    public List<NoticeReadAllResponse> readAllNotices() {
        List<NoticeReadAllResponse> result = new ArrayList<>();
        for (Notice notice : noticeRepository.findAll()) {
            if (notice != null) {
                result.add(new NoticeReadAllResponse(notice.getId(), notice.getTitle(), notice.getContent(), notice.getViewCnt(), notice.getCreatedAt()));
            }
        }

        return result;
    }

    public String updateNotice(Long noticeId, NoticeUpdateDto noticeUpdateDto) {
        Optional<Notice> notice = noticeRepository.findById(noticeId);
        if (notice.isPresent()) {
            Notice noticeInfo = notice.get();
            noticeInfo.setTitle(noticeUpdateDto.getTitle());
            noticeInfo.setContent(noticeUpdateDto.getContent());
            noticeRepository.save(noticeInfo);
        } else {
            throw new NoticeNotFoundException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return "Success";
    }

    public String deleteNotice(Long noticeId) {
        Optional<Notice> notice = noticeRepository.findById(noticeId);
        if (notice.isPresent()) {
            Notice noticeInfo = notice.get();
            noticeInfo.setDeleted(true);
            noticeRepository.save(noticeInfo);
        } else {
            throw new NoticeNotFoundException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return "Success";
    }

    public long updateNoticeViewCnt(Long noticeId) {
        Notice noticeInfo = null;
        Optional<Notice> notice = noticeRepository.findById(noticeId);
        if (notice.isPresent()) {
            noticeInfo = notice.get();
            noticeInfo.setViewCnt(noticeInfo.getViewCnt() + 1);
            noticeRepository.save(noticeInfo);
        } else {
            throw new NoticeNotFoundException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return noticeInfo.getViewCnt();
    }
}