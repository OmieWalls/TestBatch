package com.hd.batch.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hd.batch.to.Event;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class Util {

    private static Logger LOGGER = LoggerFactory.getLogger(LoggerClass.class);

    /**
     * Returns a predicate that maintains state containing information about what was seen previously.
     * Predicate returns whether the given element was seen for the first time.
     *
     * @param keyExtractor
     * @param <T> any object
     *
     * @return Predicate
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<List<Event>> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add((List<Event>) keyExtractor.apply(t));
    }

    /**
     * Returns formatted DateTime string that corresponds to BigQuery/Datastore Timestamp data type.
     *
     * @param dateTime
     * @return DateTime - formatted Timestamp string
     */
    public static DateTime formatDateTimeFromString(String dateTime) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss z");
        return formatter.parseDateTime(dateTime);
    }

    /**
     * Returns a string formatted to a BigQuery/Datastore Timestamp which does not contain milliseconds.
     *
     * @param dateTime
     * @return String - BigQuery/Datastore Timestamp
     */
    public static String formatStringFromDateTime(DateTime dateTime) {
        DateTimeFormatter formatterNoMillis = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

        return dateTime.toString(formatterNoMillis);
    }

    public static Map<String, List<Event>> sortByKey(Map<String, List<Event>> map) {

        // TreeMap stores values of HashMap
        TreeMap<String, List<Event>> sortedTree = new TreeMap<>(map);

        return new HashMap<>(sortedTree);
    }


    public static Object serialize(String json, Class classType) {
        try {
            return new ObjectMapper().readValue(json, classType);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }
}
