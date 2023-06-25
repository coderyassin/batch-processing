package org.yascode.batchprocessing.batch.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class BatchConfig {

    public static final Logger logger = LoggerFactory.getLogger(BatchConfig.class);


    private final JobLauncher jobLauncher;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager batchTransactionManager;
    private static final int BATCH_SIZE = 5;

    @Bean
    public Job firstJob() {
        return new JobBuilder("firstJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(chunkStep())
                .next(taskletStep())
                .build();
    }

    @Bean
    public Step taskletStep() {
        return new StepBuilder("taskletStep", jobRepository)
                .tasklet((stepContribution, chunkContext) -> {
                    logger.info("This is first tasklet step");
                    logger.info("SEC = {}", chunkContext.getStepContext().getStepExecutionContext());
                    return RepeatStatus.FINISHED;
                }, batchTransactionManager).build();
    }

    @Bean
    public Step chunkStep() {
        return new StepBuilder("first step", jobRepository)
                .<String, String>chunk(BATCH_SIZE, batchTransactionManager)
                .reader(reader())
                .writer(writer())
                .build();
    }

    @Bean
    public ItemReader<String> reader() {
        List<String> data = Arrays.asList("Byte", "Code", "Data", "Disk", "File", "Input", "Loop", "Logic", "Mode", "Node", "Port", "Query", "Ratio", "Root", "Route", "Scope", "Syntax", "Token", "Trace");
        return new ListItemReader<>(data);
    }

    @Bean
    public ItemWriter<String> writer() {
        return items -> {
            for (var item : items) {
                logger.info("Writing item: {}", item);
            }
        };
    }

}
