package com.up9e.exam.recode;

import java.util.Date;

public record QuestionAnswer(String infoId, String questionId, Date timestamp, String verify) {
    @Override
    public String toString() {
        return "QuestionAnswer{" +
                "infoId=" + infoId +
                ", questionId=" + questionId +
                ", timestamp=" + timestamp +
                ", verify='" + verify + '\'' +
                '}';
    }
}
