package com.hd.batch.step.writer;

import com.google.appengine.api.datastore.Entity;
import com.hd.batch.dao.EventBigQueryDAO;
import com.hd.batch.dao.EventDatastoreDAO;
import com.hd.batch.util.LoggerClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class EventWriter implements ItemWriter<List<Entity>> {

    @Autowired
    Logger LOGGER = LoggerFactory.getLogger(LoggerClass.class);

    @Autowired
    EventBigQueryDAO bigQueryDAO;

    @Autowired
    EventDatastoreDAO datastoreDAO;

    @Override
    public void write(List<? extends List<Entity>> entities) {
        try {
            entities.forEach(storeLevelEntities -> {
                storeLevelEntities.forEach(entity -> bigQueryDAO.updateEvent(entity));
                datastoreDAO.writeEntity(storeLevelEntities);
            });
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}

