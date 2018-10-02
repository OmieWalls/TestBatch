package com.hd.batch.processor;

import com.hd.batch.to.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class EventProcessor implements ItemProcessor<Event, Event> {

    private static final Logger log = LoggerFactory.getLogger(EventProcessor.class);

    @Override
    public Event process(final Event event) {
        //TODO: Process event
       return null;
    }

}