package com.hd.batch;

import com.hd.batch.dao.BigQueryDAO;
import com.hd.batch.dao.DatastoreDAO;
import com.hd.batch.processor.EventProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
@ComponentScan
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;


    // tag::readerwriterprocessor[]
    @Bean
    public ItemReader<Event> reader() {
        //TODO: Return Events from BigQuery
        return null;
    }

    @Bean
    public EventProcessor processor() {
        return new EventProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Event> bigQueryWriter(BigQueryDAO bigQueryWriter) {
        //TODO: Write upsert to BigQuery and write insert to Datastore
        return null;
    }


    @Bean
    public JdbcBatchItemWriter<Event> dataStoreWriter(DatastoreDAO dataStoreWriter) {
        //TODO: Write upsert to BigQuery and write insert to Datastore
        return null;
    }
    // end::readerwriterprocessor[]

    // tag::jobstep[]
    @Bean
    public Job importEventJob(JobCompletionNotificationListener listener, Step step1, Step step2) {
        return jobBuilderFactory.get("importEventJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .next(step2)
                .end()
                .build();
    }

    @Bean
    public Step step1(JdbcBatchItemWriter<Event> bigQueryWriter) {
        return stepBuilderFactory.get("step1")
                .<Event, Event> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(bigQueryWriter)
                .build();
    }

    @Bean
    public Step step2(JdbcBatchItemWriter<Event> dataStoreWriter) {
        return stepBuilderFactory.get("step2")
                .<Event, Event> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(dataStoreWriter)
                .build();
    }
    // end::jobstep[]
}