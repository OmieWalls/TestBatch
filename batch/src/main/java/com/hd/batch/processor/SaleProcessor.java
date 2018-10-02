package com.hd.batch.processor;

import com.google.appengine.api.datastore.Entity;
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
import java.util.Map;
import java.util.UUID;

public class SaleProcessor implements ItemProcessor<Event, Entity> {

    @Autowired
    public SaleBigQueryDAO saleBigQueryDAO;

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerClass.class);

    @Override
    public Entity process(final Event event) {

        String register = null;

        Date eventTsInMillis = new Date(event.getEventTime().getMillis());

        //query sales if exit reader
        if (event.getExitReader()) {//TODO exclude these in the query

            DateTime startTime = event.getEventTime().minusMinutes(20); // 20 minutes prior to event
            DateTime endTime = event.getEventTime().plusMinutes(10);    // 10 minutes post event

            // *** create the formatter with the "no-millis" format - is there a better way to do this???
            DateTimeFormatter formatterNoMillis = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

            String endTimeNoMillis = endTime.toString(formatterNoMillis);
            String startTimeNoMillis = startTime.toString(formatterNoMillis);

            Map<String, Object> salesMap = null;
            try {
                salesMap = saleBigQueryDAO.getMatchingSales(event.getStoreNumber(), startTimeNoMillis, endTimeNoMillis, event.getUpc());
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
            register = (String) salesMap.get("register");

            //populate the data we got from salesMap due to a match
            if (register != null) {
                event.setMatched(true);
            }
        }

        //increment check counter - regardless of match
        event.setCheckedCounter(event.getCheckedCounter()+1);

        return createEventEntity(
                event.getReceiverId(),
                eventTsInMillis,
                event.getExitReader(),
                event.getLocation(),
                event.getProductName(),
                event.getCurrRetailAmt(),
                event.getUpc(),
                event.getStoreNumber(),
                event.getTagId(),
                register,
                event.getCheckedCounter(),
                event.getMatched()
        );
    }

    private Entity createEventEntity(String readerId, Date eventTime, Boolean exitReader, String location, String productName,
                                     Double currRetailAmt, String upc, String storeNumber,
                                     String tagId, String register, int checkedCounter, Boolean matched) {
        Entity eventEntity = new Entity("event", UUID.randomUUID().toString());
        //reader info
        eventEntity.setProperty("curr_ts", new Date());
        eventEntity.setProperty("reader_id", readerId);
        eventEntity.setProperty("event_status", "new");
        eventEntity.setProperty("event_timestamp", eventTime);
        eventEntity.setProperty("exit_event", exitReader);
        eventEntity.setProperty("location", location);
        eventEntity.setProperty("product_image_url", "");
        eventEntity.setProperty("product_name", productName);
        eventEntity.setProperty("product_price", currRetailAmt);
        eventEntity.setProperty("upc", upc);
        eventEntity.setProperty("store", storeNumber);
        eventEntity.setProperty("tag_id", tagId);
        eventEntity.setProperty("video_url", "");
        eventEntity.setProperty("matched", matched);
        eventEntity.setProperty("checkedCounter", checkedCounter);
        if(register != null){
            eventEntity.setProperty("register", register);
        }else{
            eventEntity.setProperty("register", "");
        }

        return eventEntity;
    }
}