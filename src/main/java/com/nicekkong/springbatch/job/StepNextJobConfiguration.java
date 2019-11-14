package com.nicekkong.springbatch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class StepNextJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job stepNextJob() {
        return jobBuilderFactory.get("stepNextJob")
                .start(step1())
                .next(step2())
                .next(step3())
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get(">>>>>>>>>> stpe1")
                .tasklet((stepContribution, chunkContext) -> {
                    log.info("::::::::::::::::: STEP1");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get(":>>>>>>>>>>  stpe2")
                .tasklet((stepContribution, chunkContext) -> {
                    log.info(":::::::::::::::::  STEP2");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step step3() {
        return stepBuilderFactory.get(">>>>>>>>>> stpe3")
                .tasklet((stepContribution, chunkContext) -> {
                    log.info(":::::::::::::::::  STEP3");
                    return RepeatStatus.FINISHED;
                }).build();
    }
}
