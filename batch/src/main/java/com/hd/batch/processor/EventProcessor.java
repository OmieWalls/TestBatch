package com.hd.batch.processor;

import com.google.appengine.api.datastore.Entity;
import com.google.cloud.bigquery.FieldValue;
import com.google.cloud.bigquery.TableResult;
import com.hd.batch.dao.EventDatastoreDAO;
import com.hd.batch.dao.EventBigQueryDAO;
import com.hd.batch.dao.SaleBigQueryDAO;
import com.hd.batch.to.Event;
import com.hd.batch.util.LoggerClass;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class EventProcessor implements ItemProcessor<Event, Event> {

    @Autowired
    public EventBigQueryDAO eventBigQueryDAO;
    public SaleBigQueryDAO saleBigQueryDAO;
    @Autowired

    public EventDatastoreDAO eventDatastoreDAO;

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerClass.class);

    @Override
    public Event process(final Event event) {
        //TODO: Process event
        return null;
    }


    private Event parseEvent(List<FieldValue> eventRow) {
        if (eventRow != null) {
            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss z");
            DateTime eventTime = formatter.parseDateTime(eventRow.get(4).getValue().toString());
            return new Event(
                    (eventRow.get(1).getValue() != null ? eventRow.get(1).getValue().toString() : null),
                    (eventRow.get(2).getValue() != null ? eventRow.get(2).getValue().toString(): null),
                    (eventRow.get(12).getValue() != null ? eventRow.get(12).getValue().toString(): null),
                    eventTime,
                    (eventRow.get(15).getValue() != null ? Integer.parseInt(eventRow.get(15).getValue().toString()): null),
                    (eventRow.get(7).getValue() != null ? eventRow.get(7).getValue().toString(): null),
                    (eventRow.get(8).getValue() != null ? Boolean.parseBoolean(eventRow.get(8).getValue().toString()): null),
                    (eventRow.get(3).getValue() != null ? eventRow.get(3).getValue().toString(): null),
                    (eventRow.get(11).getValue() != null ? eventRow.get(11).getValue().toString(): null),
                    (eventRow.get(6).getValue() != null ? Double.parseDouble(eventRow.get(6).getValue().toString()): null),
                    (eventRow.get(14).getValue() != null ? Integer.parseInt(eventRow.get(14).getValue().toString()): null),
                    (eventRow.get(13).getValue() != null ? Boolean.parseBoolean(eventRow.get(13).getValue().toString()): null));

        }
        return null;
    }

}