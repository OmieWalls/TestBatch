package com.hd.batch.processor.processor;

import com.hd.batch.dao.SaleBigQueryDAO;
import com.hd.batch.to.Event;
import com.hd.batch.to.Sale;
import com.hd.batch.util.LoggerClass;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ComponentScan
public class EventProcessor implements ItemProcessor<List<Event>, List<Event>> {

    @Autowired
    public SaleBigQueryDAO bigQueryDAO;


    @Autowired
    Logger LOGGER = LoggerFactory.getLogger(LoggerClass.class);


    @Override
    public List<Event> process(List<Event> events) throws Exception {

        // get the time window for the entire list of events for the store
        Map<String, String> timeWindow = getSalesTimeWindowFromEventList(events);

        // compile list of UPCs from the given events
        List<String> listOfUPCs = new ArrayList<>();

        for (Event event : events) {
            listOfUPCs.add(event.getUpc());
        }

        // get the store number (all events per process have the same store number)
        String storeNumber = events.get(0).getStoreNumber();

        List<Sale> sales = bigQueryDAO.getMatchingSales(storeNumber, timeWindow, listOfUPCs);

        return validateEvents(events, sales);
    }

    private List<Event> validateEvents(List<Event> events, List<Sale> sales) {
        return null; //TODO: Perform validation...
    }

    private Map<String, String> getSalesTimeWindowFromEventList(List<Event> events) {

        // define time window = 20 minutes before and 10 minutes after list of events
        DateTime startTime = events.get(0).getEventTime().minusMinutes(20);
        DateTime endTime = events.get(events.size() - 1).getEventTime().plusMinutes(10);

        // create the formatter with the "no-millis" format
        DateTimeFormatter formatterNoMillis = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");


        String endTimeNoMillis = endTime.toString(formatterNoMillis);
        String startTimeNoMillis = startTime.toString(formatterNoMillis);

        Map<String, String> timeWindowMap = new HashMap<>();
        timeWindowMap.put("start", startTimeNoMillis);
        timeWindowMap.put("end", endTimeNoMillis);

        return timeWindowMap;
    }
}
