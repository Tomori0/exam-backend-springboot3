package com.up9e.exam.dao;

import com.up9e.exam.entity.ExamQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamQuestionDao extends JpaRepository<ExamQuestion, String> {

}
