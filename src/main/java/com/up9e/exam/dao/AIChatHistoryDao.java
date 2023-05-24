package com.up9e.exam.dao;

import com.up9e.exam.entity.AIChatHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AIChatHistoryDao extends JpaRepository<AIChatHistory, String> {

}
