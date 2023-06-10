package com.up9e.aichat.dao;

import com.up9e.aichat.entity.AIChatInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AIChatInfoDao extends JpaRepository<AIChatInfo, String> {

}
