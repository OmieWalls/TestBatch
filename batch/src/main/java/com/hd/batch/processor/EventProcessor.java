package com.hd.batch.processor;

import com.google.appengine.api.datastore.Entity;
import com.google.cloud.bigquery.FieldValue;
import com.hd.batch.dao.EventDatastoreDAO;
import com.hd.batch.dao.EventBigQueryDAO;
import com.hd.batch.to.Event;
import com.hd.batch.util.LoggerClass;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

@ComponentScan
public class EventProcessor implements ItemProcessor<Event, Event> {

    @Autowired
    public EventBigQueryDAO eventBigQueryDAO;

    @Autowired
    public EventDatastoreDAO eventDatastoreDAO;

    @Autowired
    public SaleProcessor saleProcessor;


    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerClass.class);

    @Override
    public Event process(final Event event) {
        if (event != null) {

            try {

                if (event.getUpc() == null || event.getReceiverId() == null) {
                    String thing = null;
                    throw new Exception(String.format("Error! no matching readers found for tag id: %s  store: %s ", event.getTagId(), event.getStoreNumber()));
                }

                //build event entity with enrichments from sales data
                Entity saveEntity = saleProcessor.process(event);

                //update event_copy: matched & check_count
                eventBigQueryDAO.updateRFIDEvent(saveEntity);

                //write event to datastore
                eventDatastoreDAO.writeEvent(saveEntity);

            } catch (Exception e) {
                // create the error row
                try {
                    LOGGER.error(e.getMessage());
                    LOGGER.error(String.format("Got exception processing event.  Exception: %s. Event: %s ", e.getMessage(), event));
                    eventBigQueryDAO.writeRFIDError(e.getMessage(),event);
                } catch (Exception bqEx) {
                    LOGGER.error(String.format("Got exception writing to BQ Error table.  Exception: %s. Event: %s ", bqEx.getMessage(), event));
                }
            }
        }
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
                    (eventRow.get(13).getValue() != null ? Boolean.parseBoolean(eventRow.get(13).getValue().toString()): null),
                    null);

        }
        return null;
    }

}