package com.up9e.exam.recode;

import java.util.Date;
import java.util.List;

public record ExamSubmit(List<String> answer, String infoId, Date timestamp, String verify) {
    @Override
    public String toString() {
        return "ExamSubmit{" +
                "answer=" + answer +
                ", infoId='" + infoId + '\'' +
                ", timestamp=" + timestamp +
                ", verify='" + verify + '\'' +
                '}';
    }
}
