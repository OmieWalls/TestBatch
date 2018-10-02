package com.hd.batch.services.impl;

import com.google.cloud.bigquery.*;
import com.google.appengine.api.datastore.Entity;
import com.hd.batch.dao.BigQueryDAO;
import com.hd.batch.dao.DatastoreDAO;
import com.hd.batch.services.EventServiceInterface;
import com.hd.batch.to.Event;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.*;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImpl implements EventServiceInterface {

    @Autowired
    public BigQueryDAO bigQueryDAO;
    @Autowired
    public DatastoreDAO datastoreDAO;

    private static final Logger LOG = Logger.getLogger(Event.class.getName());

    public void processEventData()  {
        //convert any existing hex tag values to ascii
        //TODO function to convert tags from hex to ascii
        bigQueryDAO.updateBigQueryEntityWithHexToASCIIByLCP(lcp);
        //read messages from the subscription
        TableResult eventList = bigQueryDAO.getRFIDEventDataFromLCP(lcp);
        if (eventList != null && eventList.getTotalRows() > 0) {
            for (List<FieldValue> rowDt : eventList.iterateAll()) {
                if (rowDt.get(0).getValue() != null) {

                    Event event = null;

                    try {
                        //parse to json
                        event = parseRFIDEvent(rowDt);

                        if(event.getUpc() == null){
                            throw new Exception(String.format("no matching tag data found for tag id: %s  store: %s ", event.getTagId(), event.getStoreNumber()));
                        }
                        if(event.getReceiverId() == null){
                            throw new Exception(String.format("no matching readers found for tag id: %s  store: %s ", event.getTagId(), event.getStoreNumber()));
                        }

                        //build event entity with enrichments
                        Entity saveEntity = analyzeRFIDEvent(event, lcp);
                        //update event_copy: matched & check_count;
                        bigQueryDAO.updateRFIDEventToBigQueryEntity(saveEntity, lcp);
                        //write event to datastore
                        datastoreDAO.writeEntity(saveEntity);

                    } catch (Exception ex) {
                        // create the error row
                        try {
                            ex.printStackTrace();
                            LOG.severe(String.format("Got exception processing event.  Exception: %s. Event: %s ", ex.getMessage(), event));
                            bigQueryDAO.writeRFIDEventErrorToBigQuery(ex.getMessage(), event, lcp);
                        } catch (Exception bqex) {
                            bqex.printStackTrace();
                            LOG.severe(String.format("Got exception writing to BQ Error table.  Exception: %s. Event: %s ", bqex.getMessage(), event));
                        }
                    }
                }
            }
        }
    }


    private Event parseRFIDEvent(List<FieldValue> eventRow) {
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

    private Entity analyzeRFIDEvent(Event event, String lcp) throws Exception {

        String register = null;

        Date eventTs = new Date(event.getEventTime().getMillis());

        //query sales if exit reader
        if (event.getExitReader()) {//TODO exclude these in the query
            DateTime endTime = event.getEventTime().plusMinutes(10);
            // *** create the formatter with the "no-millis" format - is there a better way to do this???
            DateTimeFormatter formatterNoMillis = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            String endTimeNoMillis = endTime.toString(formatterNoMillis);
            DateTime startTime = event.getEventTime().minusMinutes(20);
            String startTimeNoMillis = startTime.toString(formatterNoMillis);

            HashMap<String, Object> salesProps = bigQueryDAO.lookupMatchingSales(event.getStoreNumber(), startTimeNoMillis, endTimeNoMillis, event.getUpc(), lcp);
            register = (String) salesProps.get("register");

            //populate the data we got from sales due to a match
            if (register != null) {
                event.setMatched(true);
            }
        }

        //increment check counter - regardless of match
        event.setCheckedCounter(event.getCheckedCounter()+1);

        return createEventEntity(
                event.getReceiverId(),
                eventTs,
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
