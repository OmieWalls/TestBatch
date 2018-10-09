package com.hd.batch.util;

import com.hd.batch.to.Event;

import java.util.Comparator;

public class NewestComparator implements Comparator<Event> {
    @Override
    public int compare(Event o1, Event o2) {
        return o1.getEventTime().compareTo(o2.getEventTime());
    }
}

