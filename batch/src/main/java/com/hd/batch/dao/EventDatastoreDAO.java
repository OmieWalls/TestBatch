package com.hd.batch.dao;

import com.google.appengine.api.datastore.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventDatastoreDAO {

    @Autowired
    DatastoreDAO datastoreDAO;

    /**
     * write entity event to datastore
     *
     * @param event event entity to be written
     */
    public void writeEvent(Entity event) {
        datastoreDAO.writeEntity(event);
    }
}
