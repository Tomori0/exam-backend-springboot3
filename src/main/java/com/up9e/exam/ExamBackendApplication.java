package com.up9e.exam;

import com.up9e.exam.global.BusinessException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAsync
@EnableTransactionManagement
public class ExamBackendApplication {

    public static void main(String[] args) throws BusinessException {
        SpringApplication.run(ExamBackendApplication.class, args);
    }

}
