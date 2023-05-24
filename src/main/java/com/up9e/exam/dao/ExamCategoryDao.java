package com.up9e.exam.dao;

import com.up9e.exam.entity.ExamCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamCategoryDao extends JpaRepository<ExamCategory, Integer> {

}
