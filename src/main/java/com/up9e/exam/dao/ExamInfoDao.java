package com.up9e.exam.dao;

import com.up9e.exam.entity.ExamInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.util.List;

@Repository
public interface ExamInfoDao extends JpaRepository<ExamInfo, String> {

    List<ExamInfo> findByExpireTime(Time time);

    List<ExamInfo> findByExpireTimeNot(Time time);
}
