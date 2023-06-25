package org.yascode.batchprocessing.batch.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;
import org.yascode.batchprocessing.batch.writer.DatabaseItemWriter;
import org.yascode.batchprocessing.model.User;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class BatchConfigDataSource {

    private final JobRepository jobRepository;

    private final PlatformTransactionManager batchTransactionManager;

    private final DatabaseItemWriter databaseItemWriter;

    private static final int BATCH_SIZE = 10;

    @Bean
    public Job myJob(JobRepository jobRepository, Step step) {
        return new JobBuilder("myJob")
                .repository(jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(step)
                .end()
                .build();
    }

    @Bean
    public Step step(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) throws IOException {
        return new StepBuilder("step1", jobRepository)
                .<User, User> chunk(10, batchTransactionManager)
                .reader(reader2())
                .processor(processor())
                .writer(writer3())
                .build();
    }

    @Bean
    public FlatFileItemReader<User> reader2() {
        FlatFileItemReader<User> reader = new FlatFileItemReader<>();
        reader.setName("userItemReader");
        reader.setResource(new ClassPathResource("data/24-06-2023.csv"));

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(",");
        tokenizer.setNames("FirstName", "LastName", "Age", "Country");

        DefaultLineMapper<User> userLineMapper = new DefaultLineMapper<>();
        userLineMapper.setLineTokenizer(tokenizer);

        FieldSetMapper<User> fieldSetMapper = fieldSet -> {
            User user = new User();
            user.setFirstName(fieldSet.readString("FirstName"));
            user.setLastName(fieldSet.readString("LastName"));
            user.setAge(fieldSet.readInt("Age"));
            user.setCountry(fieldSet.readString("Country"));
            return user;
        };

        userLineMapper.setFieldSetMapper(fieldSetMapper);

        reader.setLineMapper(userLineMapper);

        reader.setLinesToSkip(1);

        return reader;
    }

    @Bean
    public ItemProcessor<User, User> processor() {
        return user -> user.getAge()>18 ? user : null;
    }

    @Bean
    public ItemWriter<User> writer2() {
        return items -> {
            for (var item : items) {
                System.out.println( item);
            }
        };
    }

    public ItemWriter<User> writer3() {
        return databaseItemWriter;
    }

}
