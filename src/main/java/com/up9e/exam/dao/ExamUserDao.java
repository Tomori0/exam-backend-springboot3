package com.up9e.exam.dao;

import com.up9e.exam.entity.ExamUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamUserDao extends JpaRepository<ExamUser, String> {
}
