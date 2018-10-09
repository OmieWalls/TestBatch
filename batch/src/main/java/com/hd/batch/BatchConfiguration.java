package com.hd.batch;

import com.google.appengine.api.datastore.Entity;
import com.hd.batch.processor.processor.EventProcessor;
import com.hd.batch.processor.reader.EventReader;
import com.hd.batch.to.Event;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableBatchProcessing
@ComponentScan
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory job;

    @Autowired
    public StepBuilderFactory steps;

    public ItemReader<List<Event>> eventReader() throws Exception { return new EventReader(); }

    public ItemProcessor<List<Event>, List<Entity>> eventProcessor() { return new EventProcessor(); }

    public ItemWriter<List<Entity>> eventWriter() {
        return null;
    }


    @Bean
    public Job eventJob() throws Exception {
        return this.job.get("eventJob")
                .start(eventLoad())
                .build();
    }

    @Bean
    public Step eventLoad(JobCompletionNotificationListener listener) throws Exception {
        return this.steps.get("eventLoad")
                .<List<Event>, List<Entity>>chunk(10)
                .reader(eventReader())
                .processor(eventProcessor())
                .writer(eventWriter())
                .listener(listener)
                .build();
    }
}