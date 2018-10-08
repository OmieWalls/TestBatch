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
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SaleProcessor implements ItemProcessor<Event, Entity> {

    @Autowired
    SaleBigQueryDAO saleBigQueryDAO;

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerClass.class);

    @Override
    public Entity process(final Event event) {

        String register = null;

        // query sales if exit reader
        if (event.getExitReader()) {

            DateTime startTime = event.getEventTime().minusMinutes(20); // 20 minutes prior to event
            DateTime endTime = event.getEventTime().plusMinutes(10);    // 10 minutes post event

            // create the formatter with the "no-millis" format - is there a better way to do this???
            DateTimeFormatter formatterNoMillis = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

            String endTimeNoMillis = endTime.toString(formatterNoMillis);
            String startTimeNoMillis = startTime.toString(formatterNoMillis);

            Map<String, Object> salesMap = null;

            try {

                salesMap = saleBigQueryDAO.getMatchingSales(event.getStoreNumber(), startTimeNoMillis, endTimeNoMillis, event.getUpc());
                register = String.valueOf(salesMap.get("register"));

            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }

            // populate the data we got from salesMap due to a match
            if (salesMap != null && !salesMap.isEmpty()) {
                event.setMatched(true);
            }
        }

        // increment check counter - regardless of match
        event.setCheckedCounter(event.getCheckedCounter()+1);
        event.setRegister(register);

        return event.entity();
    }
}