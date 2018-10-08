package com.hd.batch;

import com.google.appengine.api.datastore.Entity;
import com.hd.batch.to.Event;
import com.hd.batch.to.Sale;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
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

    List<Event> events = new ArrayList<>();

    List<Sale> sales = new ArrayList<>();

    public ItemReader<Event> eventReader() { return null; }

    public ItemWriter<Event> eventWriter(List events) {
        return null;
    }

    public ItemReader<Event> saleReader(List events) { return null; }

    public ItemWriter<Sale> saleWriter(List sales) {
        return null;
    }

    public ItemProcessor<Map<String, Object>, Entity> matchProcessor(List events, List sales) { return null; }

    public ItemWriter<Entity> matchWriter() {
        return null;
    }

    @Bean
    public Job eventJob() {
        return this.job.get("eventJob")
                .start(eventLoad())
                .next(saleLoad())
                .next(match())
                .build();
    }

    @Bean
    public Step eventLoad() {
        return this.steps.get("eventLoad")
                .<Event, Event>chunk(10)
                .reader(eventReader())
                .writer(eventWriter(events))
                .build();
    }

    @Bean
    public Step saleLoad() {
        return this.steps.get("saleMatch")
                .allowStartIfComplete(true)
                .<Event, Sale>chunk(10)
                .reader(saleReader(events))
                .writer(saleWriter(sales))
                .build();
    }

    @Bean
    public Step match() {
        return this.steps.get("match")
                .startLimit(3)
                .<Map<String, Object>, Entity>chunk(10)
                .processor(matchProcessor(events, sales))
                .writer(matchWriter())
                .build();
    }
}