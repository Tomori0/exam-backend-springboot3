package com.up9e.exam.dao;

import com.up9e.exam.entity.AIChatInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AIChatInfoDao extends JpaRepository<AIChatInfo, String> {

}
