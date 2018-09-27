package com.hd.batch;

import javax.sql.DataSource;

import com.hd.batch.processor.UpcProcessor;
import com.hd.batch.to.Upc;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    // tag::readerwriterprocessor[]
    @Bean
    public FlatFileItemReader<Upc> reader() {
        return new FlatFileItemReaderBuilder<Upc>()
                .name("upcReader")
                .resource(new ClassPathResource("sample-data.csv"))
                .delimited()
                .names(new String[]{"name_id", "upc", "product_description"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Upc>() {{
                    setTargetType(Upc.class);
                }})
                .build();
    }

    @Bean
    public UpcProcessor processor() {
        return new UpcProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Upc> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Upc>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Upc>())
                .sql("INSERT INTO upc (name_id, upc, product_description) VALUES (:nameId, :upc, :productDescription)")
                .dataSource(dataSource)
                .build();
    }
    // end::readerwriterprocessor[]

    // tag::jobstep[]
    @Bean
    public Job importUpcJob(JobCompletionNotificationListener listener, Step step1) {
        return jobBuilderFactory.get("importUpcJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(JdbcBatchItemWriter<Upc> writer) {
        return stepBuilderFactory.get("step1")
                .<Upc, Upc> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();
    }
    // end::jobstep[]
}