package com.up9e.aichat.dao;

import com.up9e.aichat.entity.AIChatPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AIChatPriceDao extends JpaRepository<AIChatPrice, String> {

    List<AIChatPrice> findAllByMonth(Integer month);
}
