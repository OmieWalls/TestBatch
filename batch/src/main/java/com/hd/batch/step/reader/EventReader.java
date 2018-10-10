package com.hd.batch.step.reader;

import com.google.cloud.bigquery.FieldValue;
import com.google.cloud.bigquery.TableResult;
import com.hd.batch.dao.EventBigQueryDAO;
import com.hd.batch.to.Event;
import com.hd.batch.util.LoggerClass;
import com.hd.batch.util.NewestComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class EventReader implements ItemReader<List<Event>> {

    @Autowired
    EventBigQueryDAO bigQueryDAO;

    @Autowired
    Logger LOGGER = LoggerFactory.getLogger(LoggerClass.class);

    private int nextStoreIndex;
    private Map<String, List<Event>> events;

    public EventReader() {
        initialize();
    }

    private void initialize() {

        bigQueryDAO.updateBigQueryEntityWithHexToASCII();
        TableResult eventTableResult = bigQueryDAO.getEvents();
        events = mapEventsByStoreNumber(eventTableResult);
        nextStoreIndex = 0;
    }

    private Map<String, List<Event>> mapEventsByStoreNumber(TableResult results) {

        Map<String, List<Event>> eventsMap = new HashMap<>();

        for (List<FieldValue> row : results.iterateAll()) {

            String storeNumber = row.get(12).getValue() != null ?
                    row.get(12).getValue().toString() : null;

            // create a new event list if list is not already created for store number and add event to list
            if (eventsMap.get(storeNumber) == null) {

                List<Event> newEventList = new ArrayList<>();
                newEventList.add(new Event(row));
                eventsMap.put(storeNumber, newEventList);

            } else { // add event to list that is already created
                eventsMap.get(storeNumber).add(new Event(row));
            }
        }
        return eventsMap;
    }

    @Override
    public List<Event> read() throws Exception {
        List<Event> nextEvents = null;

        if (nextStoreIndex < events.keySet().size()) {


            Set storeNumber = Arrays.asList(events.keySet()).get(nextStoreIndex);
            nextEvents = events.get(storeNumber);

            nextEvents.sort(new NewestComparator()); // sort by eventTime

            nextStoreIndex++;

            LOGGER.trace("Starting EventReader #" + nextStoreIndex + " for store number " + storeNumber);
        }

        return nextEvents;
    }
}