package com.hd.batch.step.writer;

import com.hd.batch.dao.EventBigQueryDAO;
import com.hd.batch.dao.EventDatastoreDAO;
import com.hd.batch.to.Event;
import com.hd.batch.util.LoggerClass;
import com.hd.batch.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class EventWriter implements ItemWriter<List<Event>> {

    @Autowired
    Logger LOGGER = LoggerFactory.getLogger(LoggerClass.class);

    @Autowired
    Util util;


    @Autowired
    EventBigQueryDAO bigQueryDAO;

    @Autowired
    EventDatastoreDAO datastoreDAO;

    @Override
    public void write(List<? extends List<Event>> entities) {

        entities.forEach(storeLevelEntities -> {
            storeLevelEntities.forEach(entity -> bigQueryDAO.updateEvent(entity.toEntity()));
            datastoreDAO.writeEntity(storeLevelEntities);
        });
    }
}

