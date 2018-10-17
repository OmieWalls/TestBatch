package com.hd.batch.step.reader;

import com.google.cloud.bigquery.TableResult;
import com.hd.batch.dao.EventBigQueryDAO;
import com.hd.batch.to.Event;
import com.hd.batch.util.LoggerClass;
import com.hd.batch.util.NewestComparator;
import com.hd.batch.util.Util;
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

        bigQueryDAO.updateBigQueryEntityConvertTagHexToASCII();

        TableResult eventTableResult = bigQueryDAO.getEvents();

        events = mapEventsByStoreNumber(eventTableResult);
        nextStoreIndex = 0;
    }

    private Map<String, List<Event>> mapEventsByStoreNumber(TableResult results) {

        Map<String, List<Event>> eventsMap = new HashMap<>();

        results.iterateAll().forEach(row -> {

            String storeNumber = row.get(12).getValue() != null ?
                    row.get(12).getValue().toString() : null;

            if (storeNumber != null) {

                List<Event> storeLevelEventList = eventsMap.get(storeNumber) == null ?
                    new ArrayList<>() : eventsMap.get(storeNumber);

                storeLevelEventList.add(new Event(row));

                eventsMap.put(storeNumber, storeLevelEventList);
            }

        });
        return Util.sortByKey(eventsMap);
    }

    @Override
    public List<Event> read() {
        List<Event> nextEvents = null;

        if (nextStoreIndex < events.keySet().size()) {

            Set storeNumber = Collections.singletonList(events.keySet()).get(nextStoreIndex);
            nextEvents = events.get(storeNumber);

            // Takes events that are sorted by timestamp and gets the effective time window for sales
            nextEvents.sort(new NewestComparator()); // sort by eventTime

            nextStoreIndex++;

            LOGGER.trace("Starting EventReader #" + nextStoreIndex + " for store number " + storeNumber);
        }

        return nextEvents;
    }
}