package com.nicekkong.springbatch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ScopeJobConfiguration {

    final JobBuilderFactory jobBuilderFactory;
    final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job scopeJob() {
        return jobBuilderFactory.get("scopeJob")
                .start(scopeStep1(null))    // JobParameter 할당은 어플리케이션 실행시에 정해지지 않는다.
                .next(scopeStep2())
                .build();
    }

    @Bean
    @JobScope   // Step 선언문에서 사용 가능, Job 실행 시점에서 Batch가 생성된다.
    public Step scopeStep1(@Value("#{jobParameters[requestDate]}")String requestDate) { // JonParameter로 LocalDate는 지원하지 않으므로, String으로 변환하여 사용해야 된다.
        return stepBuilderFactory.get("scopeStep1")
                .tasklet((stepContribution, chunkContext) -> {
                    log.info(" >>>> This is scopeStep1");
                    log.info(" >>>> requestDate = {}", requestDate);
                    return RepeatStatus.FINISHED;
                }).build();
    }


    @Bean
    public Step scopeStep2() {
        return stepBuilderFactory.get("scopeStep2")
                .tasklet(scopeStep2Tasklet(null))
                .build();
    }

    @Bean
    @StepScope  // Tasklet, ItemReader, ItemWriter, ItemProcessor 에서 사용 가능, 실행
    public Tasklet scopeStep2Tasklet(@Value("#{jobParameters[requestDate]}")String requestDate) {
        return (stepContribution, chunkContext) -> {
            log.info(" >>>>>> This is step2");
            log.info(" >>>>>>> requestDate = {}", requestDate);
            return RepeatStatus.FINISHED;
        };
    }
}
