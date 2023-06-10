package com.up9e.aichat.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "ai_chat_price", schema = "public")
public class AIChatPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer     id;
    private Integer     level;
    private Integer     month;
    private BigDecimal  price;

    @Override
    public String toString() {
        return "AIChatPrice{" +
                "id=" + id +
                ", level='" + level + '\'' +
                ", month=" + month +
                ", price='" + price + '\'' +
                '}';
    }
}
