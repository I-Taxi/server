package com.itaxi.server.notice.domain.repository;

import com.itaxi.server.notice.domain.Notice;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
