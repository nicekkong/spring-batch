package com.nicekkong.springbatch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
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
public class StepNextConditionalJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job stepNextConditionalJob() {
        return jobBuilderFactory.get("stepNextConditionalJob")
                .start(conditionalJobStep1())
                    .on(ExitStatus.FAILED.getExitCode())           // "FAILED" 일 경우
                    .to(conditionalJobStep3())     // step3으로 이동한다.

                    .on("*")                // step3결과에 관계없이
                    .end()                         // step3으로 이동하면 Flow가 종료된다.

                .from(conditionalJobStep1())       // step1로부터
                    .on("*")                // Failed 외 모든 경우에 대하여
                    .to(conditionalJobStep2())      // step2로 이동한다.

                    .next(conditionalJobStep3())    // step2가 정상 종료되면 step3으로 이동한다.
                    .on("*")                // step3의 결과에 관계 없이
                    .end()                          // step3으로 이동하면 Flow를 종료한다.

                .end()  // job 종료
                .build();

    }

    @Bean
    public Step conditionalJobStep1() {
        return stepBuilderFactory.get("step1")
                .tasklet((stepContribution, chunkContext) -> {
                    log.info(">>>>> this is stepNextConditionalJob Step1");
                    stepContribution.setExitStatus(ExitStatus.FAILED);  // on() 메서드로 전달되는 상태값은 ExitStatus 이다.
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step conditionalJobStep2() {
        return stepBuilderFactory.get("conditionalJobStep2")
                .tasklet((stepContribution, chunkContext) -> {
                    log.info(">>>>>>>>>>>> This is stepNextConditionalJob Step2");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }


    @Bean
    public Step conditionalJobStep3() {
        return stepBuilderFactory.get("conditionalJobStep3")
                .tasklet((stepContribution, chunkContext) -> {
                    log.info(">>>>>>>>>>>> This is stepNextConditionalJob Step3");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
