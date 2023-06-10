package com.up9e.aichat.dao;

import com.up9e.aichat.entity.AIChatUserLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AIChatUserLevelDao extends JpaRepository<AIChatUserLevel, String> {

}
