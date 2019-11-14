package com.nicekkong.springbatch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j      // Log 사용을 위한 Lombok
@RequiredArgsConstructor    // 생성자 DI를 위한 Lombok
@Configuration  // SpringBatch의 모든 Job은 Configuration 어노테이션으로 등록해서 사용한다.
public class SimpleJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job simpleJob() {
        return jobBuilderFactory.get("simpleJob")       // "simpleJob" 이란 이름의 BatchJob 생성
                .start(simpleStep1(null))
                .next(simpleStep2(null))
                .build();
    }

    @Bean
    @JobScope
    public Step simpleStep1(@Value("#{jobParameters[requestDate]}") String requestDate) {
        return stepBuilderFactory.get("simpleStep1")    // "simpleStep1" 이란 이름의 Batch Step를 생성한다.
                .tasklet((contribution, chunkContext) -> {  // step 안에서 수행될 기능을 명시/단일로 수행될 커스텀한 기능을 선언할 때 사용
                    log.info(">>>>> This is Step1");    // 작업 내역
//                    throw new IllegalArgumentException("Step1 Fail~!!!!!!!!!!!");
                    log.info(">>>Step1 requestDate = {}", requestDate);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    @JobScope
    public Step simpleStep2(@Value("#{jobParameters[requestDate]}") String requestDate) {
        return stepBuilderFactory.get("simpleStep2")    // "simpleStep1" 이란 이름의 Batch Step를 생성한다.
                .tasklet((contribution, chunkContext) -> {  // step 안에서 수행될 기능을 명시/단일로 수행될 커스텀한 기능을 선언할 때 사용
                    log.info(">>>>> This is Step2");    // 작업 내역
                    log.info(">>>Step2 requestDate = {}", requestDate);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
