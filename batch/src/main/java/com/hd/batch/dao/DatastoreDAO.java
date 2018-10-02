package com.hd.batch.dao;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.hd.batch.to.Event;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class DatastoreDAO {

    private static final Logger LOGGER = Logger.getLogger(Event.class.getName());

    /**
     * write entity entity to datastore
     *
     * @param entity entity to be written
     */
    public void writeEntity(Entity entity) {
        LOGGER.info(String.format("Writing entity to datastore.  Entity:   %s ", String.valueOf(entity)));
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        //TODO: Check if existing... This might run twice?
        datastore.put(entity);
    }
}
