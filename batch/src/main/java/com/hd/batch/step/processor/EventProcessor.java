package com.hd.batch.step.processor;

import com.google.appengine.api.datastore.Entity;
import com.hd.batch.dao.SaleBigQueryDAO;
import com.hd.batch.to.Event;
import com.hd.batch.to.Sale;
import com.hd.batch.util.LoggerClass;
import com.hd.batch.util.Util;
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
import java.util.stream.Collectors;


@ComponentScan
public class EventProcessor implements ItemProcessor<List<Event>, List<Entity>> {

    @Autowired
    public SaleBigQueryDAO bigQueryDAO;


    @Autowired
    Logger LOGGER = LoggerFactory.getLogger(LoggerClass.class);

    @Autowired
    Util util;


    @Override
    public List<Entity> process(List<Event> events) throws Exception {

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

        List<Event> validatedEvents = validateEvents(events, sales);

        validatedEvents.stream().filter(Util.distinctByKey(Event::getTagId));

        return validatedEvents.parallelStream().map(Event::toEntity).collect(Collectors.toList());
    }

    private List<Event> validateEvents(List<Event> events, List<Sale> sales) {
        List<Event> validatedEvents = new ArrayList<>();

        for (Event event : events) {

            List<Sale> salesMatchList = sales.stream()
                    .filter(sale -> event.getUpc().equalsIgnoreCase(sale.getUpcCode()))
                    .filter(sale -> event.getEventTime().minusMinutes(20).getMillis() < sale.getSalesTsLocal().getMillis())
                    .filter(sale -> event.getEventTime().plusMinutes(10).getMillis() > sale.getSalesTsLocal().getMillis())
                    .filter(sale -> sale.getRegisterNumber() != null)
                    .collect(Collectors.toList());

            event.setMatched(salesMatchList.size() != 0);

            event.setCheckedCounter(event.getCheckedCounter()+1);
        }
        //todo: Indicate false positives eventually... We should flag them as UNDETERMINED (Other flags are THEFT, DISMISSED, and NEW).
        return validatedEvents;
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
